package com.dragon.alphaweather.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.dragon.alphaweather.R;
import com.dragon.alphaweather.adapter.CityManageAdapter;
import com.dragon.alphaweather.entity.AirWeather;
import com.dragon.alphaweather.entity.CityAqi;
import com.dragon.alphaweather.utils.CacheUtil;
import com.dragon.alphaweather.utils.Constant;
import com.dragon.alphaweather.utils.LogUtil;
import com.dragon.alphaweather.utils.OkHttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/18.
 */

public class CityManageActivity extends AppCompatActivity {
    @BindView(R.id.city_manage_toolbar)
    Toolbar toolbar;
    @BindView(R.id.city_manage_recycler_view)
    RecyclerView recyclerView;
    @BindString(R.string.menu_city_manage)
    String cityManageTitle;
    @BindView(R.id.city_manage_colayout)
    CoordinatorLayout colayout;
    static String TAG = "CityManageActivity";
    private List<CityAqi> cityAqis;
    private CityManageAdapter cmAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manage);
        ButterKnife.bind(this);
        initToolbar();
        showCitys();
    }

    private void initToolbar() {
        toolbar.setTitle(cityManageTitle);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static void activityStart(Context context) {
        Intent intent = new Intent(context, CityManageActivity.class);
        context.startActivity(intent);
    }

    public void showCitys() {
        if (getCityList() == null) {
            return;
        }
        cityAqis = getCityList();
        cmAdapter = new CityManageAdapter(this, cityAqis, colayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cmAdapter);
    }

    //从缓存中取已选城市数据
    private List<CityAqi> getCityList() {
        String citys = CacheUtil.getACache().getAsString("citys");
        if (citys == null && TextUtils.isEmpty(citys)) {
//            Toast.makeText(this, "数据加载异常", Toast.LENGTH_SHORT).show();
            return new ArrayList<CityAqi>();
        }
        List<CityAqi> cqList = JSON.parseArray(citys, CityAqi.class);
        return cqList;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String id = data.getExtras().getString("id");
            String name = data.getExtras().getString("name");
            final List<CityAqi> cityAqiList = getCityList();
            if (cityAqiList != null && cityAqiList.size() > 0) {
                //遍历缓存中的城市列表，判断刚选择的城市是否存在,如果存在不做任何更改,如果不存在联网获取数据并写入缓存
                for (CityAqi ca : cityAqiList) {
                    if (id.equals(ca.getId())) {
                        return;
                    }
                }
            }
            Request request = new Request.Builder().get()//
                    .url(Constant.GET_CITY_AIR_WEATHER_URL + id).build();
            Call call = OkHttpUtil.getOkHttpClient().newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LogUtil.e(TAG, e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CityManageActivity.this, "联网获取数据失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    AirWeather aw = JSON.parseObject(result, AirWeather.class);
                    String aqi, cid, city, lat, lon;
                    try {
                        aqi = aw.getHeWeather5().get(0).getAqi().getCity().getAqi();
                        cid = aw.getHeWeather5().get(0).getBasic().getId();
                        city = aw.getHeWeather5().get(0).getBasic().getCity();
                        lat = aw.getHeWeather5().get(0).getBasic().getLat();
                        lon = aw.getHeWeather5().get(0).getBasic().getLon();
                    } catch (Exception e) {
                        LogUtil.e(TAG, e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Toast.makeText(CityManageActivity.this, "添加城市失败，请重新尝试", Toast.LENGTH_SHORT).show();
//                                final Snackbar sb = Snackbar.make(colayout, "添加失败", Snackbar.LENGTH_SHORT);
//                                sb.setActionTextColor(getResources().getColor(android.R.color.white))//
//                                        .setAction("知道了", new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                sb.dismiss();
//                                            }
//                                        }).show();
                                Snackbar.make(colayout, "添加失败", Snackbar.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar.make(colayout, "添加成功", Snackbar.LENGTH_SHORT).show();
                        }
                    });
//                    LogUtil.e(TAG, "==========================");
//                    LogUtil.e(TAG, aqi + "---" + id + "---" + city + "---" + lat + "---" + lon);
                    CityAqi cityAqi = new CityAqi(cid, city, lat, lon, Integer.parseInt(aqi));
                    cityAqiList.add(cityAqi);
                    CacheUtil.getACache().remove("citys");
                    String json = JSON.toJSON(cityAqiList).toString();
                    CacheUtil.getACache().put("citys", json);
                    LogUtil.e(TAG, "new citys:" + json);
                    cityAqis = cityAqiList;
                    LogUtil.e(TAG, "citys size:" + cityAqis.size());
                    //把获取的数据持久化
                    CacheUtil.getACache().put(cid, result);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (cmAdapter != null) {
                                cmAdapter.setList(cityAqis);
                                cmAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            });
        }

    }

    @OnClick(R.id.city_manage_fab)
    public void selectCity() {
        Intent intent = new Intent(CityManageActivity.this, SelectCityActivity.class);
        startActivityForResult(intent, 0);
    }

}
