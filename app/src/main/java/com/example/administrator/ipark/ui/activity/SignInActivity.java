package com.example.administrator.ipark.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ipark.ApplicationContext;
import com.example.administrator.ipark.R;
import com.example.administrator.ipark.util.NetworkStatus;
import com.example.administrator.ipark.util.StreamUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int  UNCONNECTED = -1;
    private static final int  SUCCESS = 1;
    private static final int ERROR = 0;
    private AutoCompleteTextView mPhoneNumber;
    private TextView mPassword;
    private static final String mPath = "/user/login.php";
    private ApplicationContext mAppContext;


    private Handler mHandler;
    {
        mHandler = new Handler() {
            @Override

            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UNCONNECTED:
                        Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                        break;
                    case SUCCESS:
                        Intent intent = new Intent(SignInActivity.this,MainActivity.class);
                        startActivity(intent);
                        break;
                    case ERROR:
                        Toast.makeText(getApplicationContext(),"用户名或密码错误", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        };
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
       // requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_sign_in);
        mAppContext = (ApplicationContext) getApplication();
        findViewById(R.id.sign_up_button).setOnClickListener(this);
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        mPhoneNumber = (AutoCompleteTextView) findViewById(R.id.phone_number);
        mPassword = (TextView) findViewById(R.id.password);
    }

    public void SignIn(){

        //重置错误提示
        mPhoneNumber.setError(null);
        mPassword.setError(null);

        //获取登录数据
        final String phoneNumber = mPhoneNumber.getText().toString();
        final String password = mPassword.getText().toString();

        //初始化获取焦点的视图
        boolean isValid = true;
        View focusView = null;

        //手机号验证
        if(!isPhoneNumber(phoneNumber)){

            mPhoneNumber.setError(getString(R.string.error_pattern_phoneNumber));
            focusView = mPhoneNumber;
            isValid = false;
        }

        //密码验证
        if(TextUtils.isEmpty(password)){
            mPassword.setError(getString(R.string.error_empty_password));
            focusView = mPassword;
            isValid = false;
        }else if(isPasswordValid(password)==false){
            mPassword.setError(getString(R.string.error_length_password));
            focusView = mPassword;
            isValid = false;
        }

        if(isValid == false) {

            focusView.requestFocus();
        }else if(NetworkStatus.isNetworkConnected(getApplicationContext())){

            new Thread(){
               @Override
                public void run(){
                    Log.e("TAG", "1");
                    JSONObject user = new JSONObject();
                   Log.e("TAG", "3");
                    try { Log.e("TAG", "2");
                        user.put("username", phoneNumber);
                        user.put("passwd", password);

                        String url = mAppContext.getUrl(mPath);
                        Log.e("TAG", "4");
                        doPostRequest(url, user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    private void doPostRequest(String path, JSONObject data){
        try {
            //获得传输的实体
            Log.e("COM", "1");
            byte[] entity = data.toString().getBytes("UTF-8");
            URL url = new URL(path);
            Log.e("COM", url.toString());
            //实例化一个 HTTP连接对象
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);//定义超时时间
            connection.setRequestMethod("POST");//使用POST方式发送请求
            connection.setUseCaches(false);//设置不允许使用缓存
            connection.setDoOutput(true);//允许对外输出
            connection.setDoInput(true);//允许对内输出
            connection.setRequestProperty("Content-Type", "application/json");//提交格式为json
            connection.setRequestProperty("Conten-length", String.valueOf(entity.length));
            OutputStream outputStream = connection.getOutputStream();//获得输出流
            outputStream.write(entity);
            outputStream.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                Log.e("COM", "2");
                //处理成功的请求
                InputStream inputStream = connection.getInputStream();
                byte[] is = StreamUtil.readInputStream(inputStream);
                String info = new String(is);
                Log.e("COM", info);
                if(info.equals("Login OK")){
                    Log.e("COM", "3");
                    //创建消息对象
                    Message msg = Message.obtain();
                    msg.what = SUCCESS;
                    mHandler.sendMessage(msg);
                }
               }else{
                Message msg = Message.obtain();
                msg.what = ERROR;
                mHandler.sendMessage(msg);

            }

        }catch(UnsupportedEncodingException e){//编码异常
            e.printStackTrace();
        }catch(MalformedURLException e){//路径异常
            e.printStackTrace();
        }catch(ProtocolException e){//协议类型异常
            e.printStackTrace();
        }catch(IOException e){//输入输出异常
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private boolean isPasswordValid(String password){
        return password.length() >= 6;
    }

    public boolean isPhoneNumber(String phoneNumber){
        for(int i = 0; i < phoneNumber.length(); i++){
            if(!Character.isDigit(phoneNumber.charAt(i))) {

                return false;
            }
        }
        if(phoneNumber.charAt(0) != '1'){
            return false;
        }else if(phoneNumber.length() != 11){
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_up_button:
                Intent intentUp = new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intentUp);
                break;
            case R.id.sign_in_button:
                SignIn();
//                Intent intentMain = new Intent(SignInActivity.this,MainActivity.class);
//                startActivity(intentMain);
                break;
        }
    }
}
