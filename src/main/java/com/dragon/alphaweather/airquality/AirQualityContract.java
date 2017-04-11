package com.dragon.alphaweather.airquality;

import com.dragon.alphaweather.BasePresenter;
import com.dragon.alphaweather.BaseView;
import com.dragon.alphaweather.entity.CityAqi;

/**
 * Created by Administrator on 2017/4/5.
 */

public interface AirQualityContract {
    interface View extends BaseView<Presenter> {
        void markOnMap(CityAqi ca);

        void showError(String error);
    }

    interface Presenter extends BasePresenter {
        void markAirQuality();

    }
}
