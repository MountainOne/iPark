package com.example.administrator.ipark.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.ipark.R;

public class My_fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
           View view = inflater.inflate(R.layout.fragment_my_fgt, container, false);
        return view;
    }


}
