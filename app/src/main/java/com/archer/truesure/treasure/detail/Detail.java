package com.archer.truesure.treasure.detail;

import com.google.gson.annotations.SerializedName;

/**
 * Author: qixuefeng on 2016/7/21 0021.
 * E-mail: 377289596@qq.com
 */
public class Detail {

//    {
//        "PagerSize":3,
//            "currentPage":1,
//            " TreasureID ":165,
//    }

    @SerializedName("PagerSize")
    private int pagerSize;

    @SerializedName("currentPage")
    private int currentPage;

    @SerializedName("TreasureID")
    private int treasuewId;

    public Detail(int treasuewId) {
        this.pagerSize = 1;
        this.currentPage = 1;
        this.treasuewId = treasuewId;
    }
}
