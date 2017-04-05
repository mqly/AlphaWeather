package com.dragon.alphaweather.weather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.dragon.alphaweather.R;
import com.dragon.alphaweather.entity.AirWeather;
import com.dragon.alphaweather.entity.CityAqi;
import com.dragon.alphaweather.utils.CacheUtil;
import com.dragon.alphaweather.utils.Constant;
import com.dragon.alphaweather.utils.LogUtil;
import com.dragon.alphaweather.utils.OkHttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/21.
 */

public class CityWeatherFragment extends Fragment {
    private AirWeather aw;
    @BindView(R.id.fragment_city_weather_name)
    TextView name;
    @BindView(R.id.fragment_city_weather_aqi)
    TextView aqi;
    @BindView(R.id.fragment_city_weather_maxtmp)
    TextView maxtmp;
    @BindView(R.id.fragment_city_weather_mintmp)
    TextView mintmp;
    @BindView(R.id.fragment_city_weather_tmp)
    TextView tmp;
    @BindView(R.id.fragment_city_weather_weather)
    TextView weather;
    @BindView(R.id.fragment_city_weather_co)
    TextView co;
    @BindView(R.id.fragment_city_weather_o3)
    TextView o3;
    @BindView(R.id.fragment_city_weather_no2)
    TextView no2;
    @BindView(R.id.fragment_city_weather_so2)
    TextView so2;
    @BindView(R.id.fragment_city_weather_pm2)
    TextView pm2;
    @BindView(R.id.fragment_city_weather_pm10)
    TextView pm10;
    @BindView(R.id.fragment_city_weather_ss)
    TextView ss;
    @BindView(R.id.fragment_city_weather_ssdesc)
    TextView ssdesc;
    @BindView(R.id.fragment_city_weather_cy)
    TextView cy;
    @BindView(R.id.fragment_city_weather_cydesc)
    TextView cydesc;
    @BindView(R.id.fragment_city_weather_gm)
    TextView gm;
    @BindView(R.id.fragment_city_weather_gmdesc)
    TextView gmdesc;
    @BindView(R.id.fragment_city_weather_cx)
    TextView cx;
    @BindView(R.id.fragment_city_weather_cxdesc)
    TextView cxdesc;
    @BindView(R.id.fragment_city_weather_srf)
    SwipeRefreshLayout swf;
    @BindView(R.id.fragment_city_weather_aqibg)
    ImageView aqibg;
    @BindView(R.id.fragment_city_weather_codw)
    TextView codw;
    @BindView(R.id.fragment_city_weather_o3dw)
    TextView o3dw;
    @BindView(R.id.fragment_city_weather_no2dw)
    TextView no2dw;
    @BindView(R.id.fragment_city_weather_so2dw)
    TextView so2dw;
    @BindView(R.id.fragment_city_weather_wmzl)
    TextView wmzl;
    @BindView(R.id.fragment_city_weather_year)
    TextView year;
    @BindView(R.id.frag_tod)
    TextView tod;
    @BindView(R.id.frag_tom)
    TextView tom;
    @BindView(R.id.frag_toh)
    TextView toh;


    public CityWeatherFragment() {

    }

