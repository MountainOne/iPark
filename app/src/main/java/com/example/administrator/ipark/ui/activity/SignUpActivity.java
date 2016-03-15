package com.example.administrator.ipark.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.example.administrator.ipark.R;

public class SignUpActivity extends FragmentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_up);

        findViewById(R.id.sign_in_button).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_in_button:
                Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(intent);
                break;
        }
    }
}
