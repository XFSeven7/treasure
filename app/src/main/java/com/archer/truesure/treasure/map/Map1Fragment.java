package com.archer.truesure.treasure.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.archer.truesure.R;
import com.archer.truesure.common.ActivityUtils;
import com.archer.truesure.components.TreasureView;
import com.archer.truesure.treasure.Area;
import com.archer.truesure.treasure.Treasure;
import com.archer.truesure.treasure.TreasureRepo;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author: qixuefeng on 2016/7/20 0020.
 * E-mail: 377289596@qq.com
 */
public class Map1Fragment extends MvpFragment<MapMvpView, MapPresenter> implements MapMvpView {

    private static final String TAG = "Map1Fragment";

    @Bind(R.id.map_frame)
    FrameLayout mapFrame;
    @Bind(R.id.centerLayout)
    RelativeLayout centerLayout;
    @Bind(R.id.treasureView)
    TreasureView treasureView;
    @Bind(R.id.layout_bottom)
    FrameLayout layoutBottom;

    @Bind(R.id.hide_treasure)
    RelativeLayout hide_treasure;

    @Bind(R.id.iv_located)
    ImageView iv_located;

    /**
     * 在埋藏宝藏时，下方卡片上显示的埋藏位置
     */
    @Bind(R.id.tv_currentLocation)
    TextView tvCurrentLication;

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        initBaiduMap();
        initLocation();
        initGeoCoder();

    }

    @Override
    public MapPresenter createPresenter() {
        return new MapPresenter();
    }

    private String address;

    private GeoCoder geoCoder;

    /**
     * 地理编码：将屏幕中间的定位点所定的位置，获取当前位置
     */
    private void initGeoCoder() {

        geoCoder = GeoCoder.newInstance();

        geoCoder.setOnGetGeoCodeResultListener(onGetGeoCoderResultListener);

    }


    private OnGetGeoCoderResultListener onGetGeoCoderResultListener = new OnGetGeoCoderResultListener() {

        /**
         * 地理编码查询结果回调函数
         * // 地理编码 (地址 -> 经纬度)
         * @param geoCodeResult
         */
        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

//            String address = geoCodeResult.getAddress();
//            LatLng location = geoCodeResult.getLocation();
//
//            Log.e(TAG, "onGetGeoCodeResult: " + address);
//            Log.e(TAG, "onGetGeoCodeResult: " + location);

        }

        /**
         * 反地理编码查询结果回调函数
         * // 反地理编码 (经纬度 -> 地址)
         * @param reverseGeoCodeResult
         */
        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
            if (reverseGeoCodeResult == null) return;
            if (reverseGeoCodeResult.error == SearchResult.ERRORNO.NO_ERROR) {
                address = "未知";
            }
            address = reverseGeoCodeResult.getAddress();
            tvCurrentLication.setText("当前位置：" + address);
        }
    };

    public static final int UI_MODE_NORMAL = 0;// 普通
    public static final int UI_MODE_SELECT = 1;// 选中
    public static final int UI_MODE_HIDE = 2; // 埋藏

    private int uiMode = UI_MODE_NORMAL;

    /**
     * 修改显示模式
     *
     * @param mode
     */
    public void changeMode(int mode) {

        hideAll();

//        centerLayout  藏宝
//        treasureView  查看宝藏
//        layoutBottom
//        hide_treasure 藏宝标题设置

        switch (mode) {

            case UI_MODE_NORMAL:
                uiMode = UI_MODE_NORMAL;
                break;

            case UI_MODE_SELECT:
                uiMode = UI_MODE_SELECT;
                layoutBottom.setVisibility(View.VISIBLE);
                treasureView.setVisibility(View.VISIBLE);
                break;

            case UI_MODE_HIDE:
                uiMode = UI_MODE_HIDE;
                layoutBottom.setVisibility(View.VISIBLE);
                hide_treasure.setVisibility(View.VISIBLE);
                centerLayout.setVisibility(View.VISIBLE);
                break;

        }

    }

    private void hideAll() {
        centerLayout.setVisibility(View.GONE);
        treasureView.setVisibility(View.GONE);
        layoutBottom.setVisibility(View.GONE);
        hide_treasure.setVisibility(View.GONE);
    }

    /**
     * 定位
     */
    private void initLocation() {

        LocationClientOption locationClientOption = new LocationClientOption();
        //设置每隔多少秒扫描一次
//        locationClientOption.setScanSpan(60000);
        //设置坐标模式
        locationClientOption.setCoorType("bd09ll");
        //打开GPS
        locationClientOption.setOpenGps(true);

        LocationClient client = new LocationClient(getActivity(), locationClientOption);

        client.registerLocationListener(bdLocationListener);

        client.start();
//        client.requestLocation();

    }

    private static LatLng myLocation;

    private BDLocationListener bdLocationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

            myLocation = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());

            MyLocationData data = new MyLocationData.Builder()
                    .accuracy(100f)
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude())
                    .build();

            baiduMap.setMyLocationData(data);

            updateMapArea();

            animMoveToMyLocation();


        }
    };

    private void updateMapArea() {
        // 先得到你当前所在位置
        MapStatus mapStatus = baiduMap.getMapStatus();
        double lng = mapStatus.target.longitude;
        double lat = mapStatus.target.latitude;

        // 计算出你的Area  23.999  15.130
        //              24,23  ,  16,15去确定Area
        Area area = new Area();
        area.setMaxLat(Math.ceil(lat));  // lat向上取整
        area.setMaxLng(Math.ceil(lng));  // lng向上取速
        area.setMinLat(Math.floor(lat));  // lat向下取整
        area.setMinLng(Math.floor(lng));  // lng向下取整
        // 执行业务,根据Area去获取宝藏
        getPresenter().getTreasure(area);

    }

    /**
     * 动态滑动到我的当前位置
     */
    @OnClick(R.id.tv_located)
    public void animMoveToMyLocation() {

        MapStatus status = new MapStatus.Builder()
                .target(myLocation)
                .zoom(18)
                .build();

        MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(status);

        baiduMap.setMyLocationEnabled(true);

        baiduMap.animateMapStatus(update);

    }

    private BaiduMap baiduMap;

    /**
     * 按下宝藏信息展示卡片将进入宝藏详情页
     */
    @OnClick(R.id.treasureView)
    public void clickTreasureView() {
        activityUtils.hideSoftKeyboard();
        activityUtils.showToast("gogogo");
    }

    /**
     * 初始化地图
     */
    private void initBaiduMap() {

//        BaiduMapOptions options = new BaiduMapOptions();

//        MapStatus mapStatus = new MapStatus.Builder()
//                .rotate(0)
//                .zoom(15)
//                .build();
//
//        options.mapStatus(mapStatus);

//        mapView = new MapView(getActivity(), options);

        MapView mapView = new MapView(getActivity());

        mapFrame.addView(mapView, 0);
        baiduMap = mapView.getMap();

        baiduMap.setOnMarkerClickListener(onMarkerClickListener);
        baiduMap.setOnMapStatusChangeListener(onMapStatusChangeListener);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.tv_satellite, R.id.tv_compass})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_satellite:
                int mapType = baiduMap.getMapType();
                mapType = mapType == BaiduMap.MAP_TYPE_NORMAL ? BaiduMap.MAP_TYPE_SATELLITE : BaiduMap.MAP_TYPE_NORMAL;
                baiduMap.setMapType(mapType);
                break;
            case R.id.tv_compass:
                Log.i(TAG, "onClick: ");
                boolean compassEnabled = baiduMap.getUiSettings().isCompassEnabled();
                baiduMap.getUiSettings().setCompassEnabled(!compassEnabled);
                break;
        }
    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }

    @Override
    public void setData(List<Treasure> data) {

        for (Treasure treasure : data) {
            LatLng latLng = new LatLng(treasure.getLatitude(), treasure.getLongitude());
            addOverMark(latLng, treasure.getId());
            Log.e(TAG, "setData: " + treasure.getTitle());
        }

    }

    private BitmapDescriptor dot = BitmapDescriptorFactory.fromResource(R.drawable.treasure_dot);
    private BitmapDescriptor expanded = BitmapDescriptorFactory.fromResource(R.drawable.treasure_expanded);

    private void addOverMark(LatLng latLng, final int treasureId) {

        MarkerOptions options = new MarkerOptions();

        options.position(latLng);
        options.icon(dot);
        options.anchor(0.5f, 0.5f);

        Bundle bundle = new Bundle();
        bundle.putInt("id", treasureId);
        options.extraInfo(bundle);

        baiduMap.addOverlay(options);

    }

    private BaiduMap.OnMapStatusChangeListener onMapStatusChangeListener = new BaiduMap.OnMapStatusChangeListener() {
        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus) {

        }

        @Override
        public void onMapStatusChange(MapStatus mapStatus) {

        }

        @Override
        public void onMapStatusChangeFinish(MapStatus mapStatus) {
            Log.i(TAG, "onMapStatusChangeFinish: ");
            if (uiMode == UI_MODE_HIDE) {
                // 反弹动画
//                YoYo.with(Techniques.Bounce).duration(1000).playOn(hide_treasure);
                YoYo.with(Techniques.Bounce).duration(1000).playOn(iv_located);
                // 淡入动画
//                YoYo.with(Techniques.FadeIn).duration(1000).playOn(hide_treasure);

                LatLng target = mapStatus.target;

//                if (currentMarker.getPosition() != target) {

                    if (uiMode == UI_MODE_HIDE) {

                        ReverseGeoCodeOption option = new ReverseGeoCodeOption();
                        option.location(target);

                        geoCoder.reverseGeoCode(option);

                    }

//                }

            }

        }
    };


    private Marker currentMarker;
    private BaiduMap.OnMarkerClickListener onMarkerClickListener = new BaiduMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {

            if (currentMarker != null) {
                currentMarker.setVisible(true);
            }

            currentMarker = marker;

            Log.e(TAG, "onMarkerClick: " + marker.getTitle());

            marker.setVisible(false);
            marker.setIcon(expanded);

            InfoWindow infoWindow = new InfoWindow(expanded, marker.getPosition(), 0, onInfoWindowClickListener);

            baiduMap.showInfoWindow(infoWindow);

            // 从当前Marker中取出这个宝藏的id号(@see addMarker时的操作)
            int id = marker.getExtraInfo().getInt("id");
            // 从宝藏仓库中，根据id号取出宝藏
            Treasure treasure = TreasureRepo.getInstance().getTreasure(id);
            treasureView.bindTreasure(treasure);

            changeMode(UI_MODE_SELECT);

            return false;

        }

    };

    private InfoWindow.OnInfoWindowClickListener onInfoWindowClickListener = new InfoWindow.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick() {
            currentMarker.setIcon(dot);
            baiduMap.hideInfoWindow();
            currentMarker.setVisible(true);
            changeMode(UI_MODE_NORMAL);
        }
    };


    public static LatLng getMyLocation() {
        return myLocation;
    }
}