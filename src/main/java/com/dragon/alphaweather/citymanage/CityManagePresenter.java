package com.dragon.alphaweather.citymanage;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.dragon.alphaweather.entity.AirWeather;
import com.dragon.alphaweather.entity.CityAqi;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/5.
 */

public class CityManagePresenter implements CityManageContract.Presenter {
    private CityManageContract.View cityManageView;
    private CityManageInteractor cityManageInteractor;

    public CityManagePresenter(CityManageContract.View cityManageView) {
        this.cityManageView = cityManageView;
        cityManageInteractor = new CityManageInteractorImpl();
    }

    @Override
    public void start() {

    }

    //从缓存中取已选城市数据
    @Override
    public List getCityList() {
        String citys = cityManageInteractor.getCitysString();
        if (citys == null && TextUtils.isEmpty(citys)) {
            //            Toast.makeText(this, "数据加载异常", Toast.LENGTH_SHORT).show();
            return new ArrayList<CityAqi>();
        }
        List<CityAqi> cqList = JSON.parseArray(citys, CityAqi.class);
        return cqList;
    }

    //判断新选择的城市是否已添加
    @Override
    public boolean hasCityExist(String cityId) {
        List<CityAqi> cityAqiList = getCityList();
        if (cityAqiList != null && cityAqiList.size() > 0) {
            //遍历缓存中的城市列表，判断刚选择的城市是否存在,如果存在不做任何更改,如果不存在联网获取数据并写入缓存
            for (CityAqi ca : cityAqiList) {
                if (cityId.equals(ca.getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    //添加新选择的城市到已添加列表
    @Override
    public void addAirWeather(String cityId) {
        cityManageInteractor.getAirWeatherFromNet(cityId, new OnLoadListener() {
            @Override
            public void onLoadSuccess(String str) {
                Logger.d(str);
                AirWeather aw = JSON.parseObject(str, AirWeather.class);
                String aqi, cid, city, lat, lon;
                try {
                    aqi = aw.getHeWeather5().get(0).getAqi().getCity().getAqi();
                    cid = aw.getHeWeather5().get(0).getBasic().getId();
                    city = aw.getHeWeather5().get(0).getBasic().getCity();
                    lat = aw.getHeWeather5().get(0).getBasic().getLat();
                    lon = aw.getHeWeather5().get(0).getBasic().getLon();
                } catch (Exception e) {
                    cityManageView.addFail("添加失败");
                    return;
                }
                cityManageView.addSuccess("添加成功");
                CityAqi cityAqi = new CityAqi(cid, city, lat, lon, Integer.parseInt(aqi));
                List cityAqiList = getCityList();
                cityAqiList.add(cityAqi);
                cityManageInteractor.removeCitys();
                String json = JSON.toJSON(cityAqiList).toString();
                cityManageInteractor.putAllCitys(json);
                cityManageInteractor.putCity(cid, str);
                cityManageView.notifyCitysChanged(cityAqiList);
            }

            @Override
            public void onLoadFailure(Exception e) {
                cityManageView.showError("网络请求失败");
            }
        });
    }

}
