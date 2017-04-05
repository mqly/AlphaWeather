package com.dragon.alphaweather.citymanage;

/**
 * Created by Administrator on 2017/4/5.
 */

public interface CityManageInteractor {
    String getCitysString();

    void getAirWeatherFromNet(String cityId, OnLoadListener listener);

    void removeCitys();

    void putAllCitys(String citys);

    void putCity(String cityId, String cityString);
}
