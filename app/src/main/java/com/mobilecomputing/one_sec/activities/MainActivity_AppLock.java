package com.mobilecomputing.one_sec.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mobilecomputing.one_sec.R;
import com.mobilecomputing.one_sec.adapters.AppListAdapter;
import com.mobilecomputing.one_sec.base.AppConstants;
import com.mobilecomputing.one_sec.base.BaseActivity;
import com.mobilecomputing.one_sec.model.CommLockInfo;
import com.mobilecomputing.one_sec.mvp.contract.LockMainContract;
import com.mobilecomputing.one_sec.mvp.p.LockMainPresenter;
import com.mobilecomputing.one_sec.services.BackgroundManager;
import com.mobilecomputing.one_sec.services.LockService;
import com.mobilecomputing.one_sec.utils.SystemBarHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity_AppLock extends BaseActivity implements LockMainContract.View, View.OnClickListener {

    private static final int RESULT_ACTION_IGNORE_BATTERY_OPTIMIZATION = 351;
    private RelativeLayout mTopLayout;
    private ImageView mBtnSetting;
    private LockMainPresenter mLockMainPresenter;
    AppListAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main_1;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mBtnSetting = findViewById(R.id.btn_setting);
        mTopLayout = findViewById(R.id.top_layout);


        mLockMainPresenter = new LockMainPresenter(this, this);
        mLockMainPresenter.loadAppInfo(this);
    }

    @Override
    protected void initData() {

        PowerManager powerManager = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (powerManager != null && !powerManager.isIgnoringBatteryOptimizations(AppConstants.APP_PACKAGE_NAME)) {
                @SuppressLint("BatteryLife")
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + AppConstants.APP_PACKAGE_NAME));
                startActivity(intent);
            }
        }
        if (!BackgroundManager.getInstance().init(this).isServiceRunning(LockService.class)) {
            BackgroundManager.getInstance().init(this).startService(LockService.class);
        }
        BackgroundManager.getInstance().init(this).startAlarmManager();
    }


    @Override
    protected void initAction() {
        mBtnSetting.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_ACTION_IGNORE_BATTERY_OPTIMIZATION) {

        }
    }

    @Override
    public void loadAppInfoSuccess(@NonNull List<CommLockInfo> list) {
        List<CommLockInfo> userInstalledApps = new ArrayList<>();
        for (CommLockInfo info : list) {
            if (!info.isSysApp()) {
                userInstalledApps.add(info);
            }
        }
        RecyclerView recyclerView = findViewById(R.id.app_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AppListAdapter(this);
        adapter.setLockInfos(userInstalledApps);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.btn_setting:
                startActivity(new Intent(this, LockSettingActivity.class));
                break;

        }
    }
}
