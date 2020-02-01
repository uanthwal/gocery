package com.mobilecomputing.one_sec.mvp.contract;

import android.content.Context;

import com.mobilecomputing.one_sec.base.BasePresenter;
import com.mobilecomputing.one_sec.base.BaseView;
import com.mobilecomputing.one_sec.model.CommLockInfo;
import com.mobilecomputing.one_sec.mvp.p.LockMainPresenter;

import java.util.List;

public interface LockMainContract {
    interface View extends BaseView<Presenter> {

        void loadAppInfoSuccess(List<CommLockInfo> list);
    }

    interface Presenter extends BasePresenter {
        void loadAppInfo(Context context);

        void searchAppInfo(String search, LockMainPresenter.ISearchResultListener listener);

        void onDestroy();
    }
}
