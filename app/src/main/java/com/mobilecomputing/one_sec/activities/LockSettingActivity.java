package com.mobilecomputing.one_sec.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobilecomputing.one_sec.R;
import com.mobilecomputing.one_sec.base.AppConstants;
import com.mobilecomputing.one_sec.base.BaseActivity;
import com.mobilecomputing.one_sec.model.LockAutoTime;
import com.mobilecomputing.one_sec.services.BackgroundManager;
import com.mobilecomputing.one_sec.services.LockService;
import com.mobilecomputing.one_sec.utils.SpUtil;
import com.mobilecomputing.one_sec.utils.SystemBarHelper;
import com.mobilecomputing.one_sec.utils.ToastUtil;
import com.mobilecomputing.one_sec.widget.SelectLockTimeDialog;

public class LockSettingActivity extends BaseActivity implements View.OnClickListener
        , DialogInterface.OnDismissListener, CompoundButton.OnCheckedChangeListener {

    public static final String ON_ITEM_CLICK_ACTION = "on_item_click_action";
    private static final int REQUEST_CHANGE_PWD = 3;

    private CheckBox cbLockSwitch;
    private CheckBox cbLockScreen;
    private CheckBox cbHidePattern;

    private TextView tvChangePwd;

    private LockSettingReceiver mLockSettingReceiver;
    private SelectLockTimeDialog dialog;
    private RelativeLayout mTopLayout;

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        cbLockSwitch = findViewById(R.id.checkbox_app_lock_on_off);
        cbLockScreen = findViewById(R.id.checkbox_lock_screen_switch_on_phone_lock);
        cbHidePattern = findViewById(R.id.checkbox_show_hide_pattern);

        tvChangePwd = findViewById(R.id.btn_change_pwd);
        mTopLayout = findViewById(R.id.top_layout);
    }

    @Override
    protected void initData() {
        mLockSettingReceiver = new LockSettingReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ON_ITEM_CLICK_ACTION);
        registerReceiver(mLockSettingReceiver, filter);
        dialog = new SelectLockTimeDialog(this, "");
        dialog.setOnDismissListener(this);
        boolean isLockOpen = SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE);
        cbLockSwitch.setChecked(isLockOpen);

        boolean isLockAutoScreen = SpUtil.getInstance().getBoolean(AppConstants.LOCK_AUTO_SCREEN, false);
        cbLockScreen.setChecked(isLockAutoScreen);

        boolean isTakePic = SpUtil.getInstance().getBoolean(AppConstants.LOCK_AUTO_RECORD_PIC, false);
    }

    @Override
    protected void initAction() {

        cbLockSwitch.setOnCheckedChangeListener(this);
        cbLockScreen.setOnCheckedChangeListener(this);
        cbHidePattern.setOnCheckedChangeListener(this);

        tvChangePwd.setOnClickListener(this);

    }

    @Override
    public void onClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.btn_change_pwd:
                Intent intent = new Intent(LockSettingActivity.this, GestureCreateActivity.class);
                startActivityForResult(intent, REQUEST_CHANGE_PWD);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean b) {
        switch (buttonView.getId()) {
            case R.id.checkbox_app_lock_on_off:
                SpUtil.getInstance().putBoolean(AppConstants.LOCK_STATE, b);
                if (b) {
                    BackgroundManager.getInstance().init(LockSettingActivity.this).stopService(LockService.class);
                    BackgroundManager.getInstance().init(LockSettingActivity.this).startService(LockService.class);

                    BackgroundManager.getInstance().init(LockSettingActivity.this).startAlarmManager();

                } else {
                    BackgroundManager.getInstance().init(LockSettingActivity.this).stopService(LockService.class);
                    BackgroundManager.getInstance().init(LockSettingActivity.this).stopAlarmManager();
                }
                break;
            case R.id.checkbox_lock_screen_switch_on_phone_lock:
                SpUtil.getInstance().putBoolean(AppConstants.LOCK_AUTO_SCREEN, b);
                break;
            case R.id.checkbox_show_hide_pattern:
                SpUtil.getInstance().putBoolean(AppConstants.LOCK_IS_HIDE_LINE, b);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CHANGE_PWD:
                    ToastUtil.showToast("Password reset succeeded");
                    break;
            }
        }
    }


    @Override
    public void onDismiss(DialogInterface dialogInterface) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mLockSettingReceiver);
    }

    private class LockSettingReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, @NonNull Intent intent) {
            String action = intent.getAction();
            if (action.equals(ON_ITEM_CLICK_ACTION)) {
                LockAutoTime info = intent.getParcelableExtra("info");
                boolean isLast = intent.getBooleanExtra("isLast", true);
                if (isLast) {
                    SpUtil.getInstance().putString(AppConstants.LOCK_APART_TITLE, info.getTitle());
                    SpUtil.getInstance().putLong(AppConstants.LOCK_APART_MILLISECONDS, 0L);
                    SpUtil.getInstance().putBoolean(AppConstants.LOCK_AUTO_SCREEN_TIME, false);
                } else {
                    SpUtil.getInstance().putString(AppConstants.LOCK_APART_TITLE, info.getTitle());
                    SpUtil.getInstance().putLong(AppConstants.LOCK_APART_MILLISECONDS, info.getTime());
                    SpUtil.getInstance().putBoolean(AppConstants.LOCK_AUTO_SCREEN_TIME, true);
                }
                dialog.dismiss();
            }
        }
    }

}
