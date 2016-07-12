package com.archer.truesure.user.register;

import android.os.AsyncTask;

/**
 * 注册界面的业务逻辑
 * Created by Administrator on 2016/7/12 0012.
 */
public class RegisterPresenter {

    private RegisterView registerView;

    public RegisterPresenter(RegisterView registerView) {
        this.registerView = registerView;
    }

    /**
     * 模拟注册
     */
    public void register() {
        new MyAsyncTask().execute();
    }

    private final class MyAsyncTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            registerView.showProgress();
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
                registerView.hideProgress();
                registerView.showMessage("网络错误");
                return;
            }

            registerView.hideProgress();
            registerView.NavigationToHome();

        }
    }
    
}
