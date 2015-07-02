package com.ty.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ty.coolweather.model.City;
import com.ty.coolweather.model.County;
import com.ty.coolweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tony on 2015/6/30.
 */
public class CoolWeatherDB {

    /**
     * 数据库名
     */
    public static final String DB_NAME = "cool_weather";

    public static final int VERSION = 1;

    private static CoolWeatherDB coolWeatherDB;

    private SQLiteDatabase db;

    /**
     * 构造方法私有化
     */
    private CoolWeatherDB(Context context) {

        CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public synchronized static CoolWeatherDB getInstance(Context context) {
        if (coolWeatherDB == null) {
            coolWeatherDB = new CoolWeatherDB(context);
        }

        return coolWeatherDB;
    }

    /**
     * 保存省份信息到DB
     *
     * @param province
     * @return
     */
    public long saveProvince(Province province) {
        if (province != null) {
            ContentValues values = new ContentValues();
            values.put("province_name", province.getProvinceName());
            values.put("province_code", province.getProvinceCode());

            return db.insert("Province", null, values);
        } else {
            return 0;
        }

    }

    /**
     * 从数据库读取全国所有省份信息
     */
    public List<Province> loadProvinces() {
        List<Province> provinceList = new ArrayList<Province>();
        Cursor cursor = db.rawQuery("select * from Province", null);
        if (cursor.moveToFirst()) {
            do {
                Province e = new Province();
                e.setId(cursor.getInt(cursor.getColumnIndex("id")));
                e.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                e.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));

                provinceList.add(e);

            } while (cursor.moveToNext());


        }


        return provinceList;
    }

    /**
     * 存储City实例到数据库
     *
     * @param city
     * @return
     */
    public long saveCity(City city) {
        if (city != null) {
            ContentValues values = new ContentValues();
            values.put(City.CITY_CODE, city.getCityCode());
            values.put(City.CITY_NAME, city.getCityName());
            values.put(City.PROVINCE_ID, city.getProvinceId());

            return db.insert(City.TABLE_CITY, null, values);
        } else {
            return 0;
        }
    }


    public List<City> loadCities(int provinceId) {
        List<City> cityList = new ArrayList<City>();
        if (provinceId > 0) {
            Cursor cursor = db.rawQuery(" select * from " + City.TABLE_CITY + " where " + City.PROVINCE_ID + " = ? ", new String[]{Integer.toString(provinceId)});

            if (cursor.moveToFirst()) {
                do {
                    City city = new City();
                    city.setProvinceId(provinceId);
                    city.setId(cursor.getInt(cursor.getColumnIndex(City.ID)));
                    city.setCityCode(cursor.getString(cursor.getColumnIndex(City.CITY_CODE)));
                    city.setCityName(cursor.getString(cursor.getColumnIndex(City.CITY_NAME)));
                    cityList.add(city);
                } while (cursor.moveToNext());

                return cityList;
            }
        }
        return cityList;
    }

    /**
     * 将County实例存储到数据库。
     */
    public void saveCounty(County county) {
        if (county != null) {
            ContentValues values = new ContentValues();
            values.put("county_name", county.getCountyName());
            values.put("county_code", county.getCountyCode());
            values.put("city_id", county.getCityId());
            db.insert("County", null, values);
        }
    }
    /**
     * 从数据库读取某城市下所有的县信息。
     */
    public List<County> loadCounties(int cityId) {
        List<County> list = new ArrayList<County>();
        Cursor cursor = db.query("County", null, "city_id = ?",
                new String[] { String.valueOf(cityId) }, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor
                        .getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor
                        .getColumnIndex("county_code")));
                county.setCityId(cityId);
                list.add(county);
            } while (cursor.moveToNext());
        }
        return list;
    }


}
