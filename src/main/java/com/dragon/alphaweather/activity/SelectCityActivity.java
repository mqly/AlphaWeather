package com.dragon.alphaweather.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.dragon.alphaweather.R;
import com.dragon.alphaweather.adapter.SortAdapter;
import com.dragon.alphaweather.application.MyApplication;
import com.dragon.alphaweather.entity.City;
import com.dragon.alphaweather.greendao.CityDao;
import com.dragon.alphaweather.ui.sortListView.CharacterParser;
import com.dragon.alphaweather.ui.sortListView.PinyinComparator;
import com.dragon.alphaweather.ui.sortListView.SideBar;
import com.dragon.alphaweather.ui.sortListView.SortModel;
import com.dragon.alphaweather.utils.Constant;
import com.dragon.alphaweather.utils.LogUtil;
import com.dragon.alphaweather.utils.OkHttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/20.
 */

public class SelectCityActivity extends AppCompatActivity {
    @BindView(R.id.select_city_toolbar)
    Toolbar toolbar;
    @BindString(R.string.select_city)
    String selectCityTitle;
    @BindView(R.id.select_city_listview)
    ListView sortListView;
    @BindView(R.id.sidebar)
    SideBar sideBar;
    @BindView(R.id.select_city_dialog)
    TextView dialog;
    static String TAG = "SelectCityActivity";
    private List<City> cityDatas = new ArrayList<City>();
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    public List<SortModel> sourceDataList = new ArrayList<SortModel>();

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    private SortAdapter adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        ButterKnife.bind(this);
        initToolBar();
        setCityDatas();
        initSortListView();
    }

    private void initToolBar() {
        toolbar.setTitle(selectCityTitle);
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

    //初始化sortlistview和sort数据
    public void initSortListView() {
        initSortListViewData();
        LogUtil.e(TAG, "----------" + sourceDataList.size());
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        sideBar.setTextView(dialog);
        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        });
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //这里要利用adapter.getItem(position)来获取当前position所对应的对象
                SortModel sm = (SortModel) adapter.getItem(position);
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra("id", sm.getId());
                intent.putExtra("name", sm.getName());
                //设置返回数据
                SelectCityActivity.this.setResult(RESULT_OK, intent);
                //关闭Activity
                SelectCityActivity.this.finish();
//                Toast.makeText(getApplication(), sm.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        Collections.sort(sourceDataList, pinyinComparator);
        adapter = new SortAdapter(this, sourceDataList);
        sortListView.setAdapter(adapter);
    }

    //根据城市列表数据转换字母排序的数据列表
    public List<SortModel> initSortListViewData() {
        for (int i = 0; i < cityDatas.size(); i++) {
//            LogUtil.e(TAG,"===zhongwen===="+cityDatas.get(i).getId());
            SortModel sortModel = new SortModel();
            sortModel.setName(cityDatas.get(i).getCityZh());
            sortModel.setId(cityDatas.get(i).getId());
            //汉字转换成拼音
            String pinyin = cityDatas.get(i).getCityEn();
//            String pinyin = characterParser.getSelling(cityDatas.get(i).getCityZh());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            sourceDataList.add(sortModel);
        }
        LogUtil.e(TAG, "mSortList:" + sourceDataList.size());
        return sourceDataList;
    }

    public static void activityStart(Context context) {
        Intent intent = new Intent(context, SelectCityActivity.class);
        context.startActivity(intent);
    }

    //删减服务器返回的数据为标准json
    public String formatJson(String jsonString) {
        LogUtil.e(TAG, "格式化jsonString:" + jsonString);
        String rs = jsonString.substring(187, jsonString.length() - 1);
        String result = "[" + rs + "]";
        LogUtil.e(TAG, "rs:" + result);
        return result;
    }

    //通过jsonarray解析City List
    public List<City> getAllCityList(String jsonString) {
        List<City> citys = new ArrayList<City>();
        citys = JSON.parseArray(jsonString, City.class);
        LogUtil.e(TAG, "cityid" + citys.get(0).getId());
        return citys;
    }

    //模板方法，从数据库获取城市，如果数据库无数据，则联网获取
    public void setCityDatas() {
        List<City> citys = getCityFromDB();
        if (citys.size() > 0) {
            cityDatas = citys;
            LogUtil.e(TAG, "set city data from db");
        } else {
            showProgressDialog();
            GetAllCityFromTask task = new GetAllCityFromTask();
            task.execute();
            LogUtil.e(TAG, "set city data from web");
        }

    }

    //从数据库获取城市
    public List<City> getCityFromDB() {
        CityDao dao = MyApplication.getInstance().getDaoSession().getCityDao();
        List<City> citys = dao.queryBuilder().build().list();
        return citys;
    }

    //城市数据存入数据库
    public void saveToDB(List<City> citys) {
        CityDao dao = MyApplication.getInstance().getDaoSession().getCityDao();
        dao.insertInTx(citys);
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

    public class GetAllCityFromTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            Request request = new Request.Builder().get().url(Constant.ALL_CITY_URL).build();
            Call call = OkHttpUtil.getOkHttpClient().newCall(request);
            try {
                Response response = call.execute();
                String result = response.body().string();
                LogUtil.e(TAG, result);
                return result;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String result = formatJson(s);
            cityDatas = getAllCityList(result);
            saveToDB(cityDatas);
            initSortListViewData();
            Collections.sort(sourceDataList, pinyinComparator);
            closeProgressDialog();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }

        }
    }

}
