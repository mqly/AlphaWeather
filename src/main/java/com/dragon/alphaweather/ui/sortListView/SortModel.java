package com.dragon.alphaweather.ui.sortListView;

/**
 * Created by Administrator on 2017/2/20.
 */

public class SortModel {

    private String name;   //显示的数据
    private String sortLetters;  //显示数据拼音的首字母
    private String id;//城市id

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
}

