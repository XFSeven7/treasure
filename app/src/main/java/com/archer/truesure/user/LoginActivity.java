package com.archer.truesure.user;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.archer.truesure.R;
import com.archer.truesure.common.ActivityUtils;
import com.archer.truesure.common.RegexUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.et_Username)
    EditText etUsername;
    @Bind(R.id.et_Password)
    EditText etPassword;
    @Bind(R.id.tv_forgetPassword)
    TextView tvForgetPassword;
    @Bind(R.id.btn_Login)
    Button btnLogin;

    private ActivityUtils activityUtils;

    private String et_password;
    private String et_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils = new ActivityUtils(this);
        setContentView(R.layout.activity_login);
    }

    private static final String TAG = "LoginActivity";
    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
        Log.e(TAG, "onContentChanged: " );
        etUsername.addTextChangedListener(textWatcher);
        etPassword.addTextChangedListener(textWatcher);

    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.e(TAG, "afterTextChanged: " );
            et_password = etPassword.getText().toString();
            et_username = etUsername.getText().toString();

            boolean canLogin = TextUtils.isEmpty(et_username) || TextUtils.isEmpty(et_username);
            btnLogin.setEnabled(!canLogin);
        }
    };

    @OnClick(R.id.btn_Login)
    public void login(){

        if (RegexUtils.verifyUsername(et_username) != RegexUtils.VERIFY_SUCCESS) {
            Toast.makeText(LoginActivity.this, R.string.username_rules, Toast.LENGTH_SHORT).show();
            return;
        }

        if (RegexUtils.verifyPassword(et_password) != RegexUtils.VERIFY_SUCCESS) {
            Toast.makeText(LoginActivity.this, R.string.password_rules, Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: 2016/7/11 0011 登录后的事情

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
