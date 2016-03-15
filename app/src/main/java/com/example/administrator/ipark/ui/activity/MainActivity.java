package com.example.administrator.ipark.ui.activity;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;

import com.example.administrator.ipark.R;
import com.example.administrator.ipark.fragment.Main_fragment;
import com.example.administrator.ipark.fragment.My_fragment;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private Main_fragment main_fgt;
    private My_fragment my_fgt;
    private RadioButton mRbtn_squre,mRbtn_my;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        mRbtn_my = (RadioButton) findViewById(R.id.menu_my_radio_button);
        mRbtn_squre = (RadioButton) findViewById(R.id.menu_square_radio_button);
        mRbtn_my.setOnClickListener(this);
        mRbtn_squre.setOnClickListener(this);
        setDefaultFragment();

    }

    private void setDefaultFragment(){
        android.app.FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction ts = fm.beginTransaction();
         main_fgt = new Main_fragment();
        ts.replace(R.id.layout,main_fgt);
        ts.commit();
    }
    @Override
    public void onClick(View v) {

        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (v.getId()){
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
            default:
                break;
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
