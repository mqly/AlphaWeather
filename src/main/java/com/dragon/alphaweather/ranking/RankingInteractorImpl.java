package com.dragon.alphaweather.ranking;

import com.dragon.alphaweather.cache.ACache;
import com.dragon.alphaweather.utils.CacheUtil;
import com.dragon.alphaweather.utils.Constant;
import com.dragon.alphaweather.utils.OkHttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/5.
 */

public class RankingInteractorImpl implements RankingInteractor {
    @Override
    public String getRankingListFromCache() {
        return CacheUtil.getACache().getAsString("banking");
    }

    @Override
    public void getRankingListFromNet(final OnLoadListener listener) {
        OkHttpClient client = OkHttpUtil.getOkHttpClient();
        Request request = new Request.Builder().get().url(Constant.AIR_RANKING_URL).build();
        final Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (listener != null) {
                    listener.onLoadFailure(e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (listener != null) {
                    listener.onLoadSuccess(result);
                }
            }
        });
    }

    @Override
    public void saveRankingAirQuality(String str) {
        CacheUtil.getACache().put("banking", str, ACache.TIME_DAY);
    }
}
