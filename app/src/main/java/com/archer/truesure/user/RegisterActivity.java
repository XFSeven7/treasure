package com.archer.truesure.user;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.archer.truesure.R;
import com.archer.truesure.common.RegexUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);



        etUsername.addTextChangedListener(mTextWatcher);
        etPassword.addTextChangedListener(mTextWatcher);
        etConfirm.addTextChangedListener(mTextWatcher);

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
            Toast.makeText(RegisterActivity.this, R.string.username_rules, Toast.LENGTH_SHORT).show();
            return;
        }

        if (RegexUtils.verifyPassword(et_password) != RegexUtils.VERIFY_SUCCESS) {
            Toast.makeText(RegisterActivity.this, R.string.password_rules, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!et_password.equals(et_confirm)) {
            Toast.makeText(RegisterActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
        }

        // TODO: 2016/7/11 0011 注册

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
