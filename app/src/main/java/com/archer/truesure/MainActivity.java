package com.archer.truesure;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.archer.truesure.common.ActivityUtils;
import com.archer.truesure.user.login.LoginActivity;
import com.archer.truesure.user.register.RegisterActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private ActivityUtils activityUtils;

    public static final String ACTION_ENTER_HOME = "action.enter.home";

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityUtils = new ActivityUtils(this);
        ButterKnife.bind(this);

        IntentFilter intentFilter = new IntentFilter(ACTION_ENTER_HOME);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,intentFilter);

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
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }
}
