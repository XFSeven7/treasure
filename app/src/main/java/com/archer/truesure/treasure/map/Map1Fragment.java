package com.archer.truesure.treasure.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.archer.truesure.treasure.detail.DetailActivity;
import com.archer.truesure.treasure.hide.HideActivity;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
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

    @Bind(R.id.et_treasureTitle)
    EditText et_treasureTitle;

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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public MapPresenter createPresenter() {
        return new MapPresenter();
    }

    private String address;
    private GeoCoder geoCoder;
    public static final int UI_MODE_NORMAL = 0;// 普通
    public static final int UI_MODE_SELECT = 1;// 选中
    public static final int UI_MODE_HIDE = 2; // 埋藏
    private int uiMode = UI_MODE_NORMAL;
    private static LatLng myLocation;

    private BaiduMap baiduMap;

    private BitmapDescriptor dot = BitmapDescriptorFactory.fromResource(R.drawable.treasure_dot);
    private BitmapDescriptor expanded = BitmapDescriptorFactory.fromResource(R.drawable.treasure_expanded);

    //****************************以下为该类布局中控件的点击事件*************************************

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

        baiduMap.animateMapStatus(update);

    }

    @OnClick({R.id.iv_toTreasureInfo, R.id.btn_HideHere})
    public void hideTreasure() {
        activityUtils.hideSoftKeyboard();
        String string = et_treasureTitle.getText().toString();
        if (TextUtils.isEmpty(string)) {
            activityUtils.showToast("请输入宝藏标题");
            return;
        }
        MapStatus mapStatus = baiduMap.getMapStatus();
        LatLng target = mapStatus.target;
        HideActivity.open(getActivity(), string, address, target, 0);
    }

    /**
     * 按下宝藏信息展示卡片将进入宝藏详情页
     */
    @OnClick(R.id.treasureView)
    public void clickTreasureView() {

        int id = currentMarker.getExtraInfo().getInt("id");
        Treasure treasure = TreasureRepo.getInstance().getTreasure(id);

        DetailActivity.open(getActivity(), treasure);
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

    // 地图缩放操作
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

    //****************************以下为该类的自定义方法*************************************

    /**
     * 初始化地图
     *
     * @see Map1Fragment#onViewCreated(View, Bundle)
     */
    private void initBaiduMap() {

        // 状态
        MapStatus mapStatus = new MapStatus.Builder()
                .zoom(15)//3~21
                .overlook(0) // (0) - (-45)
                .build();
        // 设置
        BaiduMapOptions options = new BaiduMapOptions()
                .mapStatus(mapStatus) // 地图相关状态
                .compassEnabled(true) // 指南针
                .zoomGesturesEnabled(true) // 设置是否允许缩放手势
                .rotateGesturesEnabled(true) // 设置是否允许旋转手势，默认允许
                .scrollGesturesEnabled(true) // 设置是否允许拖拽手势，默认允许
                .scaleControlEnabled(false) // 设置是否显示比例尺控件
                .overlookingGesturesEnabled(false) // 设置是否允许俯视手势，默认允许
                .zoomControlsEnabled(false) // 设置是否显示缩放控件
                ;

        MapView mapView = new MapView(getActivity(), options);

        mapFrame.addView(mapView, 0);
        baiduMap = mapView.getMap();

        baiduMap.setOnMarkerClickListener(onMarkerClickListener);
        baiduMap.setOnMapStatusChangeListener(onMapStatusChangeListener);

    }

    /**
     * 定位
     *
     * @see Map1Fragment#onViewCreated(View, Bundle)
     */
    private void initLocation() {

        baiduMap.setMyLocationEnabled(true);
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
        client.requestLocation();

    }

    /**
     * 地理编码：将屏幕中间的定位点所定的位置，获取当前位置
     *
     * @see Map1Fragment#onViewCreated(View, Bundle)
     */
    private void initGeoCoder() {
        geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(onGetGeoCoderResultListener);
    }

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

    /**
     * 转化为普通视图
     */
    private void hideAll() {
        centerLayout.setVisibility(View.GONE);
        treasureView.setVisibility(View.GONE);
        layoutBottom.setVisibility(View.GONE);
        hide_treasure.setVisibility(View.GONE);
    }

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


    /**
     * 网络操作，获取当前位置的1个经纬度的范围
     */
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
     * 按下back时来调用
     */
    public boolean clickBackPressed() {
        if (this.uiMode != UI_MODE_NORMAL) {
            changeMode(UI_MODE_NORMAL);
            return false;
        }
        // 当前为普通浏览模式的话，返回true，表明当前fragment不要做其他操作,你可以退出
        return true;
    }

    public static LatLng getMyLocation() {
        return myLocation;
    }

    //****************************以下为实现MapMvpView接口的方法*************************************

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

    //****************************以下均为事件监听***************************************************

    /**
     * 地理编译与反编译的事件监听
     */
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

    /**
     * 地图位置的监听事件
     * 获取当前位置
     */
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

            animMoveToMyLocation();

        }
    };

    /**
     * 地图拖动状态改变的监听
     */
    private BaiduMap.OnMapStatusChangeListener onMapStatusChangeListener = new BaiduMap.OnMapStatusChangeListener() {
        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus) {

        }

        @Override
        public void onMapStatusChange(MapStatus mapStatus) {

        }

        @Override
        public void onMapStatusChangeFinish(MapStatus mapStatus) {
            updateMapArea();
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
    /**
     * 覆盖物的点击后的监听事件
     */
    private BaiduMap.OnMarkerClickListener onMarkerClickListener = new BaiduMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {

            if (currentMarker != null) {
                currentMarker.setIcon(dot);
                currentMarker.setVisible(true);
            }

            currentMarker = marker;

            Log.e(TAG, "onMarkerClick: " + marker.getTitle());

            currentMarker.setVisible(false);
            currentMarker.setIcon(expanded);

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

    /**
     * 地图覆盖物的变形成为定位图的监听事件
     */
    private InfoWindow.OnInfoWindowClickListener onInfoWindowClickListener = new InfoWindow.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick() {
            currentMarker.setIcon(dot);
            baiduMap.hideInfoWindow();
            currentMarker.setVisible(true);
            changeMode(UI_MODE_NORMAL);
        }
    };
/*
    warning: push.default is unset; its implicit value has changed in
    Git 2.0 from 'matching' to 'simple'. To squelch this message
    and maintain the traditional behavior, use:

    git config --global push.default matching

    To squelch this message and adopt the new behavior now, use:

    git config --global push.default simple

    When push.default is set to 'matching', git will push local branches
    to the remote branches that already exist with the same name.

    Since Git 2.0, Git defaults to the more conservative 'simple'
    behavior, which only pushes the current branch to the corresponding
    remote branch that 'git pull' uses to update the current branch.

    See 'git help config' and search for 'push.default' for further information.
            (the 'simple' mode was introduced in Git 1.7.11. Use the similar mode
    'current' instead of 'simple' if you sometimes use older versions of Git)

    fatal: You are not currently on a branch.
    To push the history leading to the current (detached HEAD)
    state now, use

    git push origin HEAD:<name-of-remote-branch>


*/
}