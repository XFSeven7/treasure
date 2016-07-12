package com.archer.truesure;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.archer.truesure.common.ActivityUtils;
import com.archer.truesure.user.login.LoginActivity;
import com.archer.truesure.user.RegisterActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private ActivityUtils activityUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityUtils = new ActivityUtils(this);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_Register,R.id.btn_Login})
    public void click(View view){

        switch (view.getId()) {

            case R.id.btn_Register:
                activityUtils.startActivity(RegisterActivity.class);
                break;
            case R.id.btn_Login:
                activityUtils.startActivity(LoginActivity.class);
                break;
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
