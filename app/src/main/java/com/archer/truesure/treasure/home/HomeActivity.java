package com.archer.truesure.treasure.home;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.archer.truesure.R;
import com.archer.truesure.common.ActivityUtils;
import com.archer.truesure.treasure.map.Map1Fragment;
import com.archer.truesure.user.UserPres;
import com.archer.truesure.user.account.AccountActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.nav_view)
    NavigationView navView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private FragmentManager fragmentManager;
    private Map1Fragment map1Fragment;

    private ActivityUtils activityUtils;
    private ImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils = new ActivityUtils(this);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        String url = UserPres.getString(UserPres.HEAD_PIC_URL);
        if (url != null) {
            ImageLoader.getInstance().displayImage(UserPres.getString(UserPres.HEAD_PIC_URL), icon);
        }

    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        navView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        icon = (ImageView) navView.getHeaderView(0).findViewById(R.id.iv_userIcon);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityUtils.startActivity(AccountActivity.class);
            }
        });

        fragmentManager = getSupportFragmentManager();
        map1Fragment = (Map1Fragment) fragmentManager.findFragmentById(R.id.fragment_map);

    }

    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {

            switch (item.getItemId()) {

                case R.id.menu_item_hide:
                    map1Fragment.changeMode(Map1Fragment.UI_MODE_HIDE);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.menu_item_my_list:
                    activityUtils.showToast("我的列表");
                    break;
                case R.id.menu_item_help:
                    activityUtils.showToast("帮助");
                    break;
                case R.id.menu_item_logout:
                    activityUtils.showToast("退出");
                    break;

            }

            return false;
        }
    };

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        ButterKnife.unbind(this);
    }
}
