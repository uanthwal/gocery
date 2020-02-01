package com.mobilecomputing.one_sec.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mobilecomputing.one_sec.R;
import com.mobilecomputing.one_sec.utils.SpUtil;

public class ProfileActivity extends AppCompatActivity {
    TextView textViewName, textViewEmail, textViewDob, textViewMobNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        textViewName = findViewById(R.id.user_name);
        textViewEmail = findViewById(R.id.mail_txt);
        textViewDob = findViewById(R.id.dob_txt);
        textViewMobNo = findViewById(R.id.mob_num_txt);

        String userName = SpUtil.getInstance().getString("name");
        String email = SpUtil.getInstance().getString("email");
        String mobNum = SpUtil.getInstance().getString("mob_num");
        String dob = SpUtil.getInstance().getString("dob");

        textViewName.setText(userName);
        textViewEmail.setText(email);
        textViewDob.setText(dob);
        textViewMobNo.setText(mobNum);

        FloatingActionButton fab = findViewById(R.id.fab_edit_profile);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Profile edit feature will be available in next release", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }
}
