package com.example.administrator.ipark.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.administrator.ipark.R;
import com.example.administrator.ipark.ui.activity.west_park;

/**
 * Created by Administrator on 2016/2/22.
 */
public class Main_fragment extends Fragment implements View.OnClickListener {

    private Button mBtn_west, mBtn_east, mBtn_south ;
    private Context context = getActivity();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_squre,container,false);
        mBtn_west = (Button) view.findViewById(R.id.west_park);
        mBtn_west.setOnClickListener(this);
        mBtn_east = (Button) view.findViewById(R.id.east_park);
        mBtn_east.setOnClickListener(this);
        mBtn_south = (Button) view.findViewById(R.id.south_park);
        mBtn_south.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
      switch (v.getId()){
          case R.id.west_park:
              Intent intent_w = new Intent("com.example.iPark.START_WEST");
             startActivity(intent_w);
              break;
          case R.id.east_park:
              Intent intnt_e = new Intent("com.example.iPark.START_EAST");
              startActivity(intnt_e);
              break;
          case R.id.south_park:
              Intent intnt_s = new Intent("com.example.iPark.START_SOUTH");
              startActivity(intnt_s);
              break;
          default:
              break;
      }
    }
}
