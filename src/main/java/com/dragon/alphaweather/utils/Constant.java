package com.dragon.alphaweather.utils;

/**
 * Created by Administrator on 2017/2/18.
 */

public class Constant {
    final public static String AIR_RANKING_URL = "http://route.showapi.com/104-41?showapi_appid=32258&showapi_sign=c6c96c3c6b2842deb7362b72c9e26976";
    final public static String AIR_QUALITY_URL = "http://route.showapi.com/104-29?showapi_appid=32258&showapi_sign=c6c96c3c6b2842deb7362b72c9e26976&city=";
    final public static String ALL_CITY_URL = "http://files.heweather.com/china-city-list.json";
    final public static String GET_CITY_AIR_WEATHER_URL = "https://free-api.heweather.com/v5/weather?key=534e905e803e4776aec388860300589a&city=";
    final public static long readTimeOut = 200;
    final public static long writeTimeOut = 200;
    final public static long connTimeOut = 200;

    /*//获取省会城市及其经纬度
    public static List<CityAqi> getCapitalCitys() {
        List<CityAqi> capitalCities = new ArrayList<CityAqi>();
        CityAqi c1 = new CityAqi("北京", new LatLng(116.427287, 39.904983));
        CityAqi c2 = new CityAqi("上海", new LatLng(121.455854, 31.249585));
        CityAqi c3 = new CityAqi("天津", new LatLng(117.211693, 39.137783));
        CityAqi c4 = new CityAqi("重庆", new LatLng(106.550972, 29.609469));
        CityAqi c5 = new CityAqi("哈尔滨", new LatLng(126.630806, 45.760082));
        CityAqi c6 = new CityAqi("长春", new LatLng(125.3245, 43.886841));
        CityAqi c7 = new CityAqi("沈阳", new LatLng(123.391522, 41.792495));
        CityAqi c8 = new CityAqi("呼和浩特", new LatLng(111.765954, 40.849392));
        CityAqi c9 = new CityAqi("石家庄", new LatLng(114.581383, 38.069355));
        CityAqi c10 = new CityAqi("乌鲁木齐", new LatLng(87.40599, 43.879913));
        CityAqi c11 = new CityAqi("兰州", new LatLng(103.856056, 36.048649));
        CityAqi c12 = new CityAqi("西宁", new LatLng(101.778916, 36.623178));
        CityAqi c13 = new CityAqi("西安", new LatLng(109.301859, 34.502253));
        CityAqi c14 = new CityAqi("银川", new LatLng(106.17397, 38.491916));
        CityAqi c15 = new CityAqi("郑州", new LatLng(113.658065, 34.745814));
        CityAqi c16 = new CityAqi("济南", new LatLng(117.000923, 36.675807));
        CityAqi c17 = new CityAqi("太原", new LatLng(112.587298, 37.860315));
        CityAqi c18 = new CityAqi("合肥", new LatLng(117.316933, 31.88513));
        CityAqi c19 = new CityAqi("武汉", new LatLng(114.424376, 30.607375));
        CityAqi c20 = new CityAqi("长沙", new LatLng(113.04448, 28.168306));
        CityAqi c21 = new CityAqi("南京", new LatLng(118.742372, 32.035217));
        CityAqi c22 = new CityAqi("成都", new LatLng(104.141085, 30.628884));
        CityAqi c23 = new CityAqi("贵阳", new LatLng(106.702406, 26.556865));
        CityAqi c24 = new CityAqi("昆明", new LatLng(102.712251, 25.040609));
        CityAqi c25 = new CityAqi("南宁", new LatLng(108.314838, 22.826993));
        CityAqi c26 = new CityAqi("拉萨", new LatLng(91.097725, 29.65513));
        CityAqi c27 = new CityAqi("杭州", new LatLng(120.213064, 30.291119));
        CityAqi c28 = new CityAqi("南昌", new LatLng(115.919645, 28.661914));
        CityAqi c29 = new CityAqi("广州", new LatLng(113.257502, 23.149192));
        CityAqi c30 = new CityAqi("福州", new LatLng(119.306239, 26.075302));
        CityAqi c31 = new CityAqi("台北", new LatLng(114.092173, 40.422737));
        CityAqi c32 = new CityAqi("海口", new LatLng(110.161343, 20.02725));
        CityAqi c33 = new CityAqi("香港", new LatLng(114.162952, 22.282068));
        CityAqi c34 = new CityAqi("澳门", new LatLng(113.54909, 22.198951));
        capitalCities.add(c1);
        capitalCities.add(c2);
        capitalCities.add(c3);
        capitalCities.add(c4);
        capitalCities.add(c5);
        capitalCities.add(c6);
        capitalCities.add(c7);
        capitalCities.add(c8);
        capitalCities.add(c9);
        capitalCities.add(c10);
        capitalCities.add(c11);
        capitalCities.add(c12);
        capitalCities.add(c13);
        capitalCities.add(c14);
        capitalCities.add(c15);
        capitalCities.add(c16);
        capitalCities.add(c17);
        capitalCities.add(c18);
        capitalCities.add(c19);
        capitalCities.add(c20);
        capitalCities.add(c21);
        capitalCities.add(c22);
        capitalCities.add(c23);
        capitalCities.add(c24);
        capitalCities.add(c25);
        capitalCities.add(c26);
        capitalCities.add(c27);
        capitalCities.add(c28);
        capitalCities.add(c29);
        capitalCities.add(c30);
        capitalCities.add(c31);
        capitalCities.add(c32);
        capitalCities.add(c33);
        capitalCities.add(c34);
        return capitalCities;
    }*/

}
