package com.dragon.alphaweather.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.dragon.alphaweather.R;
import com.dragon.alphaweather.entity.AirQuality;
import com.dragon.alphaweather.entity.CityAqi;
import com.dragon.alphaweather.utils.CacheUtil;
import com.dragon.alphaweather.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/18.
 */


public class AirQualityActivity extends AppCompatActivity {
    @BindView(R.id.air_quality_toolbar)
    Toolbar toolbar;
    @BindString(R.string.menu_air_map)
    String airQualityTitle;
    static List<AirQuality> aqList = new ArrayList<AirQuality>();
    MapView mMapView = null;
    AMap aMap;
    static String TAG = "AirQualityActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_quality);
        ButterKnife.bind(this);
        initToolBar();
        initMap(savedInstanceState);
//        CityAqi aqi = new CityAqi("CN101010100", "北京", "39.904983", "116.427287", 23);
//        List<CityAqi> aqiList = new ArrayList<CityAqi>();
//        aqiList.add(aqi);
//        String test = JSON.toJSON(aqiList).toString();
//        CacheUtil.getACache().put("citys", test);
//        LogUtil.e(TAG, "test" + test);
        markAirQuality();
    }

    private void initToolBar() {
        toolbar.setTitle(airQualityTitle);
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

    private void initMap(@Nullable Bundle savedInstanceState) {
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        //初始化地图控制器对象

        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        LatLng center = new LatLng(39.90403, 116.407525);
        //设置中心点和缩放比例
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(center));
        aMap.moveCamera(CameraUpdateFactory.zoomTo(5));
    }

    public static void activityStart(Context context) {
        Intent intent = new Intent(context, AirQualityActivity.class);
        context.startActivity(intent);
    }

    //标记地图AQI
    public void markAirQuality() {
        //从缓存中取已选择城市数据
        String citys = CacheUtil.getACache().getAsString("citys");
        if (citys == null && TextUtils.isEmpty(citys)) {
            Toast.makeText(this, "数据加载异常", Toast.LENGTH_SHORT).show();
            return;
        }
        LogUtil.e(TAG, "cache citys :" + citys);
        List<CityAqi> cqList = JSON.parseArray(citys, CityAqi.class);
        for (int i = 0; i < cqList.size(); i++) {
            markOnMap(cqList.get(i));
        }

    }

    //自定义地图标记
    public void markOnMap(CityAqi ca) {
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(new LatLng(Double.parseDouble(ca.getLat()), Double.parseDouble(ca.getLon())));
        markerOption.title(ca.getName()).snippet("AQI:" + ca.getAqi());
        if (ca.getAqi() <= 30) {
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(), R.drawable.nice)));
        } else if (ca.getAqi() > 30 && ca.getAqi() <= 60) {
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(), R.drawable.mid)));
        } else if (ca.getAqi() > 60) {
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(), R.drawable.bad)));
        }

        final Marker marker = aMap.addMarker(markerOption);
    }

    //图片文字合成一张图
    public Bitmap getMyBitmap(String pm_val) {
        Bitmap bitmap = BitmapDescriptorFactory.fromResource(
                R.drawable.mid).getBitmap();
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(59f);
        textPaint.setColor(getResources().getColor(R.color.colorPrimaryDark));
        // 设置bitmap上面的文字位置
        canvas.drawText(pm_val, 30, 45, textPaint);
        return mutableBitmap;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }
}
