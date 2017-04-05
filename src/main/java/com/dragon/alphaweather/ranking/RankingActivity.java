package com.dragon.alphaweather.ranking;

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
import android.view.View;
import android.widget.Toast;

import com.dragon.alphaweather.R;
import com.dragon.alphaweather.entity.RankingAirQuality;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/18.
 */

public class RankingActivity extends AppCompatActivity implements RankingContract.View {
    @BindView(R.id.ranking_toolbar)
    Toolbar toolbar;
    @BindView(R.id.ranking_recycler_view)
    RecyclerView recyclerView;
    @BindString(R.string.menu_ranking)
    String rankingTitle;
    RankingAdapter rankingAdapter;
    static String TAG = "RankingActivity";
    List<RankingAirQuality> raqList = new ArrayList<RankingAirQuality>();
    private ProgressDialog progressDialog;
    private RankingContract.Presenter rankingPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        ButterKnife.bind(this);
        initToolBar();
        setPresenter(new RankingPresenter(this));
        rankingPresenter.start();
        raqList = rankingPresenter.getRankingList();
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

    //显示加载进度框
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    //关闭加载进度框
    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showError(String error) {
        Toast.makeText(RankingActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
    }

    //异步联网加载完成后更新列表
    @Override
    public void notifiDataChanged(List rankingList) {
        raqList = rankingList;
        rankingAdapter.setRankingList(raqList);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (rankingAdapter != null) {
                    rankingAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public void setPresenter(RankingContract.Presenter presenter) {
        this.rankingPresenter = presenter;
    }
}
