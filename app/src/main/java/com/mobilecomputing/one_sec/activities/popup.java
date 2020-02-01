package com.mobilecomputing.one_sec.activities;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.mobilecomputing.one_sec.R;

/*
 * created by Avinash
 */

//Activity to confirm delete credential
public class popup extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_popup);
    }
}
