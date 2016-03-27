package com.example.administrator.ipark.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.ipark.ApplicationContext;
import com.example.administrator.ipark.R;
import com.example.administrator.ipark.util.NetworkStatus;
import com.example.administrator.ipark.util.StreamUtil;
import com.example.administrator.ipark.util.StringUtil;

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

public class SignUpActivity extends FragmentActivity implements View.OnClickListener {

    //使用全局变量
    private ApplicationContext mAppContext;
    private static final String mPath = "/user/signup.php";

    private static final int SUCCESS = 1;
    private static final int ERROR = 0;

    //声明成员变量
    private AutoCompleteTextView mPhoneNumber;
    private EditText mNickname;
    private EditText mPassword;
    private EditText mConfirmPassword;

    private Handler handler;

    {
        handler = new Handler() {
            @Override

            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SUCCESS:
                        Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_LONG).show();
                        break;
                    case ERROR:
                        Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_LONG).show();
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //获取对全局变量的引用
        mAppContext = (ApplicationContext) getApplication();
        setContentView(R.layout.activity_sign_up);

        //获取对UI组件的引用
        mPhoneNumber = (AutoCompleteTextView) findViewById(R.id.phone_number);
        mNickname = (EditText) findViewById(R.id.nickname);
        mPassword = (EditText) findViewById(R.id.password);
        mConfirmPassword = (EditText) findViewById(R.id.confirmPassword);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_up_button).setOnClickListener(this);
    }

    public void SignUp() {
        //重置错误提示
        mPhoneNumber.setError(null);
        mNickname.setError(null);
        mPassword.setError(null);
        mConfirmPassword.setError(null);

        //获取注册数据
        final String phone_number = mPhoneNumber.getText().toString().trim();
        final String nickname = mNickname.getText().toString().trim();
        final String password = mPassword.getText().toString().trim();
        String confirmpassword = mConfirmPassword.getText().toString().trim();

        //初始化获取焦点的视图
        boolean isValid = true;
        View focusView = null;

        //手机号验证
        if (isPhoneNumber(phone_number)) {
            mPhoneNumber.setError(getString(R.string.error_empty_phone_number));
            focusView = mPhoneNumber;
            isValid = false;
        }

        //昵称验证
        if (TextUtils.isEmpty(nickname)) {
            mNickname.setError(getString(R.string.error_empty_nickname));
            focusView = mNickname;
            isValid = false;
        }


        //密码验证
        if (TextUtils.isEmpty(password)) {
            mPassword.setError(getString(R.string.error_empty_password));
            focusView = mPassword;
            isValid = false;
        } else if (isPasswordValid(password) == false) {
            mPassword.setError(getString(R.string.error_length_password));
            focusView = mPassword;
            isValid = false;
        }

        //确认密码验证
        if (TextUtils.isEmpty(confirmpassword)) {
            mConfirmPassword.setError(getString(R.string.error_empty_password));
            focusView = mConfirmPassword;
            isValid = false;
        } else if (confirmpassword.equals(password) == false) {
            mConfirmPassword.setError(getString(R.string.error_confirm_password));
            focusView = mConfirmPassword;
            isValid = false;
        } else if (isPasswordValid(confirmpassword) == false) {
            mConfirmPassword.setError(getString(R.string.error_length_password));
            focusView = mConfirmPassword;
            isValid = false;
        }


        if (!isValid) {
            focusView.requestFocus();
        } else {
            //检查网络连接
            if (NetworkStatus.isNetworkConnected(getApplicationContext())) {
                //TODO:发送网络请求
            } else {//网络不可用
                Toast.makeText(getApplicationContext(), getString(R.string.unavailable_network_connection), Toast.LENGTH_LONG).show();

            }
            new Thread() {
                @Override
                public void run() {

                    try {

                        JSONObject user = new JSONObject();
                        user.put("passwd", password);
                        user.put("phone_number", phone_number);

                        //从全局类获取访问路径
                        String url = mAppContext.getUrl(mPath);
                        doPostRequest(url, user);  //发送POST请求
                    } catch (JSONException e) {//捕获异常
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    public boolean isPhoneNumber(String phoneNumber) {
        for (int i = 0; i < phoneNumber.length(); i++) {
            if (!Character.isDigit(phoneNumber.charAt(i))) {

                return false;
            }
        }
        if (phoneNumber.charAt(0) != '1') {
            return false;
        } else if (phoneNumber.length() != 11) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                break;
            case R.id.sign_up_button:
                 SignUp();
                break;
            default:
                break;
        }
    }
    private void doPostRequest(String path, JSONObject data){
        try {
            //获得传输的实体
            byte[] entity = data.toString().getBytes("UTF-8");
            URL url = new URL(path);
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
                InputStream inputStream = connection.getInputStream();
                byte[] is = StreamUtil.readInputStream(inputStream);
                String info = new String(is);

                if(info.equals("Insert OK")){
                    Message msg = Message.obtain();
                    msg.what = SUCCESS;
                    handler.sendMessage(msg);
                }else if(info.equals("Insert Failed") || info.equals("Username used")){
                    Message msg = Message.obtain();
                    msg.what = ERROR;
                    handler.sendMessage(msg);
                }

            }else{
                Message msg = Message.obtain();
                msg.what = ERROR;
                handler.sendMessage(msg);

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
}
