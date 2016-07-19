package com.archer.truesure.treasure.map;

import com.archer.truesure.treasure.Treasure;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

/**
 * Author: qixuefeng on 2016/7/19 0019.
 * E-mail: 377289596@qq.com
 */
public interface MapMvpView extends MvpView {

    void showMessage(String msg);

    void setData(List<Treasure> data);

}
