package com.archer.truesure.treasure.map;

import com.archer.truesure.net.NetOkHttpClient;
import com.archer.truesure.treasure.Area;
import com.archer.truesure.treasure.Treasure;
import com.archer.truesure.treasure.TreasureApi;
import com.archer.truesure.treasure.TreasureRepo;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author: qixuefeng on 2016/7/19 0019.
 * E-mail: 377289596@qq.com
 */
public class MapPresenter extends MvpNullObjectBasePresenter<MapMvpView> {

    private Call<List<Treasure>> treasureInArea;

    private Area area;

    public void getTreasure(Area area){

        if (TreasureRepo.getInstance().isCached(area)) {
            return;
        }

        this.area = area;

        TreasureApi treasureApi = NetOkHttpClient.getInstance().getTreasureApi();

        if (treasureInArea != null) {
            treasureInArea.cancel();

        }
        treasureInArea = treasureApi.getTreasureInArea(area);

        treasureInArea.enqueue(callBack);


    }

    private static final String TAG = "MapPresenter";

    private Callback<List<Treasure>> callBack = new Callback<List<Treasure>>() {
        @Override
        public void onResponse(Call<List<Treasure>> call, Response<List<Treasure>> response) {

            if (response != null && response.isSuccessful()) {

                List<Treasure> body = response.body();

                if (body == null) {
                    getView().showMessage("未知错误");
                    return;
                }

                // 缓存宝藏及区域
                TreasureRepo.getInstance().addTreasure(body);
                TreasureRepo.getInstance().cache(area);

                getView().setData(body);
                return;

            }

            getView().showMessage("未知错误");

        }

        @Override
        public void onFailure(Call<List<Treasure>> call, Throwable t) {
            getView().showMessage(t.getMessage());
        }

    };


}
