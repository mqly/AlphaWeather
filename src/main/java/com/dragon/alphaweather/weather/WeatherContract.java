package com.dragon.alphaweather.weather;

import com.dragon.alphaweather.BasePresenter;
import com.dragon.alphaweather.BaseView;

import java.util.List;

/**
 * Created by Administrator on 2017/4/4.
 */

public interface WeatherContract {
    interface View extends BaseView<Presenter> {
        void showSelectCityDialog();
    }

    interface Presenter extends BasePresenter {
        List getCityWeather();

        List getCityList();

        boolean hasCitys();
        void loadCitys();
    }
}
