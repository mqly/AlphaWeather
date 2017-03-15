package com.dragon.alphaweather.adapter;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dragon.alphaweather.R;
import com.dragon.alphaweather.entity.CityAqi;
import com.dragon.alphaweather.utils.CacheUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/19.
 */

public class CityManageAdapter extends RecyclerView.Adapter<CityManageAdapter.MyViewHolder> {
    private List<CityAqi> citys;
    private Context context;
    private CoordinatorLayout colayout;

    public CityManageAdapter(Context context, List<CityAqi> citys, CoordinatorLayout colayout) {
        this.citys = citys;
        this.context = context;
        this.colayout = colayout;
    }

    public void setList(List<CityAqi> citys) {
        this.citys = citys;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_city_manage, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final CityAqi cityAqi = citys.get(position);
        holder.nameTextView.setText(cityAqi.getName());
        int aqi = cityAqi.getAqi();
        holder.aqiTextView.setText(aqi + "");
        if (aqi > 30 & aqi <= 60) {
            holder.aqiTextView.setBackgroundColor(context.getResources().getColor(R.color.colorAirMid));
        } else if (aqi > 60) {
            holder.aqiTextView.setBackgroundColor(context.getResources().getColor(R.color.colorAirBad));
        }

        holder.delImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    citys.remove(position);
                    notifyDataSetChanged();
                    CacheUtil.getACache().remove("citys");
                    String json = JSON.toJSON(citys).toString();
                    CacheUtil.getACache().put("citys", json);
                    CacheUtil.getACache().remove(cityAqi.getId());
                } catch (Exception e) {
                    Snackbar.make(colayout, "删除失败", Snackbar.LENGTH_SHORT).show();
                }
                Snackbar.make(colayout, "删除成功", Snackbar.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return citys.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_city_manage_name)
        public TextView nameTextView;
        @BindView(R.id.item_city_manage_aqi)
        public TextView aqiTextView;
        @BindView(R.id.item_city_manage_del)
        public ImageView delImageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
