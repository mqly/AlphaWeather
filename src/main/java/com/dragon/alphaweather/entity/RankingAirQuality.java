package com.dragon.alphaweather.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/18.
 */

public class RankingAirQuality implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * so2 : 6
     * o3 : 51
     * area_code : sansha
     * pm2_5 : 7
     * primary_pollutant :
     * ct : 2017-02-18 21:50:30.669
     * co : 0.3
     * area : 三沙
     * no2 : 30
     * aqi : 30
     * quality : 优
     * pm10 : 20
     * o3_8h : 58
     */

    private int so2;
    private int o3;
    private String area_code;
    private int pm2_5;
    private String primary_pollutant;
    private String ct;
    private double co;
    private String area;
    private int no2;
    private int aqi;
    private String quality;
    private int pm10;
    private int o3_8h;

    public RankingAirQuality() {

    }

    public int getSo2() {
        return so2;
    }

    public void setSo2(int so2) {
        this.so2 = so2;
    }

    public int getO3() {
        return o3;
    }

    public void setO3(int o3) {
        this.o3 = o3;
    }

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }

    public int getPm2_5() {
        return pm2_5;
    }

    public void setPm2_5(int pm2_5) {
        this.pm2_5 = pm2_5;
    }

    public String getPrimary_pollutant() {
        return primary_pollutant;
    }

    public void setPrimary_pollutant(String primary_pollutant) {
        this.primary_pollutant = primary_pollutant;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public double getCo() {
        return co;
    }

    public void setCo(double co) {
        this.co = co;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getNo2() {
        return no2;
    }

    public void setNo2(int no2) {
        this.no2 = no2;
    }

    public int getAqi() {
        return aqi;
    }

    public void setAqi(int aqi) {
        this.aqi = aqi;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public int getPm10() {
        return pm10;
    }

    public void setPm10(int pm10) {
        this.pm10 = pm10;
    }

    public int getO3_8h() {
        return o3_8h;
    }

    public void setO3_8h(int o3_8h) {
        this.o3_8h = o3_8h;
    }
}
