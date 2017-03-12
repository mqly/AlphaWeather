package com.dragon.alphaweather.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;

import com.alibaba.fastjson.JSON;
import com.dragon.alphaweather.R;
import com.dragon.alphaweather.adapter.CityWeatherAdapter;
import com.dragon.alphaweather.entity.AirWeather;
import com.dragon.alphaweather.entity.CityAqi;
import com.dragon.alphaweather.fragment.CityWeatherFragment;
import com.dragon.alphaweather.utils.CacheUtil;
import com.dragon.alphaweather.utils.LogUtil;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.content_main_viewpager)
    ViewPager viewPager;
    @BindView(R.id.content_main_indicator)
    CirclePageIndicator indicator;
    private List<CityAqi> cityAqis;
    CityWeatherAdapter myFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    //加载城市空气质量和天气信息
    private void loadCityWeather() {
        List<CityWeatherFragment> fragmentList = new ArrayList<CityWeatherFragment>();
        for (int i = 0; i < cityAqis.size(); i++) {
            String cityInfo = CacheUtil.getACache().getAsString(cityAqis.get(i).getId());
            LogUtil.e("main", "loadcityweather cityinfo" + cityInfo);
            AirWeather aw = JSON.parseObject(cityInfo, AirWeather.class);
//            LogUtil.e("main","loadcityweather awID"+aw.getHeWeather5().get(0).getBasic().getId());
//            LogUtil.e("main","loadcityweather awCITY"+aw.getHeWeather5().get(0).getBasic().getCity());
            CityWeatherFragment fragment = CityWeatherFragment.newInstance(aw);
            fragmentList.add(fragment);
        }
        myFragmentAdapter = new CityWeatherAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(myFragmentAdapter);
        indicator.setViewPager(viewPager);
    }

    //如果没有已选城市弹出对话框提示先去添加城市
    private void showSelectCityDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("请先选择城市");
        builder.setTitle("没有已选城市");
        builder.setPositiveButton("去选择", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                CityManageActivity.activityStart(MainActivity.this);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
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
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cityAqis = getCityList();
        LogUtil.e("main", "resume:" + cityAqis.size());
        for (int i = 0; i < cityAqis.size(); i++) {
            LogUtil.e("main", cityAqis.get(i).getName() + cityAqis.get(i).getId());
        }
        if (myFragmentAdapter != null) {
            myFragmentAdapter.notifyDataSetChanged();
        }

        if (cityAqis.size() <= 0) {
            showSelectCityDialog();
            return;
        }
        loadCityWeather();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_about:
//                Toast.makeText(this, "关于应用", Toast.LENGTH_SHORT).show();
                AboutActivity.activityStart(MainActivity.this);
                break;
            case R.id.nav_air_map:
//                Toast.makeText(this, "空气质量图", Toast.LENGTH_SHORT).show();
                AirQualityActivity.activityStart(MainActivity.this);
                break;
            case R.id.nav_city_manage:
//                Toast.makeText(this, "城市管理", Toast.LENGTH_SHORT).show();
                CityManageActivity.activityStart(MainActivity.this);
                break;
            case R.id.nav_phb:
//                Toast.makeText(this, "排行榜", Toast.LENGTH_SHORT).show();
                RankingActivity.activityStart(MainActivity.this);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
