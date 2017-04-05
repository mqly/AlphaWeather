package com.dragon.alphaweather.citymanage;

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
import android.view.View;
import android.widget.Toast;

import com.dragon.alphaweather.R;
import com.dragon.alphaweather.entity.CityAqi;
import com.dragon.alphaweather.selectcity.SelectCityActivity;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/18.
 */

public class CityManageActivity extends AppCompatActivity implements CityManageContract.View {
    @BindView(R.id.city_manage_toolbar)
    Toolbar toolbar;
    @BindView(R.id.city_manage_recycler_view)
    RecyclerView recyclerView;
    @BindString(R.string.menu_city_manage)
    String cityManageTitle;
    @BindView(R.id.city_manage_colayout)
    CoordinatorLayout colayout;
    private List<CityAqi> cityAqis;
    private CityManageAdapter cmAdapter;
    private CityManageContract.Presenter cityManagePresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manage);
        ButterKnife.bind(this);
        initToolbar();
        setPresenter(new CityManagePresenter(this));
        cityAqis = cityManagePresenter.getCityList();
        cmAdapter = new CityManageAdapter(this, cityAqis, colayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cmAdapter);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String id = data.getExtras().getString("id");
            String name = data.getExtras().getString("name");
            if (cityManagePresenter.hasCityExist(id)) {
                return;
            }
            cityManagePresenter.addAirWeather(id);
        }

    }

    @OnClick(R.id.city_manage_fab)
    public void selectCity() {
        Intent intent = new Intent(CityManageActivity.this, SelectCityActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public void setPresenter(CityManageContract.Presenter presenter) {
        cityManagePresenter = presenter;
    }

    @Override
    public void showError(final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CityManageActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void addFail(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(colayout, message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void addSuccess(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(colayout, message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void notifyCitysChanged(List citys) {
        cityAqis = citys;
        cmAdapter.setList(cityAqis);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (cmAdapter != null) {
                    cmAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
