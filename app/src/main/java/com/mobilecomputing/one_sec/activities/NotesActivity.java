package com.mobilecomputing.one_sec.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mobilecomputing.one_sec.R;

public class NotesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credit_card_activity);
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
