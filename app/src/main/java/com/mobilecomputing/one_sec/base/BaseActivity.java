package com.mobilecomputing.one_sec.base;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mobilecomputing.one_sec.LockApplication;
import com.mobilecomputing.one_sec.R;

public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar mToolbarloadAppInfoSuccess;
    private TextView mCustomTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LockApplication.getInstance().doForCreate(this);
        setContentView(getLayoutId());

        initViews(savedInstanceState);

        initData();
        initAction();
    }

    public abstract int getLayoutId();

    protected abstract void initViews(Bundle savedInstanceState);

    public void hiddenActionBar() {
        getSupportActionBar().hide();
    }

    protected abstract void initData();

    protected abstract void initAction();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LockApplication.getInstance().doForFinish(this);
    }

    public final void clear() {
        super.finish();
    }
}
