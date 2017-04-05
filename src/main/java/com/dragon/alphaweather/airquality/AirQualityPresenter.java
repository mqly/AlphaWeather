package com.dragon.alphaweather.airquality;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.dragon.alphaweather.entity.CityAqi;

import java.util.List;

/**
 * Created by Administrator on 2017/4/5.
 */

public class AirQualityPresenter implements AirQualityContract.Presenter {
    private AirQualityContract.View airQualityView;
    private AirQualityInteractor airQualityInteractor;

    public AirQualityPresenter(AirQualityContract.View airQualityView) {
        this.airQualityView = airQualityView;
        airQualityInteractor = new AirQualityInteractorImpl();
    }

    @Override
    public void start() {

    }

    //标记地图AQI
    public void markAirQuality() {
        //从缓存中取已选择城市数据
        String citys = airQualityInteractor.getCitysString();
        if (citys == null && TextUtils.isEmpty(citys)) {
            airQualityView.showError("暂无数据，请先添加城市");
            return;
        }
        //        LogUtil.e(TAG, "cache citys :" + citys);
        List<CityAqi> cqList = JSON.parseArray(citys, CityAqi.class);
        for (int i = 0; i < cqList.size(); i++) {
            airQualityView.markOnMap(cqList.get(i));
        }

    }


}
