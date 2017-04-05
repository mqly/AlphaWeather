package com.dragon.alphaweather.citymanage;

import com.dragon.alphaweather.utils.CacheUtil;
import com.dragon.alphaweather.utils.Constant;
import com.dragon.alphaweather.utils.OkHttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/5.
 */

public class CityManageInteractorImpl implements CityManageInteractor {
    @Override
    public String getCitysString() {
        return CacheUtil.getACache().getAsString("citys");
    }

    //联网获取城市天气信息
    @Override
    public void getAirWeatherFromNet(String cityId, final OnLoadListener listener) {
        Request request = new Request.Builder().get()//
                .url(Constant.GET_CITY_AIR_WEATHER_URL + cityId).build();
        Call call = OkHttpUtil.getOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (listener != null) {
                    listener.onLoadFailure(e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (listener != null) {
                    listener.onLoadSuccess(response.body().string());
                }
            }
        });
    }

    @Override
    public void removeCitys() {
        CacheUtil.getACache().remove("citys");
    }

    @Override
    public void putAllCitys(String citys) {
        CacheUtil.getACache().put("citys", citys);
    }

    @Override
    public void putCity(String cityId, String cityString) {
        CacheUtil.getACache().put(cityId, cityString);
    }
}
