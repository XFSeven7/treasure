package com.archer.truesure.treasure.hide;

import com.archer.truesure.net.NetOkHttpClient;
import com.archer.truesure.treasure.TreasureApi;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author: qixuefeng on 2016/7/21 0021.
 * E-mail: 377289596@qq.com
 */
public class HidePresenter extends MvpNullObjectBasePresenter<HideView> {

    private Call<HideTreasureResult> hideTreasureResultCall;

    public void hideTreasure(HideTreasure hideTreasure) {

        getView().showProgress();

        TreasureApi treasureApi = NetOkHttpClient.getInstance().getTreasureApi();

        if (hideTreasureResultCall != null) {
            hideTreasureResultCall.cancel();
        }

        hideTreasureResultCall = treasureApi.hideTreasure(hideTreasure);

        hideTreasureResultCall.enqueue(callback);

    }

    private Callback<HideTreasureResult> callback = new Callback<HideTreasureResult>() {
        @Override
        public void onResponse(Call<HideTreasureResult> call, Response<HideTreasureResult> response) {

            getView().hideProgress();

            if (response != null && response.isSuccessful()) {

                HideTreasureResult body = response.body();

                if (body == null) {
                    getView().showMessage("未知错误");
                    return;
                }

                getView().showMessage(body.getMsg());

                if(body.code == 1){
                    getView().navigateToHome();
                }

            }

        }

        @Override
        public void onFailure(Call<HideTreasureResult> call, Throwable t) {
            getView().hideProgress();
            getView().showMessage(t.getMessage());
        }
    };

    @Override public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (hideTreasureResultCall != null) {
            hideTreasureResultCall.cancel();
        }
    }

}
