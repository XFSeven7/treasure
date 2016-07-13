package com.archer.truesure.user.login;

import android.os.AsyncTask;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

/**
 * 具体的业务逻辑
 * Created by Administrator on 2016/7/12 0012.
 */
public class LoginPresenter extends MvpNullObjectBasePresenter<LoginView> {

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
            getView().showProgress();
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
                getView().hideProgress();
                getView().showMessage("网络错误");
                return;
            }

            getView().hideProgress();
            getView().NavigationToHome();

        }
    }

}
