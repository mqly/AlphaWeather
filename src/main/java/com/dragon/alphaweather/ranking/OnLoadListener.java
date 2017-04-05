package com.dragon.alphaweather.ranking;

/**
 * Created by Administrator on 2017/4/5.
 */

public interface OnLoadListener {
    void onLoadSuccess(String str);

    void onLoadFailure(Exception e);
}
