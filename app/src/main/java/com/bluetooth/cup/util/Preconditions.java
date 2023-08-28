package com.bluetooth.cup.util;

import android.util.Log;

import androidx.annotation.NonNull;


public class Preconditions {
    private static final String TAG = Preconditions.class.getSimpleName();
    public static void checkNotNull(Object obj,String msg){
        if (obj == null){
            Log.e(TAG,msg);
            throw new NullPointerException(msg);
        }
    }

    @NonNull
    public static <T> T checkNotNullValue(T t, String msg){
        if (t == null){
            Log.e(TAG,msg);
            throw new NullPointerException(msg);
        }
        return t;
    }

    public static <T> boolean isNull(T t){
        return t == null;
    }

    public static <T> boolean isNotNull(T t){
        return t != null;
    }

}