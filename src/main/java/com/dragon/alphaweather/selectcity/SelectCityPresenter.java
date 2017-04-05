package com.dragon.alphaweather.selectcity;

import com.alibaba.fastjson.JSON;
import com.dragon.alphaweather.entity.City;
import com.dragon.alphaweather.ui.sortListView.SortModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/5.
 */

public class SelectCityPresenter implements SelectCityContract.Presenter {
    private SelectCityContract.View selectCityView;
    private SelectCityInteractor selectCityInteractor;

    public SelectCityPresenter(SelectCityContract.View selectCityView) {
        this.selectCityView = selectCityView;
        selectCityInteractor = new SelectCityInteractorImpl();
    }

    @Override
    public void start() {
        //从数据库加载所有城市列表
        List<City> citys = selectCityInteractor.getCityDatasFromDB();
        if (citys.size() > 0) {
            selectCityView.showCityDatas(initSortListViewData(citys));
        } else {
            selectCityView.showProgressDialog();
            selectCityInteractor.getCityDatasFromNet(new OnLoadListener() {
                @Override
                public void onLoadSuccess(String str) {
                    List<City> cityDatas = getAllCityList(str);
                    selectCityInteractor.saveToDB(cityDatas);
                    selectCityView.closeProgressDialog();
                    selectCityView.showCityDatas(initSortListViewData(cityDatas));
                }

                @Override
                public void onLoadFailure(Exception e) {
                    selectCityView.showError("加载城市列表失败");
                }
            });
        }
    }

    //根据城市列表数据转换字母排序的数据列表
    public List<SortModel> initSortListViewData(List<City> cityDatas) {
        List<SortModel> sourceDataList = new ArrayList<SortModel>();
        for (int i = 0; i < cityDatas.size(); i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(cityDatas.get(i).getCityZh());
            sortModel.setId(cityDatas.get(i).getId());
            //汉字转换成拼音
            String pinyin = cityDatas.get(i).getCityEn();
            String sortString = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            sourceDataList.add(sortModel);
        }
        return sourceDataList;
    }

    //通过jsonarray解析City List
    public List<City> getAllCityList(String jsonString) {
        List<City> citys = new ArrayList<City>();
        citys = JSON.parseArray(jsonString, City.class);
        return citys;
    }
}
