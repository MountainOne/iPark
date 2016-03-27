package com.example.administrator.ipark;

/**
 * Created by Administrator on 2016/2/5.
 */
public class ApplicationContext extends android.app.Application{
    private static String baseUrl = "http://forest.picp.net:22973";

    public String getUrl(String path){
        return baseUrl+path;
    }

    public String getUrl() { return baseUrl;}
}
