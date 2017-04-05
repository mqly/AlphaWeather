package com.dragon.alphaweather.ranking;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dragon.alphaweather.entity.RankingAirQuality;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/5.
 */

public class RankingPresenter implements RankingContract.Presenter {
    private RankingContract.View rankingView;
    private RankingInteractor rankingInteractor;

    public RankingPresenter(RankingContract.View view) {
        rankingView = view;
        rankingInteractor = new RankingInteractorImpl();
    }

    @Override
    public void start() {
        rankingView.showProgressDialog();
    }

    //获取排行榜列表
    @Override
    public List getRankingList() {
        final List[] rankingListTopTen = {new ArrayList()};
        //缓存中如果有数据直接拿缓存数据，缓存中没有则联网获取
        String jsonString = rankingInteractor.getRankingListFromCache();
        if (jsonString != null && !TextUtils.isEmpty(jsonString)) {
            //            LogUtil.e(TAG, "data from cache");
            rankingListTopTen[0] = RankingTop10(jsonString);
            rankingView.closeProgressDialog();
            return rankingListTopTen[0];
        }
        rankingInteractor.getRankingListFromNet(new OnLoadListener() {
            @Override
            public void onLoadSuccess(String str) {
                rankingView.closeProgressDialog();
                rankingListTopTen[0] = getRankingAirQuality(str);
                rankingView.notifiDataChanged(rankingListTopTen[0]);
            }

            @Override
            public void onLoadFailure(Exception e) {
                rankingView.showError(e.getMessage());
            }
        });
        return rankingListTopTen[0];
    }

    //返回排行榜前10
    public List RankingTop10(String array) {
        List<RankingAirQuality> allList = JSON.parseArray(array, RankingAirQuality.class);
        List<RankingAirQuality> topTenList = new ArrayList<RankingAirQuality>();
        for (int i = 0; i < 10; i++) {
            topTenList.add(allList.get(i));
        }
        return topTenList;
    }


    //json转成domain实体
    public List getRankingAirQuality(String result) {
        JSONObject obj = JSON.parseObject(result).getJSONObject("showapi_res_body");
        //        LogUtil.e(TAG, obj.toString());
        JSONArray array = obj.getJSONArray("list");
        //        LogUtil.e(TAG, array.toString());
        rankingInteractor.saveRankingAirQuality(array.toString());
        return RankingTop10(array.toString());
    }
}
