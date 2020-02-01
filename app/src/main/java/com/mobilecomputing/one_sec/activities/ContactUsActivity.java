package com.mobilecomputing.one_sec.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mobilecomputing.one_sec.R;

public class ContactUsActivity extends AppCompatActivity {
    Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        submitBtn = findViewById(R.id.submit_contact_form);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Feedback submitted successfully", Toast.LENGTH_LONG).show();
            }
        });
    }
}