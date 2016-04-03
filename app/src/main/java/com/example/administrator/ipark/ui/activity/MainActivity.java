package com.example.administrator.ipark.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviPara;
import com.example.administrator.ipark.R;
import com.example.administrator.ipark.fragment.Main_fragment;
import com.example.administrator.ipark.fragment.My_fragment;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    //初始化fragment
    private Main_fragment main_fgt;
    private My_fragment my_fgt;
    private RadioButton mRbtn_squre,mRbtn_my,mRbtn_map;


    // 北京科技大学
    double mLat1 = 39.9961;
    double mLon1 = 116.36406;
    // 百度大厦坐标
    double mLat2 = 40.056858;
    double mLon2 = 116.308194;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        mRbtn_my = (RadioButton) findViewById(R.id.menu_my_radio_button);
        mRbtn_squre = (RadioButton) findViewById(R.id.menu_square_radio_button);
        mRbtn_map = (RadioButton) findViewById(R.id.menu_map_radio_button);
        mRbtn_map.setOnClickListener(this);
        mRbtn_my.setOnClickListener(this);
        mRbtn_squre.setOnClickListener(this);
        setDefaultFragment();

    }
    //设置默认fragment
    private void setDefaultFragment(){
        android.app.FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction ts = fm.beginTransaction();
         main_fgt = new Main_fragment();
        ts.replace(R.id.layout, main_fgt);
        ts.commit();
    }
    @Override
    public void onClick(View v) {

        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (v.getId()){

            case R.id.menu_map_radio_button:
                startNavi();
                break;
            case R.id.menu_my_radio_button:
                if(my_fgt == null) {
                    my_fgt = new My_fragment();
                }
                transaction.replace(R.id.layout, my_fgt);
                break;
            case R.id.menu_square_radio_button:
                if(main_fgt == null){
                    main_fgt = new Main_fragment();
                }
                transaction.replace(R.id.layout, main_fgt);
                break;
            default:
                break;
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void startNavi() {
        LatLng pt1 = new LatLng(mLat1, mLon1);
        LatLng pt2 = new LatLng(mLat2, mLon2);
        // 构建 导航参数
        NaviPara para = new NaviPara();
        para.startPoint = pt1;
        para.startName = "从这里开始";
        para.endPoint = pt2;
        para.endName = "到这里结束";

        try {

            BaiduMapNavigation.openBaiduMapNavi(para, this);

        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
            builder.setTitle("提示");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    BaiduMapNavigation.getLatestBaiduMapApp(MainActivity.this);
                }
            });

            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.create().show();
        }
    }
}
