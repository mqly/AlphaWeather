package com.dragon.alphaweather.weather;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/2/21.
 */

public class CityWeatherAdapter extends FragmentStatePagerAdapter {
    private List<CityWeatherFragment> fragmentList;
    private FragmentManager mFragmentManager;

    public CityWeatherAdapter(FragmentManager fm) {
        super(fm);
        this.mFragmentManager = fm;
    }

    public CityWeatherAdapter(FragmentManager fm, List<CityWeatherFragment> fragmentList) {
        super(fm);
        this.mFragmentManager = fm;
        this.fragmentList = fragmentList;
//        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
