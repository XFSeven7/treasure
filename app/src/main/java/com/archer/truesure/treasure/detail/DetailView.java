package com.archer.truesure.treasure.detail;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

/**
 * Author: qixuefeng on 2016/7/21 0021.
 * E-mail: 377289596@qq.com
 */
public interface DetailView extends MvpView {

    void showMessage(String msg);

    void setData(List<DetailResult> data);

}
