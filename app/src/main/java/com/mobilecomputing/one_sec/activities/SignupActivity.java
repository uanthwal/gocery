package com.mobilecomputing.one_sec.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mobilecomputing.one_sec.R;
import com.mobilecomputing.one_sec.db.AppDbHandler;
import com.mobilecomputing.one_sec.model.LoginInfo;

public class SignupActivity extends AppCompatActivity {

    EditText editTextName, editTextPass, editTextConfirmPass, editTextMobNum, editTextEmail;
    Button buttonSignUp;
    AppDbHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextName = findViewById(R.id.edit_txt_name);
        editTextPass = findViewById(R.id.edit_txt_password);
        editTextConfirmPass = findViewById(R.id.edit_txt_cnfrm_password);
        editTextName = findViewById(R.id.edit_txt_name);
        editTextMobNum = findViewById(R.id.edit_txt_mob_num);
        editTextEmail = findViewById(R.id.edit_txt_email);
        buttonSignUp = findViewById(R.id.sign_up_btn);
        dbHandler = new AppDbHandler(this, null, null, 1);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag = 0;

                if (editTextName.getText().toString().equals("")) {
                    editTextName.setError("Please enter name");
                    flag = -1;
                }
                if (editTextEmail.getText().toString().equals("")) {
                    editTextEmail.setError("Please enter email address");
                    flag = -1;
                }
                if (editTextPass.getText().toString().equals("")) {
                    editTextPass.setError("Please enter password");
                    flag = -1;
                }
                if (editTextMobNum.getText().toString().equals("")) {
                    editTextMobNum.setError("Please enter mobile number");
                    flag = -1;
                }
                if (editTextConfirmPass.getText().toString().equals("")) {
                    editTextConfirmPass.setError("Please confirm password");
                    flag = -1;
                }
                if (editTextConfirmPass.getText().toString().equals("")) {
                    editTextConfirmPass.setError("Please enter password");
                    flag = -1;
                }
                if (!editTextPass.getText().toString().equals(editTextConfirmPass.getText().toString())) {
                    editTextConfirmPass.setError("Confirm Password not matching");
                    flag = -1;
                }

                if (flag == -1)
                    return;

                flag = dbHandler.checkIfUserExists(editTextEmail.getText().toString());
                if (flag == -1) {
                    LoginInfo login = new LoginInfo(editTextName.getText().toString(), editTextEmail.getText().toString(), editTextPass.getText().toString(), editTextMobNum.getText().toString(), "20-Nov-1994");
                    dbHandler.addNewUser(login);
                    Toast.makeText(getApplicationContext(), "User Created", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "User Already Exists", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
