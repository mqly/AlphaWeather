package com.dragon.alphaweather.ranking;

/**
 * Created by Administrator on 2017/4/5.
 */

public interface RankingInteractor {
    String getRankingListFromCache();

    void getRankingListFromNet(OnLoadListener listener);

    void saveRankingAirQuality(String str);
}
