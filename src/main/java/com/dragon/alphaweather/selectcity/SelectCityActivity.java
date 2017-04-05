package com.dragon.alphaweather.selectcity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dragon.alphaweather.R;
import com.dragon.alphaweather.ui.sortListView.CharacterParser;
import com.dragon.alphaweather.ui.sortListView.PinyinComparator;
import com.dragon.alphaweather.ui.sortListView.SideBar;
import com.dragon.alphaweather.ui.sortListView.SortModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/20.
 */

public class SelectCityActivity extends AppCompatActivity implements SelectCityContract.View {
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
    private SelectCityContract.Presenter selectCityPresenter;
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
        setPresenter(new SelectCityPresenter(this));
        selectCityPresenter.start();
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

    public static void activityStart(Context context) {
        Intent intent = new Intent(context, SelectCityActivity.class);
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

    //显示城市列表数据
    @Override
    public void showCityDatas(List<SortModel> sourceDataList) {
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
            }
        });
        Collections.sort(sourceDataList, pinyinComparator);
        adapter = new SortAdapter(this, sourceDataList);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sortListView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(SelectCityContract.Presenter presenter) {
        selectCityPresenter = presenter;
    }

}
