package com.dragon.alphaweather.selectcity;

import com.dragon.alphaweather.BasePresenter;
import com.dragon.alphaweather.BaseView;
import com.dragon.alphaweather.ui.sortListView.SortModel;

import java.util.List;

/**
 * Created by Administrator on 2017/4/5.
 */

public interface SelectCityContract {
    interface View extends BaseView<Presenter> {
        void showProgressDialog();

        void closeProgressDialog();

        void showCityDatas(List<SortModel> sourceDataList);

        void showError(String error);

    }

    interface Presenter extends BasePresenter {
        List getAllCityList(String jsonString);
    }
}
