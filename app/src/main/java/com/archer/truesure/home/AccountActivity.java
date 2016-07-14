package com.archer.truesure.home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.archer.truesure.R;
import com.archer.truesure.common.ActivityUtils;
import com.archer.truesure.components.IconSelectWindow;
import com.pkmmte.view.CircularImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 用户中心
 */
public class AccountActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.iv_userIcon)
    CircularImageView ivUserIcon;
    @Bind(R.id.user_name)
    TextView userName;
    @Bind(R.id.linearLayout)
    RelativeLayout linearLayout;
    private ActivityUtils activityUtils;

    private IconSelectWindow iconSelectWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils = new ActivityUtils(this);
        setContentView(R.layout.activity_account);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private static final String TAG = "AccountActivity";
    @OnClick(R.id.iv_userIcon)
    public void userIcon() {

        if (iconSelectWindow == null) {
            iconSelectWindow = new IconSelectWindow(this, listener);
        }

//        if (iconSelectWindow.isShowing()) {
//            iconSelectWindow.dismiss();
//            Log.e(TAG, "userIcon: 1" );
//            return;
//        }

        Log.e(TAG, "userIcon: 2" );
        iconSelectWindow.show();

    }

    private IconSelectWindow.Listener listener = new IconSelectWindow.Listener() {
        @Override
        public void openCanmera() {
            activityUtils.showToast("ccc");
        }

        @Override
        public void openGallay() {
            activityUtils.showToast("ggg");
        }
    };


    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
