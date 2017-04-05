package com.dragon.alphaweather.citymanage;

import com.dragon.alphaweather.BasePresenter;
import com.dragon.alphaweather.BaseView;

import java.util.List;

/**
 * Created by Administrator on 2017/4/5.
 */

public interface CityManageContract {
    interface View extends BaseView<Presenter> {
        void showError(String error);

        void addFail(String message);

        void addSuccess(String message);

        void notifyCitysChanged(List citys);
    }

    interface Presenter extends BasePresenter {
        List getCityList();

        boolean hasCityExist(String cityId);

        void addAirWeather(String cityId);
    }
}
