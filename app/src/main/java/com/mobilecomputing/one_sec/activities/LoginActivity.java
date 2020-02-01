package com.mobilecomputing.one_sec.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilecomputing.one_sec.R;
import com.mobilecomputing.one_sec.db.AppDbHandler;
import com.mobilecomputing.one_sec.model.LoginInfo;
import com.mobilecomputing.one_sec.utils.SpUtil;

public class LoginActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    AppDbHandler dbHandler;
    Button buttonLogin;
    ImageView mainLogo;
    TextView textViewSignUp;
    int backButtonPressCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mainLogo = findViewById(R.id.logo_imgvw);
        setMainLogoAnimation(3000);
        editTextEmail = findViewById(R.id.edit_txt_email);
        editTextPassword = findViewById(R.id.edit_txt_password);
        buttonLogin = findViewById(R.id.login_btn);
        textViewSignUp = findViewById(R.id.sign_up);
        dbHandler = new AppDbHandler(this, null, null, 1);
        if (SpUtil.getInstance().getString("email", "-1").equals("-1")) {
            SpUtil.getInstance().clear();
        } else {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int flag = 0;
                setMainLogoAnimation(1000);
                if (editTextEmail.getText().toString().equals("")) {
                    editTextPassword.setError("Please enter email address");
                    flag = -1;
                }
                if (editTextPassword.getText().toString().equals("")) {
                    editTextPassword.setError("Please enter password");
                    flag = -1;
                }
                if (flag == 0) {
                    LoginInfo loginInfo = dbHandler.authenticateUser(editTextEmail.getText().toString(), editTextPassword.getText().toString());
                    if (loginInfo.getEmail() != null) {
                        SpUtil.getInstance().putString("name", loginInfo.getName());
                        SpUtil.getInstance().putString("dob", loginInfo.getDob());
                        SpUtil.getInstance().putString("mob_num", loginInfo.getMobNum());
                        SpUtil.getInstance().putString("email", loginInfo.getEmail());
                        Intent intent = new Intent(LoginActivity.this, FingerprintAuthentication.class);
                        startActivity(intent);
                    } else {
                        onShakeImage();
                    }
                }
            }
        });

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setMainLogoAnimation(int duration) {
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(duration);
        rotate.setInterpolator(new LinearInterpolator());
        mainLogo.startAnimation(rotate);
    }

    public void onShakeImage() {
        Animation shake;
        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.linear_interpolator);
        mainLogo.startAnimation(shake);
    }

    @Override
    public void onBackPressed() {
        if (backButtonPressCount >= 1) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Press the back button once again to exit.", Toast.LENGTH_SHORT).show();
            backButtonPressCount++;
        }
    }
}
