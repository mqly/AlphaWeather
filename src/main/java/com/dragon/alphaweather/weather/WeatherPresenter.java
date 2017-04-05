package com.dragon.alphaweather.weather;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.dragon.alphaweather.entity.AirWeather;
import com.dragon.alphaweather.entity.CityAqi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/4.
 */

public class WeatherPresenter implements WeatherContract.Presenter {
    private WeatherContract.View weatherView;
    private WeatherInteractor weatherInteractor;

    public WeatherPresenter(WeatherContract.View weatherView) {
        this.weatherView = weatherView;
        this.weatherInteractor = new WeatherInteractorImpl();
    }

    @Override
    public void start() {

    }

    //加载城市空气质量和天气信息
    @Override
    public List getCityWeather() {
        List<CityAqi> cityAqis = getCityList();
        List<CityWeatherFragment> fragmentList = new ArrayList<CityWeatherFragment>();
        for (int i = 0; i < cityAqis.size(); i++) {
            String cityInfo = weatherInteractor.getCityInfo(cityAqis.get(i).getId());
            AirWeather aw = JSON.parseObject(cityInfo, AirWeather.class);
            CityWeatherFragment fragment = CityWeatherFragment.newInstance(aw);
            fragmentList.add(fragment);
        }
        return fragmentList;
    }

    //从缓存中取已选城市数据
    public List<CityAqi> getCityList() {
        String citys = weatherInteractor.getCitysString();
        if (citys == null && TextUtils.isEmpty(citys)) {
            return new ArrayList<CityAqi>();
        }
        List<CityAqi> cqList = JSON.parseArray(citys, CityAqi.class);
        return cqList;
    }

    //判断缓存中是否有已添加的城市
    public boolean hasCitys() {
        if (getCityList().size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void loadCitys() {
        if (!hasCitys()) {
            weatherView.showSelectCityDialog();
        }
    }
}
