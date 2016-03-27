package com.example.administrator.ipark.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2015/10/25.
 */
public class NetworkStatus {

    public static boolean isNetworkConnected(Context context){
        if(context != null){
            ConnectivityManager mConnectivityMannager =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo  mNetworkInfo = mConnectivityMannager.getActiveNetworkInfo();
            if(mNetworkInfo != null){
                return  mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
