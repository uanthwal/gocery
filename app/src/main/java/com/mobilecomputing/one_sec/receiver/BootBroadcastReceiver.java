package com.mobilecomputing.one_sec.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;

import com.mobilecomputing.one_sec.base.AppConstants;
import com.mobilecomputing.one_sec.services.BackgroundManager;
import com.mobilecomputing.one_sec.services.LoadAppListService;
import com.mobilecomputing.one_sec.services.LockService;
import com.mobilecomputing.one_sec.utils.LogUtil;
import com.mobilecomputing.one_sec.utils.SpUtil;


public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(@NonNull Context context, Intent intent) {
        LogUtil.i("Boot service....");


        BackgroundManager.getInstance().init(context).startService(LoadAppListService.class);
        if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE, false)) {
            BackgroundManager.getInstance().init(context).startService(LockService.class);
            BackgroundManager.getInstance().init(context).startAlarmManager();
        }
    }
}
