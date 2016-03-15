package com.example.administrator.ipark.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

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

public class east_park extends FragmentActivity implements View.OnClickListener {

    Button mCar1,mCar2;
    private static final int SHOW_RESPONSE = 1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SHOW_RESPONSE :
                    if(msg.arg1 == 1){
                        mCar1.setBackgroundResource(R.color.colorOrange);
                    }
                    if (msg.arg2 == 1){
                        mCar2.setBackgroundResource(R.color.colorOrange);
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
        getServerResponse();
        mCar1 = (Button) findViewById(R.id.car1);
        mCar2 = (Button) findViewById(R.id.car2);
        mCar1.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.car1:
                mCar1.setBackgroundResource(R.color.colorOrange);
                break;
            default:
                break;
        }
    }

    private void getServerResponse(){
        //开启线程来发送网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection connection = null;
                try{
                    URL url = new URL("http://forest.picp.net:22973/index.php");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);


                    InputStream in = connection.getInputStream();
                    //下面获取到的输入流进行读取
                    byte[] is = StreamUtil.readInputStream(in);
                    String json = new String(is);
                    //解析返回的JSON对象
                    JSONObject jsonObject = new JSONObject(json);
                    int car_1 = jsonObject.getInt("1");
                    int car_2 = jsonObject.getInt("2");
                    Log.d("Car2",car_2 + "");
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//                    StringBuilder response = new StringBuilder();
//                    String line;
//                    while ((line = reader.readLine()) != null){
//                        response.append(line);
//                    }

                    Message message = new Message();
                    message.what = SHOW_RESPONSE;

                    //服务器将返回的结果存放到Message中
                    message.arg1 = car_1;
                    message.arg2 = car_2;
                    Log.d("Car1",car_1 + "");
                    handler.sendMessage(message);
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

}
