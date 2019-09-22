package com.example.weather_forecast.activitys;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.bumptech.glide.Glide;
import com.example.weather_forecast.Beans.AQI;
import com.example.weather_forecast.Beans.Forecast;
import com.example.weather_forecast.Beans.HeWeather;
import com.example.weather_forecast.Beans.Now;
import com.example.weather_forecast.Beans.Suggestion;
import com.example.weather_forecast.R;
import com.example.weather_forecast.Utils.GetRequest_Interface;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TAG";
    private String weatherId;
    //必应图片
    private ImageView iv_bingPic;
    private SwipeRefreshLayout swipe_refresh_layout;
    private AppBarLayout app_bar_layout;
    /**
     * 左滑菜单
     */
    private DrawerLayout drawer_layout;
    private NavigationView navigation_view;
    private ImageView iv_menu;
    //REQUEST_CODE>=0
    private static final int REQUEST_CODE = 0;
    /**
     * activity_divider
     */
    private TextView tv_updateTime;
    /**
     * activity_weather
     */
    private ImageView iv_weather;
    private TextView tv_area;
    private ImageView iv_location;
    private String substringDistrict;
    private TextView tv_weather;
    private TextView tv_rain_possibility;
    private TextView tv_temperature;
    private TextView tv_temperature_range;
    /**
     * 未来三天天气预报
     */
    private TextView tv_forecast;
    private LinearLayout linear_forecast;
    /**
     * 空气质量
     */
    private TextView tv_pm25;
    private TextView tv_pm10;
    private TextView tv_o3;
    private TextView tv_no2;
    private TextView tv_co;
    private TextView tv_so2;
    private TextView tv_aqi_txt;
    private TextView tv_aqi_num;
    /**
     * 舒适度
     */
    private TextView tv_fl;
    private TextView tv_vis;
    private TextView tv_hum;
    /**
     * 生活建议
     */
    private LinearLayout linear_suggestion;
    private TextView life_suggestion;
    /**
     * 高德地图设置
     */
    //声明AMapLocationClient对象
    private AMapLocationClient mLocationClient;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //背景图和状态栏融合
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_main);
        requestPermissions();
        initViews();
        initData();
        initGaoDeAPI();
        startGaoDeAPI();
        requestBingPic();
    }

    private void initViews() {
        iv_weather = findViewById(R.id.iv_weather);
        iv_location = findViewById(R.id.iv_location);
        tv_area = findViewById(R.id.tv_area);
        tv_weather = findViewById(R.id.tv_weather);
        tv_rain_possibility = findViewById(R.id.tv_rain_possibility);
        tv_temperature = findViewById(R.id.tv_temperature);
        tv_temperature_range = findViewById(R.id.tv_temperature_range);
        linear_forecast = findViewById(R.id.linear_forecast);
        tv_pm25 = findViewById(R.id.tv_pm25);
        tv_pm10 = findViewById(R.id.tv_pm10);
        tv_o3 = findViewById(R.id.tv_03);
        tv_no2 = findViewById(R.id.tv_no2);
        tv_co = findViewById(R.id.tv_co);
        tv_so2 = findViewById(R.id.tv_so2);
        tv_aqi_txt = findViewById(R.id.tv_aqi_txt);
        tv_aqi_num = findViewById(R.id.tv_aqi_num);
        tv_updateTime = findViewById(R.id.tv_updateTime);
        tv_hum = findViewById(R.id.tv_hum);
        tv_fl = findViewById(R.id.tv_fl);
        tv_vis = findViewById(R.id.tv_vis);
        tv_forecast = findViewById(R.id.tv_forecast);
        linear_suggestion = findViewById(R.id.linear_suggestion);
        iv_bingPic = findViewById(R.id.iv_bingPic);
        drawer_layout = findViewById(R.id.drawer_layout);
        navigation_view = findViewById(R.id.navigation_view);
        iv_menu = findViewById(R.id.iv_menu);
        swipe_refresh_layout = findViewById(R.id.swipe_refresh_layout);
        app_bar_layout = findViewById(R.id.app_bar_layout);
        life_suggestion = findViewById(R.id.life_suggestion);
    }

    private void initData() {
        if(canGetSharedPreferencesData()){
            getSharedPreferencesData();
        }
        //左侧菜单按钮的点击事件
        iv_menu.setOnClickListener((v) -> drawer_layout.openDrawer(Gravity.START));
        //NavigationView点击事件
        navigation_view.setNavigationItemSelectedListener((menuItem) -> {
            switch (menuItem.getItemId()) {
                case R.id.tv_manage_city:
                    if (substringDistrict != null) {
                        Intent intent = new Intent(MainActivity.this, PickerCityActivity.class);
//                        drawerLayout.closeDrawer(Gravity.START);
                        intent.putExtra("substringDistrict", substringDistrict);
                        startActivityForResult(intent, REQUEST_CODE);
                    } else {
                        Toast.makeText(this, "请检查当前网络连接", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            return false;
        });
        //AppBarLayout中处理滑动冲突并响应更新监听
        app_bar_layout.addOnOffsetChangedListener((addBarLayout, verticalOffset) -> {
            //只有当AppBarLayout滑动到顶部时，才可以刷新页面，也就是SwipeRefreshLayout可用
            //注意这里是setEnabled，也就是设置是否可用，只有Enabled状态才会去响应更新监听
            if (verticalOffset >= 0) {
                swipe_refresh_layout.setEnabled(true);
            } else {
                swipe_refresh_layout.setEnabled(false);
            }
        });
        swipe_refresh_layout.setColorSchemeResources(R.color.colorPrimaryDark);
        swipe_refresh_layout.setOnRefreshListener(() -> startGaoDeAPI());
    }

    private void initGaoDeAPI() {
        //初始化AMapLocationClient对象
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式(默认使用高精度模式)
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位请求超时时间(默认30000毫秒，建议超时时间不要低于8000毫秒)
        mLocationOption.setHttpTimeOut(8000);
        //设置是都开启缓存机制(默认开启)
        mLocationOption.setLocationCacheEnable(true);
        //获取一次定位结果,该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
    }

    private void startGaoDeAPI() {
        //启动定位
        mLocationClient.startLocation();
        //设置定位回调监听
        mLocationClient.setLocationListener((aMapLocation) -> {
            //首先判断AMapLocation对象不为空，当定位错误码类型为0时定位成功
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    String district = aMapLocation.getDistrict();//梅江区
                    if (district.endsWith("区")) {
                        substringDistrict = district.substring(0, district.length() - 1);//梅江
                    } else {
                        substringDistrict = district;
                    }
                    //一拿到目前位置后，马上停止定位
                    //停止定位后，本地定位服务并不会被销毁，下次进来调用onResume()方法，依旧开启定位服务并设置定位回掉监听
                    mLocationClient.stopLocation();
                    requestWeather();
                    iv_location.setVisibility(View.VISIBLE);
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，ErrInfo是错误信息
                    Log.e("AmapError", "location Error,ErrCode:" + aMapLocation.getErrorCode() + ",ErrInfo:" + aMapLocation.getErrorInfo());
                }
            }
        });
    }

    private void requestBingPic() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://guolin.tech/api/bing_pic").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "加载每日必应图片失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String urlString = response.body().string();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                preferences.edit().putString("BindPicUrl", urlString).apply();
                runOnUiThread(() -> Glide.with(MainActivity.this).load(urlString).into(iv_bingPic));
            }
        });
    }

    private void getSharedPreferencesData() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String activity_weather_str = preferences.getString("activity_weather", null);
        String activity_split_str = preferences.getString("activity_split", null);
        String activity_ari_quality_str = preferences.getString("activity_air_quality", null);
        String activity_comfortable = preferences.getString("activity_comfortable", null);
        String bindPicUrl = preferences.getString("BindPicUrl", null);
        String[] split_data_1 = splitData(activity_weather_str);
        String[] split_data_2 = splitData(activity_ari_quality_str);
        String[] split_data_3 = splitData(activity_comfortable);

        for (int i = 0; i < split_data_1.length; i++) {
            Log.e("TAG_SPLIT_data_1", split_data_1[i]);
        }

        tv_area.setText(split_data_1[0]);
        tv_temperature.setText(split_data_1[1]);
        tv_rain_possibility.setText(split_data_1[2]);
        tv_weather.setText(split_data_1[3]);
        tv_temperature_range.setText(split_data_1[4]);
        iv_weather.setImageResource(Integer.parseInt(split_data_1[5]));

        tv_updateTime.setText(activity_split_str);

        tv_co.setText(split_data_2[0]);
        tv_no2.setText(split_data_2[1]);
        tv_o3.setText(split_data_2[2]);
        tv_pm10.setText(split_data_2[3]);
        tv_pm25.setText(split_data_2[4]);
        tv_so2.setText(split_data_2[5]);
        tv_aqi_txt.setText(split_data_2[6]);
        tv_aqi_num.setText(split_data_2[7]);

        tv_fl.setText(split_data_3[0]);
        tv_vis.setText(split_data_3[1]);
        tv_hum.setText(split_data_3[2]);

        Glide.with(MainActivity.this).load(bindPicUrl).into(iv_bingPic);
        life_suggestion.setText("");
    }

    private boolean canGetSharedPreferencesData() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        if (!preferences.getBoolean("weatherBean", false)) {
            return false;
        }
        return true;
    }

    private void requestWeather() {
        if (substringDistrict == null) return;
        if (canGetSharedPreferencesData()) {
            getSharedPreferencesData();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://free-api.heweather.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        GetRequest_Interface result = retrofit.create(GetRequest_Interface.class);
        Observable<HeWeather> observable = result.getCall(substringDistrict, "bc0418b57b2d4918819d3974ac1285d9");
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HeWeather>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HeWeather heWeather) {
                        HeWeather.WeatherBean weatherBean = heWeather.getHeWeather5().get(0);
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("weatherBean", true);
                        /**
                         * activity_heather区域
                         */
                        iv_location.setImageResource(R.drawable.location);
                        //获取当前地区
                        String area = weatherBean.getBasic().getCity();
                        Now now = weatherBean.getNow();
                        //获取当前温度值
                        String tmp = now.getTmp();
                        //获取当天的天气预报
                        Forecast todayForecast = weatherBean.getWeatherList().get(0);
                        //获取当天降雨概率
                        int rain_possibility = todayForecast.getRain_possibility();
                        //获取当前天气状况
                        String weatherMsg = todayForecast.getWeather().getWeatherMsg();
                        //获取当前天气状况的代号（根据代码查找天气图片）
                        weatherId = todayForecast.getWeather().getWeatherId();
                        int drawableId = queryWeatherIcon(weatherId);
                        setWeatherIcon(drawableId, iv_weather);
                        //获取当前天气的最高温和最低温
                        String max = todayForecast.getTemperature().getMax();
                        String min = todayForecast.getTemperature().getMin();
                        /**
                         *  分割线的更新时间
                         */
                        String[] split = weatherBean.getBasic().getUpdate().getUpdateTime().split(" ");
                        /**
                         * 未来天气预报区域
                         */
                        linear_forecast.removeAllViews();
                        for (Forecast forecast : weatherBean.getWeatherList()) {
                            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_forecast, linear_forecast, false);
                            TextView tv_date = view.findViewById(R.id.tv_date);
                            ImageView iv_weather_icon = view.findViewById(R.id.iv_weather_icon);
                            TextView tv_weather_msg = view.findViewById(R.id.tv_weather_msg);
                            TextView tv_max_min = view.findViewById(R.id.tv_max_min);
                            tv_date.setText(forecast.getDate());
                            setWeatherIcon(queryWeatherIcon(forecast.getWeather().getWeatherId()), iv_weather_icon);
                            tv_weather_msg.setText(forecast.getWeather().getWeatherMsg());
                            tv_max_min.setText(forecast.getTemperature().getMax() + "℃/" + forecast.getTemperature().getMin() + "℃");
                            linear_forecast.addView(view);
                        }
                        linear_forecast.setVisibility(View.VISIBLE);
                        /**
                         *  空气质量区域
                         */
                        AQI.City city = weatherBean.getAqi().getCity();
                        /**
                         *  生活建议区域
                         */
                        life_suggestion.setText("生活建议");
                        int[] imageResources = {R.drawable.air, R.drawable.comf, R.drawable.flu, R.drawable.car, R.drawable.sport, R.drawable.week, R.drawable.clothe};
                        Suggestion suggestion = weatherBean.getSuggestion();
                        String[] suggestionBrf = {"空气质量：" + suggestion.getAir().getBrf(), "舒适感：" + suggestion.getComf().getBrf(), "流感指数：" + suggestion.getFlu().getBrf(), "洗车建议：" + suggestion.getCw().getBrf(), "运动建议：" + suggestion.getSport().getBrf(), "旅游建议：" + suggestion.getTrav().getBrf(), "着装建议：" + suggestion.getDrsg().getBrf()};
                        String[] suggestionTxt = {suggestion.getAir().getTxt(), suggestion.getComf().getTxt(), suggestion.getFlu().getTxt(), suggestion.getCw().getTxt(), suggestion.getSport().getTxt(), suggestion.getTrav().getTxt(), suggestion.getDrsg().getTxt()};
                        linear_suggestion.removeAllViews();
                        for (int i = 0; i < imageResources.length; i++) {
                            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_suggestion, linear_suggestion, false);
                            ImageView iv_suggestion = view.findViewById(R.id.iv_suggestion);
                            TextView tv_suggestion_txt = view.findViewById(R.id.tv_suggestion_txt);
                            TextView tv_suggestion_brf = view.findViewById(R.id.tv_suggestion_brf);
                            iv_suggestion.setImageResource(imageResources[i]);
                            tv_suggestion_brf.setText(suggestionBrf[i]);
                            tv_suggestion_txt.setText(suggestionTxt[i]);
                            //添加下划线
                            View divider = new View(MainActivity.this);
                            divider.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
                            divider.setTop(8);
                            divider.setBackgroundColor(getResources().getColor(R.color.lineColor, null));
                            linear_suggestion.addView(view);
                            if (i != imageResources.length - 1) {
                                linear_suggestion.addView(divider);
                            }
                        }
                        linear_suggestion.setVisibility(View.VISIBLE);

                        tv_area.setText(area);
                        tv_temperature.setText(tmp + "℃");
                        tv_rain_possibility.setText("降雨概率：" + rain_possibility + "%");
                        tv_weather.setText(weatherMsg);
                        tv_temperature_range.setText(max + "℃/" + min + "℃");
                        tv_updateTime.setText("最新更新时间：" + split[1]);
                        tv_co.setText(city.getCo());
                        tv_no2.setText(city.getNo2());
                        tv_o3.setText(city.getO3());
                        tv_pm10.setText(city.getPm10());
                        tv_pm25.setText(city.getPm25());
                        tv_so2.setText(city.getSo2());
                        tv_aqi_txt.setText(city.getQuality());
                        tv_aqi_num.setText("AQI指数：" + city.getAqi());
                        tv_fl.setText("体感温度：" + now.getTemperature() + "℃");
                        tv_vis.setText(now.getVisiable() + "公里");
                        tv_hum.setText(now.getHum());

                        editor.putString("activity_weather", area + "mSplit" + tmp + "℃" + "mSplit" + "降雨概率：" + rain_possibility + "%" + "mSplit" + weatherMsg + "mSplit" + max + "℃/" + min + "℃" + "mSplit" + drawableId);
                        editor.putString("activity_split", "上次更新时间：" + split[1]);
                        editor.putString("activity_air_quality", city.getCo() + "mSplit" + city.getNo2() + "mSplit" + city.getO3() + "mSplit" + city.getPm10() + "mSplit"
                                + city.getPm25() + "mSplit" + city.getSo2() + "mSplit" + city.getQuality() + "mSplit" + "AQI指数：" + city.getAqi());
                        editor.putString("activity_comfortable", "体感温度：" + now.getTemperature() + "℃" + "mSplit" + now.getVisiable() + "公里" + "mSplit" + now.getHum());
                        editor.apply();
                    }

                    @Override
                    public void onError(Throwable e) {
                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                                .setIcon(R.drawable.warning)
                                .setTitle("出错啦")
                                .setMessage("出现未知错误，建议重新启动程序")
                                .setNegativeButton("关闭", (dialog, which) -> finish())
                                .setNeutralButton("取消", (dialog, which) -> dialog.dismiss()).create();
                        alertDialog.show();
                        if (swipe_refresh_layout.isRefreshing()) {
                            swipe_refresh_layout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onComplete() {
                        tv_forecast.setVisibility(View.GONE);
                        swipe_refresh_layout.setRefreshing(false);
                    }
                });
    }

    private String[] splitData(String string) {
        String[] split = string.split("mSplit");
        for (int i = 0; i < split.length; i++) {
            Log.e("TAG_SPLIT", split[i]);
        }
        return split;
    }

    private void setWeatherIcon(int drawableId, ImageView view) {
        view.setImageResource(drawableId);
    }

    private int queryWeatherIcon(String weatherId) {
        switch (weatherId) {
            case "100":
                return R.drawable.sunny_d;
            case "101":
                return R.drawable.cloudy;
            case "102":
                return R.drawable.few_clouds;
            case "103":
                return R.drawable.partly_cloudy;
            case "104":
                return R.drawable.overcast_d;
            case "200":
                return R.drawable.windy;
            case "201":
                return R.drawable.calm;
            case "202":
                return R.drawable.light_breeze;
            case "203":
                return R.drawable.moderate_breeze;
            case "204":
                return R.drawable.fresh_breeze;
            case "205":
                return R.drawable.strong_breeze;
            case "206":
                return R.drawable.high_wind;
            case "207":
                return R.drawable.gale;
            case "209":
                return R.drawable.storm;
            case "300":
                return R.drawable.shower_rain;
            case "301":
                return R.drawable.heavy_shower_rain_d;
            case "302":
                return R.drawable.thunder_shower;
            case "303":
                return R.drawable.heavy_thunder_storm;
            case "305":
                return R.drawable.light_rain;
            case "306":
                return R.drawable.moderate_rain;
            case "307":
                return R.drawable.heavy_rain;
            case "310":
                return R.drawable.storm;
            case "311":
                return R.drawable.heavy_storm;
            case "312":
                return R.drawable.severe_storm;
            case "314":
                return R.drawable.light_to_moderate_rain;
            case "315":
                return R.drawable.moderate_to_heavy_rain;
            case "316":
                return R.drawable.heavy_rain_to_storm;
            case "317":
                return R.drawable.storm_to_heavy_storm;
            case "318":
                return R.drawable.heavy_to_server_storm;
            case "400":
                return R.drawable.light_snow;
            case "401":
                return R.drawable.moderate_snow;
            case "402":
                return R.drawable.heavy_snow;
            case "502":
                return R.drawable.haze;
            default:
                return R.drawable.default_weathericon;
        }
    }

    private void requestPermissions() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), REQUEST_CODE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁定位客户端，同时销毁本地定位服务
        mLocationClient.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer_layout.openDrawer(Gravity.START);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //筛选不同intent调用startActivityForResult()方法的requestCode
        if (requestCode == 0 && resultCode == RESULT_OK) {
            //如果传过来的值和定位的值不一致，隐藏定位图标
//            错误，获取Intent应该使用传过来的data，也就是参数中的Intent，通过getIntent()是获取不到对象的
//            String getDistrictName = getIntent().getStringExtra("districtName");
            String getDistrictName = data.getExtras().getString("districtName");
            Log.e("TAG", getDistrictName);
            Log.e("TAG", substringDistrict);
            if (getDistrictName != substringDistrict) {
                iv_location.setVisibility(View.GONE);
            }
            substringDistrict = getDistrictName;
            requestWeather();
            if (drawer_layout.isDrawerOpen(Gravity.START)) {
                drawer_layout.closeDrawer(Gravity.START);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        if (grantResult == PackageManager.PERMISSION_DENIED) {
                            Toast.makeText(this, permissions[i] + "权限被拒绝", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

}
