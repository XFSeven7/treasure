package com.archer.truesure.treasure.detail;

import com.archer.truesure.net.NetOkHttpClient;
import com.archer.truesure.treasure.TreasureApi;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author: qixuefeng on 2016/7/21 0021.
 * E-mail: 377289596@qq.com
 */
public class DetailPresenter extends MvpNullObjectBasePresenter<DetailView> {

    private Call<List<DetailResult>> treasureCall;

    public void detail(Detail detail) {

        TreasureApi treasureApi = NetOkHttpClient.getInstance().getTreasureApi();

        if (treasureCall != null) {
            treasureCall.cancel();
        }

        treasureCall = treasureApi.getTreasure(detail);

        treasureCall.enqueue(callBack);

    }

    private Callback<List<DetailResult>> callBack = new Callback<List<DetailResult>>() {
        @Override
        public void onResponse(Call<List<DetailResult>> call, Response<List<DetailResult>> response) {

            if (response != null && response.isSuccessful()) {

                List<DetailResult> body = response.body();

                if (body == null) {
                    getView().showMessage("未知错误");
                    return;
                }

                getView().setData(body);

            }

        }

        @Override
        public void onFailure(Call<List<DetailResult>> call, Throwable t) {
            getView().showMessage(t.getMessage());
        }
    };


}
