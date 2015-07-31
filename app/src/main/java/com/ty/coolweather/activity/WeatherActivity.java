package com.ty.coolweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ty.coolweather.R;
import com.ty.coolweather.util.HttpCallbackListener;
import com.ty.coolweather.util.HttpUtil;
import com.ty.coolweather.util.Utility;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tony on 2015/7/30.
 */
public class WeatherActivity extends Activity {

   private LinearLayout weatherInfoLayout;
    private TextView cityNameTv;
    private TextView publishTimeTv;
    private TextView weatherDespTv;
    private TextView temp1Tv;
    private TextView temp2Tv;
    private ImageView img1;
    //private TextView currentDateTv;

    private Map<String, Integer> imageMap;

    private Button switchCityBtn;
    private Button refreshWeatherBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initImageMapping();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);

        weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
        cityNameTv = (TextView) findViewById(R.id.city_name);
        publishTimeTv = (TextView) findViewById(R.id.publish_text);
        weatherDespTv = (TextView) findViewById(R.id.weather_desc);
        temp1Tv = (TextView) findViewById(R.id.temp1);
        temp2Tv = (TextView) findViewById(R.id.temp2);
        img1 = (ImageView) findViewById(R.id.image);
        //currentDateTv = (TextView) findViewById(R.id.current_date);

        switchCityBtn = (Button) findViewById(R.id.switch_city);
        switchCityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, ChooseAreaActivity.class);
                intent.putExtra("fromWeatherActivity", true);
                startActivity(intent);
                finish();
            }
        });

        refreshWeatherBtn = (Button) findViewById(R.id.refresh_weather);
        refreshWeatherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String weatherCode = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).getString("weatherCode", "");
                if ( !TextUtils.isEmpty(weatherCode)) {
                    publishTimeTv.setText("手机君卖力同步中。。。");
                    queryWeatherInfo(weatherCode);
                }

            }
        });

        String countyCode = getIntent().getStringExtra("countyCode");
        if ( !TextUtils.isEmpty(countyCode)) {
            publishTimeTv.setText("手机君卖力同步中。。。");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameTv.setVisibility(View.INVISIBLE);
            queryWeatherCode(countyCode);
        } else {
            showWeatherBySharedPreferences();
        }
    }

    private void initImageMapping() {
        imageMap = new HashMap<String, Integer>();
        imageMap.put("d0", R.drawable.d0);
        imageMap.put("d1", R.drawable.d1);
        imageMap.put("d2", R.drawable.d2);
        imageMap.put("d3", R.drawable.d3);
        imageMap.put("d4", R.drawable.d4);
        imageMap.put("d5", R.drawable.d5);
        imageMap.put("d6", R.drawable.d6);
        imageMap.put("d7", R.drawable.d7);
        imageMap.put("d8", R.drawable.d8);
        imageMap.put("d9", R.drawable.d9);
        imageMap.put("d10", R.drawable.d10);
        imageMap.put("d12", R.drawable.d12);
        imageMap.put("d13", R.drawable.d13);
        imageMap.put("d14", R.drawable.d14);
        imageMap.put("d15", R.drawable.d15);
        imageMap.put("d16", R.drawable.d16);
        imageMap.put("d17", R.drawable.d17);
        imageMap.put("d18", R.drawable.d18);
        imageMap.put("d19", R.drawable.d19);
        imageMap.put("d20", R.drawable.d20);
        imageMap.put("d21", R.drawable.d21);
        imageMap.put("d22", R.drawable.d22);
        imageMap.put("d23", R.drawable.d23);
        imageMap.put("d24", R.drawable.d24);
        imageMap.put("d25", R.drawable.d25);
        imageMap.put("d26", R.drawable.d26);
        imageMap.put("d27", R.drawable.d27);
        imageMap.put("d28", R.drawable.d28);
        imageMap.put("d29", R.drawable.d29);
        imageMap.put("d30", R.drawable.d30);
        imageMap.put("d31", R.drawable.d31);
        imageMap.put("d53", R.drawable.d53);
    }

    private void queryWeatherCode(String countyCode) {
        String address = "http://www.weather.com.cn/data/list3/city" + countyCode + ".xml";
        queryFromServer(address, "countyCode");
    }

    private void queryWeatherInfo(String weatherCode) {
        String address = "http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
        queryFromServer(address, "weatherCode");
    }

    private void queryFromServer(final String address, final String type) {
        HttpUtil.sendRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if ("countyCode".equals(type)) {
                    if (!TextUtils.isEmpty(response)) {
                        String[] array = response.split("\\|");
                        if (array != null && array.length == 2) {
                            String weatherCode = array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                } else if ("weatherCode".equals(type)) {
                    Utility.handleWeatherResponse(WeatherActivity.this, response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeatherBySharedPreferences();
                        }
                    });
                }

            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishTimeTv.setText("同步失败，手机君跪地求饶");
                    }
                });

            }
        });
    }

    private void showWeatherBySharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
        cityNameTv.setText(sharedPreferences.getString("cityName", ""));
        temp1Tv.setText(sharedPreferences.getString("temp1" , ""));
        temp2Tv.setText(sharedPreferences.getString("temp2", ""));
        String imageName = sharedPreferences.getString("img1", "");
        Log.d(this.getClass().getSimpleName(), "imageName " + imageName);
        img1.setImageResource(imageMap.get(imageName));
        weatherDespTv.setText(sharedPreferences.getString("weatherDesp" , ""));
        //currentDateTv.setText(sharedPreferences.getString("currentDate", ""));
        publishTimeTv.setText("今天"+sharedPreferences.getString("publishTime", "")+"发布");
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameTv.setVisibility(View.VISIBLE);
    }


}
