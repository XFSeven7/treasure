package com.archer.truesure.home.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.archer.truesure.R;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author: qixuefeng on 2016/7/18 0018.
 * E-mail: 377289596@qq.com
 */
public class MapFragment extends Fragment {

    @Bind(R.id.map_frame)
    FrameLayout mapFrame;

    private MapView mapView;
    private BaiduMap baiduMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initBaiduMap();
        initLocation();
    }

    private LocationClient locationClient;
    private LatLng myLocation;

    private void initLocation() {

        //激活定位图层
        baiduMap.setMyLocationEnabled(true);
        //定位实例化
        locationClient = new LocationClient(getActivity().getApplicationContext());
        //进行一些定位的一般常规性设置
        LocationClientOption option = new LocationClientOption();

        option.setOpenGps(true);
        option.setScanSpan(60000);
        option.setCoorType("bd09ll");
        locationClient.setLocOption(option);

        locationClient.registerLocationListener(locationListener);

        locationClient.start();
        locationClient.requestLocation();

    }

    //定位监听
    private BDLocationListener locationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

            if (bdLocation == null) {
                locationClient.requestLocation();
                return;
            }

            double lon = bdLocation.getLongitude();//经度
            double lat = bdLocation.getLatitude();//纬度
            myLocation = new LatLng(lat,lon);
            MyLocationData myLocationData = new MyLocationData.Builder()
                    .longitude(lon)
                    .latitude(lat)
                    .accuracy(100f)
                    .build();

            baiduMap.setMyLocationData(myLocationData);
            //移动到我的位置上去
            animateMoveToMyLocation();

        }
    };

    @OnClick(R.id.tv_located)
    public void animateMoveToMyLocation() {

        MapStatus mapStatus = new MapStatus.Builder()
                .target(myLocation)
                .rotate(0)
                .zoom(19)
                .build();

        MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(mapStatus);
        baiduMap.animateMapStatus(update);

    }

    /**
     * 初始化百度地图
     */
    private void initBaiduMap() {

        MapStatus mapStatus = new MapStatus.Builder()
                .zoom(15)
                .overlook(-20)
                .build();

        BaiduMapOptions options = new BaiduMapOptions()
                .mapStatus(mapStatus)
                .zoomControlsEnabled(false);

        mapView = new MapView(getActivity(), options);

        baiduMap = mapView.getMap();

        mapFrame.addView(mapView, 0);

    }

    @OnClick({R.id.iv_scaleDown, R.id.iv_scaleUp})
    public void scaleMap(View view) {

        switch (view.getId()) {

            case R.id.iv_scaleUp:
                baiduMap.setMapStatus(MapStatusUpdateFactory.zoomIn());
                break;

            case R.id.iv_scaleDown:
                baiduMap.setMapStatus(MapStatusUpdateFactory.zoomOut());
                break;
        }

    }

    @OnClick(R.id.tv_satellite)
    public void switchMapType(){

        int type = baiduMap.getMapType();
        type = type == BaiduMap.MAP_TYPE_NORMAL ? BaiduMap.MAP_TYPE_SATELLITE : BaiduMap.MAP_TYPE_NORMAL;
        baiduMap.setMapType(type);

    }

    @OnClick(R.id.tv_compass)
    public void switchCompass(){
        boolean isCompass = baiduMap.getUiSettings().isCompassEnabled();
        baiduMap.getUiSettings().setCompassEnabled(!isCompass);
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
