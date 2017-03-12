package com.dragon.alphaweather.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dragon.alphaweather.R;
import com.dragon.alphaweather.adapter.RankingAdapter;
import com.dragon.alphaweather.cache.ACache;
import com.dragon.alphaweather.entity.RankingAirQuality;
import com.dragon.alphaweather.utils.CacheUtil;
import com.dragon.alphaweather.utils.Constant;
import com.dragon.alphaweather.utils.LogUtil;
import com.dragon.alphaweather.utils.OkHttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/18.
 */

public class RankingActivity extends AppCompatActivity {
    @BindView(R.id.ranking_toolbar)
    Toolbar toolbar;
    @BindView(R.id.ranking_recycler_view)
    RecyclerView recyclerView;
    @BindString(R.string.menu_ranking)
    String rankingTitle;
    RankingAdapter rankingAdapter;
    static String TAG = "RankingActivity";
    static List<RankingAirQuality> raqList = new ArrayList<RankingAirQuality>();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        ButterKnife.bind(this);
        initToolBar();
        showProgressDialog();
        setRankingList();
        rankingAdapter = new RankingAdapter(this, raqList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(rankingAdapter);
    }

    private void initToolBar() {
        toolbar.setTitle(rankingTitle);
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
        Intent intent = new Intent(context, RankingActivity.class);
        context.startActivity(intent);
    }

    //获取空气质量排行榜数据并加入list中,排行榜数据近取前10
    public void setRankingList() {
        //缓存中如果有数据直接拿缓存数据，缓存中没有则联网获取
        String jsonString = CacheUtil.getACache().getAsString("banking");
        if (jsonString != null && !TextUtils.isEmpty(jsonString)) {
            LogUtil.e(TAG, "data from cache");
            RankingTop10(jsonString);
            closeProgressDialog();
            return;
        }
        OkHttpClient client = OkHttpUtil.getOkHttpClient();
        Request request = new Request.Builder().get().url(Constant.AIR_RANKING_URL).build();
        final Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.e(TAG, e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(RankingActivity.this, "网络异常，数据加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtil.e(TAG, result);
                getRankingAirQuality(result);
                closeProgressDialog();
            }
        });
    }

    //json转成domain实体
    public void getRankingAirQuality(String result) {
        JSONObject obj = JSON.parseObject(result).getJSONObject("showapi_res_body");
        LogUtil.e(TAG, obj.toString());
        JSONArray array = obj.getJSONArray("list");
        LogUtil.e(TAG, array.toString());
        CacheUtil.getACache().put("banking", array.toString(), ACache.TIME_DAY);
        RankingTop10(array.toString());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rankingAdapter.notifyDataSetChanged();
            }
        });

        LogUtil.e(TAG, "best list size:" + raqList.size());
    }

    private void RankingTop10(String array) {
        List<RankingAirQuality> allList = JSON.parseArray(array, RankingAirQuality.class);
        if (raqList != null && raqList.size() > 0) {
            raqList.clear();
        }
        for (int i = 0; i < 10; i++) {
            raqList.add(allList.get(i));
        }
    }

    //显示加载进度框
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    //关闭加载进度框
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

}
