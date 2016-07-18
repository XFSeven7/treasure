package com.archer.truesure.temp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.archer.truesure.R;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TempActivity extends AppCompatActivity {

    @Bind(R.id.map)
    MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_temp2);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

}
