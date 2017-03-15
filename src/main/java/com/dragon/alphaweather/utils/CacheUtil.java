package com.dragon.alphaweather.utils;

import com.dragon.alphaweather.application.MyApplication;
import com.dragon.alphaweather.cache.ACache;

/**
 * Created by Administrator on 2017/2/19.
 */

public class CacheUtil {
    private static ACache myCache;

    public static ACache getACache() {
        synchronized (CacheUtil.class) {
            if (myCache == null) {
                myCache = ACache.get(MyApplication.getContext());

            }
        }
        return myCache;
    }
}
