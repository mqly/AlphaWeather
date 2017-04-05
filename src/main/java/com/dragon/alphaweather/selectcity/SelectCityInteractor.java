package com.dragon.alphaweather.selectcity;

import java.util.List;

/**
 * Created by Administrator on 2017/4/5.
 */

public interface SelectCityInteractor {
    List getCityDatasFromDB();

    void saveToDB(List citys);

    void getCityDatasFromNet(OnLoadListener listener);
}
