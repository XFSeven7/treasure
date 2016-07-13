package com.archer.truesure.user.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.archer.truesure.HomeActivity;
import com.archer.truesure.MainActivity;
import com.archer.truesure.R;
import com.archer.truesure.common.ActivityUtils;
import com.archer.truesure.common.RegexUtils;
import com.archer.truesure.components.AlertDialogFragment;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends MvpActivity<LoginView, LoginPresenter> implements LoginView {

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
        Log.e(TAG, "onContentChanged: ");

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getTitle());
        }

        //给ET添加监听
        etUsername.addTextChangedListener(textWatcher);
        etPassword.addTextChangedListener(textWatcher);

    }

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
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

            Log.e(TAG, "afterTextChanged: ");
            et_password = etPassword.getText().toString();
            et_username = etUsername.getText().toString();

            boolean canLogin = TextUtils.isEmpty(et_username) || TextUtils.isEmpty(et_username);
            btnLogin.setEnabled(!canLogin);

        }
    };

    @OnClick(R.id.btn_Login)
    public void login() {

        if (RegexUtils.verifyUsername(et_username) != RegexUtils.VERIFY_SUCCESS) {
            showUsernameError();
            return;
        }

        if (RegexUtils.verifyPassword(et_password) != RegexUtils.VERIFY_SUCCESS) {
            showPasswordError();
            return;
        }

        // TODO: 2016/7/11 0011 登录后的事情
        getPresenter().login();
        activityUtils.hideSoftKeyboard();

    }

    private void showUsernameError() {
        AlertDialogFragment fragment = AlertDialogFragment.newsIntance(R.string.username_error, R.string.username_rules);
        fragment.show(getSupportFragmentManager(), "showUsernameError");
        etUsername.setText("");

    }

    private void showPasswordError() {
        AlertDialogFragment fragment = AlertDialogFragment.newsIntance(R.string.password_error, R.string.password_rules);
        fragment.show(getSupportFragmentManager(), "showPasswordError");
        etPassword.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    //********************************LoginView接口所实现的方法**************************************

    private ProgressDialog progressDialog;

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(this, "", "正在登录，请稍等...");
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }

    @Override
    public void NavigationToHome() {
        activityUtils.startActivity(HomeActivity.class);
        finish();
        Intent intent = new Intent(MainActivity.ACTION_ENTER_HOME);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
