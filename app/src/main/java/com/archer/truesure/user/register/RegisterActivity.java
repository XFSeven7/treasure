package com.archer.truesure.user.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.archer.truesure.HomeActivity;
import com.archer.truesure.MainActivity;
import com.archer.truesure.R;
import com.archer.truesure.common.ActivityUtils;
import com.archer.truesure.common.RegexUtils;
import com.archer.truesure.components.AlertDialogFragment;
import com.archer.truesure.user.entity.RegisterInfo;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 注册视图
 */
public class RegisterActivity extends MvpActivity<RegisterView, RegisterPresenter> implements RegisterView {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.et_Username)
    EditText etUsername;
    @Bind(R.id.et_Password)
    EditText etPassword;
    @Bind(R.id.et_Confirm)
    EditText etConfirm;
    @Bind(R.id.btn_Register)
    Button btnRegister;

    private String et_username;
    private String et_password;
    private String et_confirm;

    private ActivityUtils activityUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils = new ActivityUtils(this);
        setContentView(R.layout.activity_register);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getTitle());
        }

        etUsername.addTextChangedListener(mTextWatcher);
        etPassword.addTextChangedListener(mTextWatcher);
        etConfirm.addTextChangedListener(mTextWatcher);

    }

    @NonNull
    @Override
    public RegisterPresenter createPresenter() {
        return new RegisterPresenter();
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

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            et_username = etUsername.getText().toString();
            et_password = etPassword.getText().toString();
            et_confirm = etConfirm.getText().toString();

            if (TextUtils.isEmpty(et_username)) {
                btnRegister.setEnabled(false);
            } else {
                btnRegister.setEnabled(true);
            }
        }
    };



    @OnClick(R.id.btn_Register)
    public void register() {

        if (RegexUtils.verifyUsername(et_username) != RegexUtils.VERIFY_SUCCESS) {
            showUsernameError();
            return;
        }

        if (RegexUtils.verifyPassword(et_password) != RegexUtils.VERIFY_SUCCESS) {
            showPasswordError();
            return;
        }

        if (!et_password.equals(et_confirm)) {
            Toast.makeText(RegisterActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            etPassword.setText("");
            etConfirm.setText("");
            return;
        }

        // TODO: 2016/7/11 0011 注册
        getPresenter().register(new RegisterInfo(et_username, et_password));
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
        etConfirm.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    //********************************RegisterView接口所实现的方法***********************************

    private ProgressDialog progressDialog;

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(this, "", "正在注册，请稍等...");
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

    @Override
    public void addRegister() {

    }

}
