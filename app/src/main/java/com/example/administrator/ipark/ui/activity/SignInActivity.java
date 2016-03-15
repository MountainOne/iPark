package com.example.administrator.ipark.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.administrator.ipark.R;

public class SignInActivity extends FragmentActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_in);

        findViewById(R.id.sign_up_button).setOnClickListener(this);
        findViewById(R.id.sign_in_button).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_up_button:
                Intent intentUp = new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intentUp);
                break;
            case R.id.sign_in_button:
                Intent intentMain = new Intent(SignInActivity.this,MainActivity.class);
                startActivity(intentMain);
                break;
        }
    }
}
