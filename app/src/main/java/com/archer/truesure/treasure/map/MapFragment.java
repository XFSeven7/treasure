package com.archer.truesure.treasure.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.archer.truesure.R;
import com.archer.truesure.common.ActivityUtils;
import com.archer.truesure.treasure.Area;
import com.archer.truesure.treasure.Treasure;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author: qixuefeng on 2016/7/18 0018.
 * E-mail: 377289596@qq.com
 */
public class MapFragment extends MvpFragment<MapMvpView, MapPresenter> implements MapMvpView {

    @Bind(R.id.map_frame)
    FrameLayout mapFrame;

    private MapView mapView;
    private BaiduMap baiduMap;

    private ActivityUtils activityUtils;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);
        activityUtils = new ActivityUtils(getActivity());
        return view;
    }

    @Override
    public MapPresenter createPresenter() {
        return new MapPresenter();
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

    private BaiduMap.OnMarkerClickListener markerClickListener = new BaiduMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            return false;
        }
    };


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
            myLocation = new LatLng(lat, lon);

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

    /**
     * 移动到我定位的位置
     */
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

//         对Marker的监听
        baiduMap.setOnMarkerClickListener(markerClickListener);
//         对地图状态进行监听
        baiduMap.setOnMapStatusChangeListener(mapStatusChangeListener);

    }

    private BaiduMap.OnMapStatusChangeListener mapStatusChangeListener = new BaiduMap.OnMapStatusChangeListener() {
        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus) {
        }

        @Override
        public void onMapStatusChange(MapStatus mapStatus) {
        }

        @Override
        public void onMapStatusChangeFinish(MapStatus mapStatus) {
            updateMapArea();
        }
    };

    private void updateMapArea() {

        MapStatus mapStatus = baiduMap.getMapStatus();

        double latitude = mapStatus.target.latitude;//纬度
        double longitude = mapStatus.target.longitude;//经度

        Area area = new Area();
        area.setMaxLat(Math.ceil(latitude));  // lat向上取整
        area.setMaxLng(Math.ceil(longitude));  // lng向上取速
        area.setMinLat(Math.floor(latitude));  // lat向下取整
        area.setMinLng(Math.floor(longitude));  // lng向下取整
        // 执行业务,根据Area去获取宝藏
        getPresenter().getTreasure(area);

    }


    /**
     * 地图缩放
     *
     * @param view
     */
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
    public void switchMapType() {
        int type = baiduMap.getMapType();
        type = type == BaiduMap.MAP_TYPE_NORMAL ? BaiduMap.MAP_TYPE_SATELLITE : BaiduMap.MAP_TYPE_NORMAL;
        baiduMap.setMapType(type);

    }

    @OnClick(R.id.tv_compass)
    public void switchCompass() {
        boolean isCompass = baiduMap.getUiSettings().isCompassEnabled();
        baiduMap.getUiSettings().setCompassEnabled(!isCompass);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }

    private static final String TAG = "MapFragment";

    @Override
    public void setData(List<Treasure> data) {

        for (int i = 0; i < data.size(); i++) {
            Log.i(TAG, "setData: " + data.get(i).getLongitude() + " " + data.get(i).getLocation());
            Log.i(TAG, "setData: " + data.get(i).getTitle());
            Log.i(TAG, "setData: " + data.get(i).getId());
            Log.i(TAG, "setData: " + data.get(i).getSize());
            Log.i(TAG, "setData: **********************");
        }

        BitmapDescriptor treasure_expanded = BitmapDescriptorFactory.fromResource(R.drawable.treasure_expanded);

        MarkerOptions markerOptions = new MarkerOptions()
                .icon(treasure_expanded)
                .position(myLocation);

        baiduMap.addOverlay(markerOptions);

//        addOverlays(data);
    }

    BitmapDescriptor treasure_dot = BitmapDescriptorFactory.fromResource(R.drawable.treasure_dot);
    BitmapDescriptor treasure_expanded = BitmapDescriptorFactory.fromResource(R.drawable.treasure_expanded);

//    private void addOverlays(List<Treasure> data) {
//
//        //测试覆盖物
//        MarkerOptions markerOptions = new MarkerOptions();
//
//        markerOptions.icon(treasure_dot);
//        markerOptions.position(myLocation);
//
//        List<OverlayOptions> optionses = new ArrayList<>();
//
//        for (Treasure treasure : data) {
//
//            LatLng latLng = new LatLng(treasure.getLatitude(), treasure.getLongitude());
//            MarkerOptions mo = new MarkerOptions();
//            mo.icon(treasure_dot);
//            mo.position(latLng);
//            optionses.add(mo);
//
//        }
//
//        baiduMap.addOverlays(optionses);
//
//    }

}
