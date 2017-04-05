package com.dragon.alphaweather.ranking;

import com.dragon.alphaweather.BasePresenter;
import com.dragon.alphaweather.BaseView;

import java.util.List;

/**
 * Created by Administrator on 2017/4/5.
 */

public interface RankingContract {
    interface View extends BaseView<Presenter> {

        void showProgressDialog();

        void closeProgressDialog();

        void showError(String error);

        void notifiDataChanged(List rankingList);
    }

    interface Presenter extends BasePresenter {

        List getRankingList();

        List getRankingAirQuality(String result);

        List RankingTop10(String array);
    }
}
