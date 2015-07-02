package com.ty.coolweather.model;

/**
 * Created by Tony on 2015/6/25.
 */
public class City {
    private int id;
    private String cityName;
    private String cityCode;
    private int provinceId;

    public static final String TABLE_CITY = "City";

    public static final String ID = "id";

    public static final String CITY_NAME = "city_name";

    public static final String CITY_CODE = "city_code";

    public static final String PROVINCE_ID = "province_id";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}