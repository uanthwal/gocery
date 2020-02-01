package com.mobilecomputing.one_sec.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobilecomputing.one_sec.R;

public class AddCredential extends AppCompatActivity {

    private EditText inputName;
    private EditText inputUsername;
    private EditText inputPassword;
    private EditText inputWebsite;
    private EditText inputKey;
    private Button btnSaveCredentials;
    DatabaseHelper myDB;
    private Cryptography cryptography;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcredential_layout);

        cryptography = Cryptography.getInstance();

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        inputName = findViewById(R.id.inputName);
        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);
        inputWebsite = findViewById(R.id.inputWebsite);
        inputKey = findViewById(R.id.inputKey);
        btnSaveCredentials = findViewById(R.id.btnSaveCredentials);

        //update website with name
        inputName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputWebsite.setText(inputName.getText().toString()+".com");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        myDB = new DatabaseHelper(this);

        //save details
        btnSaveCredentials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (inputName.length() != 0 && inputUsername.length() != 0 && inputPassword.length() != 0) {
                    AddData(inputName.getText().toString(), inputUsername.getText().toString(), (inputPassword.getText().toString()) , inputWebsite.getText().toString(), inputKey.getText().toString());
                } else {
                    Toast.makeText(AddCredential.this, "Fields are empty", Toast.LENGTH_LONG).show();
                }

                Intent intent = new Intent(AddCredential.this, ViewCredentials.class);
                startActivity(intent);
                finish();

            }

        });

    }



    public void AddData(String name, String username, String password, String website, String secretkey) {
        boolean insertData = myDB.addData(name, username, password, website, secretkey);

        if (insertData) {
            Toast.makeText(AddCredential.this, "Login Credentials saved", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(AddCredential.this, "Data insertion error. Check log for details", Toast.LENGTH_LONG).show();
        }
    }


}



