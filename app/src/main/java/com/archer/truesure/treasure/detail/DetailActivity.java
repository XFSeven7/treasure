package com.archer.truesure.treasure.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.archer.truesure.R;
import com.archer.truesure.components.TreasureView;
import com.archer.truesure.treasure.Treasure;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    private static final String KEY_TREASURE = "key_treasure";
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.frameLayout)
    FrameLayout frameLayout;
    @Bind(R.id.treasureView)
    TreasureView treasureView;
    @Bind(R.id.tv_detail_description)
    TextView tvDetailDescription;

    private Treasure treasure;

    private BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.treasure_expanded);

    public static void open(@NonNull Context context, @NonNull Treasure treasure) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(KEY_TREASURE, treasure);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasure_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        treasure = (Treasure) intent.getSerializableExtra(KEY_TREASURE);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(treasure.getTitle());
            treasureView.bindTreasure(treasure);
        }

        initMap();

    }

    private void initMap() {

        BaiduMapOptions options = new BaiduMapOptions();
        options.compassEnabled(false);//
        options.overlookingGesturesEnabled(false);//
        options.rotateGesturesEnabled(false);//
        options.scaleControlEnabled(false);//
        options.zoomGesturesEnabled(false);//
        options.zoomControlsEnabled(false);//

        LatLng latLng = new LatLng(treasure.getLatitude(), treasure.getLongitude());

        MapStatus mapStatus = new MapStatus.Builder()
                .zoom(18)
                .target(latLng)
                .overlook(-20)
                .build();

        options.mapStatus(mapStatus);


        MapView mapView = new MapView(this, options);

        frameLayout.addView(mapView);

        BaiduMap baiduMap = mapView.getMap();

        MarkerOptions options1 = new MarkerOptions();
        options1.icon(icon);
        options1.position(latLng);

        baiduMap.addOverlay(options1);

    }
}
