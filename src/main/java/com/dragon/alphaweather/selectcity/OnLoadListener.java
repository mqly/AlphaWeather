package com.dragon.alphaweather.selectcity;

/**
 * Created by Administrator on 2017/4/5.
 */

public interface OnLoadListener {
    void onLoadSuccess(String str);

    void onLoadFailure(Exception e);
}