    public static CityWeatherFragment newInstance(AirWeather aw) {
        Bundle args = new Bundle();
        args.putSerializable("aw", aw);
        CityWeatherFragment fragment = new CityWeatherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.aw = (AirWeather) args.getSerializable("aw");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_city_weather, null);
        ButterKnife.bind(this, view);
        setCityWeatherData(aw);
        final String id = aw.getHeWeather5().get(0).getBasic().getId();
        swf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Request request = new Request.Builder().get()//
                        .url(Constant.GET_CITY_AIR_WEATHER_URL + id).build();
                Call call = OkHttpUtil.getOkHttpClient().newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        LogUtil.e("citywweather", e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        LogUtil.e("citywweather", "swfrseponse");
                        String result = response.body().string();
                        final AirWeather aw = JSON.parseObject(result, AirWeather.class);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setCityWeatherData(aw);
                            }
                        });

                        String nid = aw.getHeWeather5().get(0).getBasic().getId();
                        String naqi = aw.getHeWeather5().get(0).getAqi().getCity().getAqi();
                        List<CityAqi> cas = getCityList();
                        for (int i = 0; i < cas.size(); i++) {
                            if (cas.get(i).getId().equals(nid)) {
                                cas.get(i).setAqi(Integer.parseInt(naqi));
                            }
                        }
                        CacheUtil.getACache().remove("citys");
                        String json = JSON.toJSON(cas).toString();
                        CacheUtil.getACache().put("citys", json);
                        LogUtil.e("citywweather", "fresh=========");
                        CacheUtil.getACache().put(nid, result);
                        LogUtil.e("citywweather", "fresh finished");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                swf.setRefreshing(false);
                                Toast.makeText(getActivity(), "更新完毕", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
            }
        });
        return view;
    }

    //从缓存中取已选城市数据
    private List<CityAqi> getCityList() {
        String citys = CacheUtil.getACache().getAsString("citys");
        if (citys == null && TextUtils.isEmpty(citys)) {
            //            Toast.makeText(this, "数据加载异常", Toast.LENGTH_SHORT).show();
            return new ArrayList<CityAqi>();
        }
        List<CityAqi> cqList = JSON.parseArray(citys, CityAqi.class);
        return cqList;

    }

    private void setCityWeatherData(AirWeather aw) {
        name.setText(aw.getHeWeather5().get(0).getBasic().getCity());
        int tempaqi = Integer.parseInt(aw.getHeWeather5().get(0).getAqi().getCity().getAqi());
        aqi.setText(tempaqi + "");
        if (tempaqi <= 30) {
            aqibg.setBackground(getResources().getDrawable(R.drawable.city_weather_aqi_nice_bg));
        } else if (tempaqi > 30 && tempaqi <= 60) {
            aqibg.setBackground(getResources().getDrawable(R.drawable.city_weather_aqi_mid_bg));
        } else if (tempaqi > 60) {
            aqibg.setBackground(getResources().getDrawable(R.drawable.city_weather_aqi_bad_bg));
        }

        maxtmp.setText(aw.getHeWeather5().get(0).getDaily_forecast().get(0).getTmp().getMax());
        mintmp.setText(aw.getHeWeather5().get(0).getDaily_forecast().get(0).getTmp().getMin());
        tmp.setText(aw.getHeWeather5().get(0).getNow().getTmp());
        weather.setText(aw.getHeWeather5().get(0).getNow().getCond().getTxt());
        String tmpco = aw.getHeWeather5().get(0).getAqi().getCity().getCo();
        if (TextUtils.isEmpty(tmpco)) {
            co.setText("暂无数据");
            codw.setVisibility(View.INVISIBLE);
        } else {
            co.setText(tmpco);
        }
        String tmpo3 = aw.getHeWeather5().get(0).getAqi().getCity().getO3();
        if (TextUtils.isEmpty(tmpo3)) {
            o3.setText("暂无数据");
            o3dw.setVisibility(View.INVISIBLE);
        } else {
            o3.setText(tmpo3);
        }
        String tmpno2 = aw.getHeWeather5().get(0).getAqi().getCity().getNo2();
        if (TextUtils.isEmpty(tmpno2)) {
            no2.setText("暂无数据");
            no2dw.setVisibility(View.INVISIBLE);
        } else {
            no2.setText(tmpno2);
        }
        String tmpso2 = aw.getHeWeather5().get(0).getAqi().getCity().getSo2();
        if (TextUtils.isEmpty(tmpso2)) {
            so2.setText("暂无数据");
            so2dw.setVisibility(View.INVISIBLE);
        } else {
            so2.setText(tmpso2);
        }
        String wm2 = aw.getHeWeather5().get(0).getAqi().getCity().getPm25();
        pm2.setText(wm2);
        String wm10 = aw.getHeWeather5().get(0).getAqi().getCity().getPm10();
        pm10.setText(wm10);
        //        Log.e("TAG","wwm2:"+wm2+"wm10:"+wm10);
        double daywm = (Double.parseDouble(wm2) + 30) * 0.75 * 8;
        wmzl.setText(daywm + "");
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.#");
        String nx = df.format((350000 / daywm) / 365);
        year.setText(nx);
        ss.setText(aw.getHeWeather5().get(0).getSuggestion().getComf().getBrf());
        ssdesc.setText(aw.getHeWeather5().get(0).getSuggestion().getComf().getTxt());
        cy.setText(aw.getHeWeather5().get(0).getSuggestion().getDrsg().getBrf());
        cydesc.setText(aw.getHeWeather5().get(0).getSuggestion().getDrsg().getTxt());
        gm.setText(aw.getHeWeather5().get(0).getSuggestion().getFlu().getBrf());
        gmdesc.setText(aw.getHeWeather5().get(0).getSuggestion().getFlu().getTxt());
        cx.setText(aw.getHeWeather5().get(0).getSuggestion().getTrav().getBrf());
        cxdesc.setText(aw.getHeWeather5().get(0).getSuggestion().getTrav().getTxt());
        String dd = "今天白天" + aw.getHeWeather5().get(0).getDaily_forecast().get(0).getCond().getTxt_d() + "  ,夜晚" + aw.getHeWeather5().get(0).getDaily_forecast().get(0).getCond().getTxt_n() + "  ,最高气温" + aw.getHeWeather5().get(0).getDaily_forecast().get(0).getTmp().getMax() + "  ,最低气温" + aw.getHeWeather5().get(0).getDaily_forecast().get(0).getTmp().getMin() + "  ," + aw.getHeWeather5().get(0).getDaily_forecast().get(0).getWind().getDir() + aw.getHeWeather5().get(0).getDaily_forecast().get(0).getWind().getSc();
        String dm = "明天白天" + aw.getHeWeather5().get(0).getDaily_forecast().get(1).getCond().getTxt_d() + "  ,夜晚" + aw.getHeWeather5().get(0).getDaily_forecast().get(1).getCond().getTxt_n() + "  ,最高气温" + aw.getHeWeather5().get(0).getDaily_forecast().get(1).getTmp().getMax() + "  ,最低气温" + aw.getHeWeather5().get(0).getDaily_forecast().get(1).getTmp().getMin() + "  ," + aw.getHeWeather5().get(0).getDaily_forecast().get(1).getWind().getDir() + aw.getHeWeather5().get(0).getDaily_forecast().get(1).getWind().getSc();
        String dh = "后天白天" + aw.getHeWeather5().get(0).getDaily_forecast().get(2).getCond().getTxt_d() + "  ,夜晚" + aw.getHeWeather5().get(0).getDaily_forecast().get(2).getCond().getTxt_n() + "  ,最高气温" + aw.getHeWeather5().get(0).getDaily_forecast().get(2).getTmp().getMax() + "  ,最低气温" + aw.getHeWeather5().get(0).getDaily_forecast().get(2).getTmp().getMin() + "  ," + aw.getHeWeather5().get(0).getDaily_forecast().get(2).getWind().getDir() + aw.getHeWeather5().get(0).getDaily_forecast().get(2).getWind().getSc();
        tod.setText(dd);
        tom.setText(dm);
        toh.setText(dh);
    }
}
