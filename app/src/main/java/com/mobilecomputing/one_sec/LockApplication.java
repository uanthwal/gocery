package com.mobilecomputing.one_sec;

import com.gu.toolargetool.TooLargeTool;
import com.mobilecomputing.one_sec.activities.GestureUnlockActivity;
import com.mobilecomputing.one_sec.base.BaseActivity;
import com.mobilecomputing.one_sec.utils.SpUtil;

import org.litepal.LitePalApplication;

import java.util.ArrayList;
import java.util.List;



public class LockApplication extends LitePalApplication {

    private static LockApplication application;
    private static List<BaseActivity> activityList;

    public static LockApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;




        SpUtil.getInstance().init(application);
        activityList = new ArrayList<>();
    }

    public void doForCreate(BaseActivity activity) {
        activityList.add(activity);
    }

    public void doForFinish(BaseActivity activity) {
        activityList.remove(activity);
    }

    public void clearAllActivity() {
        try {
            for (BaseActivity activity : activityList) {
                if (activity != null && !clearAllWhiteList(activity))
                    activity.clear();
            }
            activityList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean clearAllWhiteList(BaseActivity activity) {
        return activity instanceof GestureUnlockActivity;
    }
}
