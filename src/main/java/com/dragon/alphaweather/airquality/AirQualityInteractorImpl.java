package com.dragon.alphaweather.airquality;

import com.dragon.alphaweather.utils.CacheUtil;

/**
 * Created by Administrator on 2017/4/5.
 */

public class AirQualityInteractorImpl implements AirQualityInteractor {
    @Override
    public String getCitysString() {
        return CacheUtil.getACache().getAsString("citys");
    }
}
