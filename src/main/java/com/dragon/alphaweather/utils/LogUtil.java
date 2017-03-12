package com.dragon.alphaweather.utils;

import android.util.Log;

/**
 * Created by Administrator on 2017/2/19.
 */

public class LogUtil {
    public static boolean isDebug = true;

    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }
}
