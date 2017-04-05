package com.dragon.alphaweather.selectcity;

import com.dragon.alphaweather.application.MyApplication;
import com.dragon.alphaweather.entity.City;
import com.dragon.alphaweather.greendao.CityDao;
import com.dragon.alphaweather.utils.Constant;
import com.dragon.alphaweather.utils.OkHttpUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/5.
 */

public class SelectCityInteractorImpl implements SelectCityInteractor {
    //从数据库获取城市
    @Override
    public List getCityDatasFromDB() {
        CityDao dao = MyApplication.getInstance().getDaoSession().getCityDao();
        List<City> citys = dao.queryBuilder().build().list();
        return citys;
    }

    //城市数据存入数据库
    @Override
    public void saveToDB(List citys) {
        CityDao dao = MyApplication.getInstance().getDaoSession().getCityDao();
        dao.insertInTx(citys);
    }

    @Override
    public void getCityDatasFromNet(final OnLoadListener listener) {
        Request request = new Request.Builder().get().url(Constant.ALL_CITY_URL).build();
        Call call = OkHttpUtil.getOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onLoadFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                listener.onLoadSuccess(response.body().string());
            }
        });
    }
}
