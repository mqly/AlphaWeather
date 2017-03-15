package com.dragon.alphaweather.utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017/2/19.
 */

public class OkHttpUtil {
    private static OkHttpClient okHttpClient;

    public static OkHttpClient getOkHttpClient() {
        synchronized (OkHttpUtil.class) {
            if (okHttpClient == null) {
                okHttpClient = new OkHttpClient.Builder()//
                        .readTimeout(Constant.readTimeOut, TimeUnit.SECONDS)//
                        .writeTimeout(Constant.writeTimeOut, TimeUnit.SECONDS)//
                        .connectTimeout(Constant.connTimeOut, TimeUnit.SECONDS)//
                        .retryOnConnectionFailure(true)//
                        .build();

            }
        }
        return okHttpClient;
    }
}
