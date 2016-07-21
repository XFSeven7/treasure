package com.archer.truesure.treasure;

import com.archer.truesure.treasure.detail.Detail;
import com.archer.truesure.treasure.detail.DetailResult;
import com.archer.truesure.treasure.hide.HideTreasure;
import com.archer.truesure.treasure.hide.HideTreasureResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Author: qixuefeng on 2016/7/19 0019.
 * E-mail: 377289596@qq.com
 */
public interface TreasureApi {

    @POST("/Handler/TreasureHandler.ashx?action=show")
    Call<List<Treasure>> getTreasureInArea(@Body Area area);

    @POST("/Handler/TreasureHandler.ashx?action=hide")
    Call<HideTreasureResult> hideTreasure(@Body HideTreasure hideTreasure);

    @POST("/Handler/TreasureHandler.ashx?action=tdetails")
    Call<List<DetailResult>> getTreasure(@Body Detail detail);

}
