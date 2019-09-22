package com.example.weather_forecast.activitys;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weather_forecast.R;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;

import java.util.ArrayList;
import java.util.List;

public class PickerCityActivity extends AppCompatActivity {
    private static final int MESSAGE_WHAT = 0x01;
    CityPickerView mPicker = new CityPickerView();
    private List<String> list = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ListView lvManageCity;
    private TextView tvLocationArea;
    private TextView tvPickerCity;
    private ImageView ivBack;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_WHAT) {
                operateData((ArrayList<String>) msg.obj);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker_city);
        mPicker.init(this);
        initViews();
        initData();
    }

    private void initViews() {
        lvManageCity = findViewById(R.id.lv_manage_city);
        tvLocationArea = findViewById(R.id.tv_location_area);
        tvPickerCity = findViewById(R.id.tv_picker_city);
        ivBack = findViewById(R.id.iv_back);
        tvLocationArea.setText("目前请求城市：" + getIntent().getStringExtra("substringDistrict"));
    }

    private void initData() {
        tvPickerCity.setOnClickListener((v) -> {
            //判断输入法的隐藏状态
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager.isActive()) {
                manager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                //调用CityPicker选取区域
                selectAddress();
            }
        });
        ivBack.setOnClickListener((v) -> onBackPressed());
    }

    private void operateData(ArrayList<String> list) {
        if (!list.isEmpty()) {
            //遍历完省市县的数据后，将数据显示到ListView上
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
            lvManageCity.setAdapter(adapter);
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_item);
            LayoutAnimationController controller = new LayoutAnimationController(animation);
            controller.setDelay(0.5f);
            controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
            lvManageCity.setLayoutAnimation(controller);
            //注意：显示的是（省+市+县）的字符串，但是通过intent传过去的是截取的字符串
            lvManageCity.setOnItemClickListener((parent, view, position, id) -> {
                String itemCityName = parent.getItemAtPosition(position).toString();
                String[] splitCityName = itemCityName.split(" ");
                //通过分割" "找到地区
                String districtName = splitCityName[2];
                String substringDistrictName = districtName.substring(0, districtName.length() - 1);
                //通过分割" "找到城市
                String cityName = splitCityName[1];
                String substringCityName = cityName.substring(0, cityName.length() - 1);
                //对地区进行验证
                if (!districtName.isEmpty()) {
                    if (districtName.endsWith("区") || districtName.endsWith("县") || districtName.endsWith("市")) {
                        if (districtName.length() == 2) {
                            sendIntent(districtName);
                        } else if (districtName.equals("市辖区")) {
                            sendIntent(substringCityName);
                        } else if (districtName.endsWith("旗") || districtName.endsWith("自治县")||districtName.endsWith("办事处")||districtName.length()>5) {
                            Toast.makeText(PickerCityActivity.this, "抱歉，无法查询到该地区信息", Toast.LENGTH_LONG).show();
                        } else {
                            sendIntent(substringDistrictName);
                        }
                    } else {
                        Toast.makeText(PickerCityActivity.this, "抱歉，无法查询到该地区信息", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (cityName.endsWith("市")) {
                        sendIntent(substringCityName);
                    } else {
                        Toast.makeText(PickerCityActivity.this, "抱歉，无法查询到该地区信息", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    /**
     * 拿到intent实例
     * 直接放置数据到intent或者通过bundle包装再放入intent
     * 通过setResult()方法将intent实例作为结果传递给原Activity
     * 调用本Activity的finish()方法
     */
    private void sendIntent(String substringName) {
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putString("districtName", substringName);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    //省市县的选择弹出框
    private void selectAddress() {
        CityConfig cityConfig = new CityConfig.Builder()
                .province("广东省")
                .city("梅州市")
                .district("梅江区")
                .visibleItemsCount(6)
                .provinceCyclic(false)
                .cityCyclic(false)
                .districtCyclic(false)
                .cancelText("取消")
                .confirmText("确定")
                .title("选择地区")
                .drawShadows(true)
                .titleBackgroundColor("#ccffffff")
                .showBackground(true)
                .build();
        mPicker.setConfig(cityConfig);
        mPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                if (district != null) {
                    Log.e("TAG", province.getName() + city.getName());
                    String listStr = province.getName() + " " + city.getName() + " " + district.getName() + "       【切换】";
                    //若两次选择的区域相同，则不添加进集合
                    if (!list.contains(listStr)) {
                        list.add(listStr);
                        Message message = Message.obtain();
                        message.what = MESSAGE_WHAT;
                        message.obj = list;
                        handler.sendMessage(message);
                    }
                } else {
                    Log.e("TAG", province.getName() + city.getName());
                    String listStr = province.getName() + " " + city.getName() + "      【切换】";
                    if (!list.contains(listStr)) {
                        list.add(listStr);
                        Message message = Message.obtain();
                        message.what = MESSAGE_WHAT;
                        message.obj = list;
                        handler.sendMessage(message);
                    }
                }
            }
        });
        mPicker.showCityPicker();
    }

}
