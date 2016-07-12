package com.archer.truesure.user.login;

import android.os.AsyncTask;

/**
 * 具体的业务逻辑
 * Created by Administrator on 2016/7/12 0012.
 */
public class LoginPresenter {

    private LoginView loginView;

    public LoginPresenter(LoginView loginView) {
        this.loginView = loginView;
    }

    /**
     * 模拟登录
     */
    public void login() {
        new MyAsyncTask().execute();
    }

    private final class MyAsyncTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loginView.showProgress();
        }

        @Override
        protected Integer doInBackground(Void... params) {

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                return 0;
            }
            return 1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if (integer == 0) {
                loginView.hideProgress();
                loginView.showMessage("网络错误");
                return;
            }

            loginView.hideProgress();
            loginView.NavigationToHome();

        }
    }

}
