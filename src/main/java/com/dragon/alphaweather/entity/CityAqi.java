package com.dragon.alphaweather.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/19.
 */

public class CityAqi implements Serializable {
    private String name;
    private String lat;
    private String lon;
    private int aqi;
    private String id;
    private static final long serialVersionUID = 1L;

    public CityAqi() {

    }

    public CityAqi(String id, String name, String lat, String lon, int aqi) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.aqi = aqi;
    }

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


    public int getAqi() {
        return aqi;
    }

    public void setAqi(int aqi) {
        this.aqi = aqi;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
