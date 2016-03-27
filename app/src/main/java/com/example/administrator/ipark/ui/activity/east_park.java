package com.example.administrator.ipark.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.ipark.ApplicationContext;
import com.example.administrator.ipark.R;
import com.example.administrator.ipark.util.StreamUtil;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class east_park extends FragmentActivity implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeLayout;
    private ImageView mCar1,mCar2,mCar3,mCar4;
    private static final int SHOW_RESPONSE = 1;
    private static final String mPath = "/index.php";
    //使用全局变量
    private ApplicationContext mAppContext;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
//            Log.d("Test","1234");
            Bundle bundle = msg.getData();
            switch (msg.what){
                case SHOW_RESPONSE :
                    if(bundle.getInt("car1") == 1){
                        mCar1.setVisibility(View.VISIBLE);
                    }
                    if (bundle.getInt("car2") == 1){
                        mCar2.setVisibility(View.VISIBLE);
                    }
                    if (bundle.getInt("car3") == 1){
                        mCar3.setVisibility(View.VISIBLE);
                    }
                    if (bundle.getInt("car4") == 1){
                        mCar4.setVisibility(View.VISIBLE);
                    }
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_east_park);
        mAppContext = (ApplicationContext) getApplication();
        getServerResponse();

        mCar1 = (ImageView) findViewById(R.id.Car1);
        mCar2 = (ImageView) findViewById(R.id.Car2);
        mCar3 = (ImageView) findViewById(R.id.Car3);
        mCar4 = (ImageView) findViewById(R.id.Car4);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.id_swipe_ly);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }



    private void getServerResponse(){
        //开启线程来发送网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection connection = null;
                try{
                    URL url = new URL(mAppContext.getUrl(mPath));
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);


                    InputStream in = connection.getInputStream();
//                    //下面获取到的输入流进行读取
                    byte[] is = StreamUtil.readInputStream(in);
                    String json = new String(is);
                    //解析返回的JSON对象
                    JSONObject jsonObject = new JSONObject(json);
                    Message message = new Message();
                    message.what = SHOW_RESPONSE;

                    //服务器将返回的结果存放到Message中

                    Bundle bundle = new Bundle();
                    bundle.putInt("car1", jsonObject.getInt("1"));
                    bundle.putInt("car2", jsonObject.getInt("2"));
                    bundle.putInt("car3", jsonObject.getInt("3"));
                    bundle.putInt("car4", jsonObject.getInt("4"));
                    message.setData(bundle);

                    handler.sendMessage(message);
                    in.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if(connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    @Override
    public void onRefresh() {
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                getServerResponse();
                mSwipeLayout.setRefreshing(false);
                Toast.makeText(east_park.this,"刷新成功",Toast.LENGTH_SHORT).show();
            }
        }, 4000);
    }
}
