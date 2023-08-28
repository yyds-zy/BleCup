package com.bluetooth.cup.util;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by 阿飞の小蝴蝶 on 2023/7/31
 * Describe:
 */
public class MiscUtil {
    /**
     * 测量 View
     *
     * @param measureSpec
     * @param defaultSize View 的默认大小
     * @return
     */
    public static int measure(int measureSpec, int defaultSize) {
        int result = defaultSize;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);

        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == View.MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }

    /**
     * dip 转换成px
     *
     * @param dip
     * @return
     */
    public static int dipToPx(Context context, float dip) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 获取数值精度格式化字符串
     *
     * @param precision
     * @return
     */
    public static String getPrecisionFormat(int precision) {
        return "%." + precision + "f";
    }

    /**
     * 反转数组
     *
     * @param arrays
     * @param <T>
     * @return
     */
    public static <T> T[] reverse(T[] arrays) {
        if (arrays == null) {
            return null;
        }
        int length = arrays.length;
        for (int i = 0; i < length / 2; i++) {
            T t = arrays[i];
            arrays[i] = arrays[length - i - 1];
            arrays[length - i - 1] = t;
        }
        return arrays;
    }

    /**
     * 测量文字高度
     * @param paint
     * @return
     */
    public static float measureTextHeight(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return (Math.abs(fontMetrics.ascent) - fontMetrics.descent);
    }

    /**
     * 根据年月日计算星期几
     * @param year
     * @param month
     * @param dayOfMonth
     * @return
     */
    public static String getWeek(int year, int month, int dayOfMonth) {
        String dayName;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        calendar.set(year, month - 1, dayOfMonth);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                dayName = "周日";
                break;
            case Calendar.MONDAY:
                dayName = "周一";
                break;
            case Calendar.TUESDAY:
                dayName = "周二";
                break;
            case Calendar.WEDNESDAY:
                dayName = "周三";
                break;
            case Calendar.THURSDAY:
                dayName = "周四";
                break;
            case Calendar.FRIDAY:
                dayName = "周五";
                break;
            case Calendar.SATURDAY:
                dayName = "周六";
                break;
            default:
                dayName = "Unknown";
                break;
        }
        return dayName;
    }

    //20230815002042
    public static String getCurrentTime(String time) {
        String hour = time.substring(8, 10);
        String min = time.substring(10, 12);
        String sec = time.substring(12, 14);
        String result = hour + ":" + min + ":" + sec;
        Log.d("xuezhiyuan", " getCurrentTime " + result);
        return result;
    }

    //获取日期  日期格式  2月27日  周二
    public static String getDay(String time) {
        String year = time.substring(0, 4);
        String month = time.substring(4, 6);
        String dayOfMonth = time.substring(6, 8);
        String week = getWeek(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(dayOfMonth));
        String result = Integer.parseInt(month) + "月" + Integer.parseInt(dayOfMonth) + "日" + " " + week;
        Log.d("xuezhiyuan", " getDay " + result);
        return result;
    }

    /**
     * byte2string
     * @param byteArray
     * @return
     */
    public static String byte2String(byte[] byteArray) {
        String result = "";
        char temp;

        int length = byteArray.length;
        for (int i = 0; i < length; i++) {
            temp = (char) byteArray[i];
            result += temp;
        }
        return result;
    }
}
