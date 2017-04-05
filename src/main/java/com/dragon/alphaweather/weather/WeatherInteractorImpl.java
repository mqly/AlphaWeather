package com.dragon.alphaweather.weather;

import com.dragon.alphaweather.utils.CacheUtil;

/**
 * Created by Administrator on 2017/4/4.
 */

public class WeatherInteractorImpl implements WeatherInteractor {

    @Override
    public String getCityInfo(String cityId) {
        return CacheUtil.getACache().getAsString(cityId);
    }

    @Override
    public String getCitysString() {
        return CacheUtil.getACache().getAsString("citys");
    }
}
