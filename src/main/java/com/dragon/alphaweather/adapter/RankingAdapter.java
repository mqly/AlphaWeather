package com.dragon.alphaweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dragon.alphaweather.R;
import com.dragon.alphaweather.entity.RankingAirQuality;
import com.dragon.alphaweather.utils.LogUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/19.
 */

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.MyViewHolder> {
    private List<RankingAirQuality> airs;
    private Context context;

    public RankingAdapter(Context context, List<RankingAirQuality> airs) {
        this.airs = airs;
        this.context = context;
        LogUtil.e("RankingActivity", "adapter size:" + airs.size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_airquality_ranking, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RankingAirQuality raq = airs.get(position);
        holder.areaTextView.setText(raq.getArea());
        holder.aqiTextView.setText(raq.getAqi() + "");
    }

    @Override
    public int getItemCount() {
        return airs.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_airquality_ranking_area)
        public TextView areaTextView;
        @BindView(R.id.item_airquality_ranking_aqi)
        public TextView aqiTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
