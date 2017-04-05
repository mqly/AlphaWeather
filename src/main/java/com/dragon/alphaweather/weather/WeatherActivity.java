package com.dragon.alphaweather.weather;

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
import android.view.MenuItem;

import com.dragon.alphaweather.R;
import com.dragon.alphaweather.about.AboutActivity;
import com.dragon.alphaweather.citymanage.CityManageActivity;
import com.dragon.alphaweather.airquality.AirQualityActivity;
import com.dragon.alphaweather.ranking.RankingActivity;
import com.viewpagerindicator.CirclePageIndicator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherActivity extends AppCompatActivity
        implements WeatherContract.View, NavigationView.OnNavigationItemSelectedListener {
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
    CityWeatherAdapter myFragmentAdapter;
    private WeatherContract.Presenter weatherPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolbar();
        initDrawer();
        setPresenter(new WeatherPresenter(this));
    }

    //初始化侧滑
    private void initDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
//        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
    }

    //如果没有已选城市弹出对话框提示先去添加城市
    public void showSelectCityDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WeatherActivity.this);
        builder.setMessage("请先选择城市");
        builder.setTitle("没有已选城市");
        builder.setPositiveButton("去选择", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                CityManageActivity.activityStart(WeatherActivity.this);
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
        if (myFragmentAdapter != null) {
            myFragmentAdapter.notifyDataSetChanged();
        }
        weatherPresenter.loadCitys();
        myFragmentAdapter = new CityWeatherAdapter(getSupportFragmentManager(), weatherPresenter.getCityWeather());
        viewPager.setAdapter(myFragmentAdapter);
        indicator.setViewPager(viewPager);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_about:
                AboutActivity.activityStart(WeatherActivity.this);
                break;
            case R.id.nav_air_map:
                AirQualityActivity.activityStart(WeatherActivity.this);
                break;
            case R.id.nav_city_manage:
                CityManageActivity.activityStart(WeatherActivity.this);
                break;
            case R.id.nav_phb:
                RankingActivity.activityStart(WeatherActivity.this);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setPresenter(WeatherContract.Presenter presenter) {
        weatherPresenter = presenter;
    }
}
