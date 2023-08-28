package com.bluetooth.cup.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    public static String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        return "TIME:" + formatter.format(curDate);
    }
}
