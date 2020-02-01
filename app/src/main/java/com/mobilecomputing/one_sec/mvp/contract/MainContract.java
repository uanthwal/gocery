package com.mobilecomputing.one_sec.mvp.contract;

import android.content.Context;


import com.mobilecomputing.one_sec.base.BasePresenter;
import com.mobilecomputing.one_sec.base.BaseView;
import com.mobilecomputing.one_sec.model.CommLockInfo;

import java.util.List;


public interface MainContract {
    interface View extends BaseView<Presenter> {
        void loadAppInfoSuccess(List<CommLockInfo> list);
    }

    interface Presenter extends BasePresenter {
        void loadAppInfo(Context context, boolean isSort);

        void loadLockAppInfo(Context context);

        void onDestroy();
    }
}
