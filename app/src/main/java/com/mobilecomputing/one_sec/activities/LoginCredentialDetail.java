package com.mobilecomputing.one_sec.activities;

/*
 * created by Avinash
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.method.KeyListener;
import android.transition.Explode;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.mobilecomputing.one_sec.R;
import com.squareup.picasso.Picasso;

import org.jboss.aerogear.security.otp.Totp;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Random;

public class LoginCredentialDetail extends AppCompatActivity implements Serializable {
    private EditText txtValueName;
    private EditText txtValueUsername;
    private EditText txtValuePassword;
    private EditText txtValueWebsite;
    private EditText txtValue2FAKey;
    private ImageView imgFavicon;

    private MaterialButton btnUpdateCredentials;
    private ImageView btnGeneratePassword;
    private TextView txtLengthValue;
    private TextView txt2FATimer;
    private TextView txt2FACode;
    private TextView txtGeneratedPassword;
    private SeekBar seekPasswordLength;
    private Button btnUsePassword;
    Dialog deleteDialog;

    TextInputLayout txtName;
    TextInputLayout txtUsername;
    TextInputLayout txtPassword;
    TextInputLayout txtWebsite;
    TextInputLayout txt2FAKey;

    Drawable originalDrawable;
    KeyListener originalListener;
    DatabaseHelper myDB;
    boolean first = true;

    private Cryptography cryptography;
    private int itemID;
    Handler handler2FA;

    Button btnDeleteConfirm;
    Button btnDeleteCancel;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logindetails_layout);
        setTitle("Login");
        initAnimation();

        deleteDialog = new Dialog(LoginCredentialDetail.this);
        deleteDialog.setContentView(R.layout.delete_popup);
        btnDeleteConfirm = deleteDialog.findViewById(R.id.btnDeleteConfirm);
        btnDeleteCancel = deleteDialog.findViewById(R.id.btnDeleteCancel);

        //delete credential from database
        btnDeleteConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDB.deleteCredential(txtValueName.getText().toString());
                Toast.makeText(getApplicationContext(), "Login deleted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), ViewCredentials.class);
                startActivity(intent);
                finish();
            }
        });

        btnDeleteCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog.hide();
            }
        });


        cryptography = Cryptography.getInstance();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //map variables to items in layout
        txtName = findViewById(R.id.txtName);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        txtWebsite = findViewById(R.id.txtWebsite);
        txt2FAKey = findViewById(R.id.txt2FAKey);

        txtValueName = findViewById(R.id.txtValueName);
        txtValueUsername = findViewById(R.id.txtValueUsername);
        txtValuePassword = findViewById(R.id.txtValuePassword);
        txtValueWebsite = findViewById(R.id.txtValueWebsite);
        txtValue2FAKey = findViewById(R.id.txtValue2FAKey);
        txt2FATimer = findViewById(R.id.txt2FATimer);
        txt2FACode = findViewById(R.id.txt2FACode);
        btnUpdateCredentials = findViewById(R.id.btnUpdateCredentials);
        imgFavicon = findViewById(R.id.imgFavicon);
        btnGeneratePassword = findViewById(R.id.btnGeneratePassword);
        txtLengthValue = findViewById(R.id.txtLengthValue);
        seekPasswordLength = findViewById(R.id.seekPasswordLength);
        txtGeneratedPassword = findViewById(R.id.txtGeneratedPassword);
        btnUsePassword = findViewById(R.id.btnUsePassword);

        String password = generatePassword(8);
        txtGeneratedPassword.setText(password);


        btnUpdateCredentials.setVisibility(View.GONE);
        btnUpdateCredentials.setEnabled(false);

        originalListener = txtValueName.getKeyListener();
        originalDrawable = txtValueName.getBackground();
        disableEditText();

        String name = getIntent().getStringExtra("NAME");
        txtValueName.setText(name);
        txtValueUsername.setText(getIntent().getStringExtra("USERNAME"));
        password = getIntent().getStringExtra("PASSWORD");
        txtValuePassword.setText(password);
        txtValueWebsite.setText(getIntent().getStringExtra("WEBSITE"));
        txtValue2FAKey.setText(getIntent().getStringExtra("SECRETKEY"));


        //update database
        myDB = new DatabaseHelper(getApplicationContext());
        itemID = myDB.getIDFromName(name);
        myDB.updateCredentials(itemID, name, txtValueUsername.getText().toString(),
                password, txtValueWebsite.getText().toString(),
                txtValue2FAKey.getText().toString());

        // get 2FA code
        handler2FA = new Handler();
        handler2FA.postDelayed(runnable2FA, 1000);


        //QR Scanner
        txtValue2FAKey.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (txtValue2FAKey.getRight() - txtValue2FAKey.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
//                        Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_LONG).show();


                        //QR Scanner for 2FA code
                        Intent intent = new Intent();
                        intent.setClass(LoginCredentialDetail.this, QRScanner.class);
                        intent.putExtra("NAME", txtValueName.getText().toString());
                        intent.putExtra("USERNAME", txtValueUsername.getText().toString());
                        intent.putExtra("PASSWORD", txtValuePassword.getText().toString());
                        intent.putExtra("WEBSITE", txtValueWebsite.getText().toString());
                        intent.putExtra("SECRETKEY", txtValue2FAKey.getText().toString());
                        startActivity(intent);
                        finish();
                        return true;
                    }
                }
                return false;
            }
        });

        //Save updated details
        btnUsePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtValuePassword.setText(txtGeneratedPassword.getText());
                System.out.println(itemID);
                String name = txtValueName.getText().toString();
                String username = txtValueUsername.getText().toString();
                String password = txtValuePassword.getText().toString();
                String website = txtValueWebsite.getText().toString();
                String secretkey = txtValue2FAKey.getText().toString();
                myDB.updateCredentials(itemID, name, username, password, website, secretkey);
                Toast.makeText(getApplicationContext(), "Details updated", Toast.LENGTH_LONG).show();
                disableEditText();
            }
        });


        //Password generator
        btnGeneratePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = generatePassword(seekPasswordLength.getProgress());
                txtGeneratedPassword.setText(password);
            }
        });

        //Change password length
        seekPasswordLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txtLengthValue.setText(String.valueOf(i));
//                System.out.println(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                String password = generatePassword(seekBar.getProgress());
                txtGeneratedPassword.setText(password);
            }
        });


        //Update Credentials
        btnUpdateCredentials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(itemID);
                String name = txtValueName.getText().toString();
                String username = txtValueUsername.getText().toString();
                String password = txtValuePassword.getText().toString();
                String website = txtValueWebsite.getText().toString();
                String secretKey = txtValue2FAKey.getText().toString();
                myDB.updateCredentials(itemID, name, username, password, website, secretKey);
                Toast.makeText(getApplicationContext(), "Details updated", Toast.LENGTH_LONG).show();
                disableEditText();


            }
        });

        //Get favicon
        try {
            //Send request to favicongrabber API
            String url = "http://favicongrabber.com/api/grab/";
            url += getIntent().getStringExtra("WEBSITE");
            System.out.println(url);
            URL faviconURL = new URL(url);
            HttpURLConnection con = (HttpURLConnection) faviconURL.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            //Parse JSON Object
            JSONObject myResponse = new JSONObject(content.toString());
            JSONObject image = ((JSONArray) myResponse.get("icons")).getJSONObject(0);
            String imageURL = image.get("src").toString();
            //load image from the url into imageView
            Picasso.get().load(imageURL).into(imgFavicon);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.setClass(this, ViewCredentials.class);
        finish();
    }

    private void initAnimation() {

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//        Explode enterTransition = new Explode();
//        enterTransition.setDuration(500);
//        getWindow().setEnterTransition(enterTransition);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    //generate random password
    private String generatePassword(int length){
          final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
          final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
          final String NUMBER = "0123456789";
          final String OTHER_CHAR = "!@#$%&*()_+-=[]?";

          String ALLOWED_CHAR = CHAR_LOWER + CHAR_UPPER + NUMBER + OTHER_CHAR;
          int len = ALLOWED_CHAR.length();
          String result = "";
          Random rand = new Random();


          for(int i=0; i<length; i++){
              int pos = rand.nextInt(len);
              result += ALLOWED_CHAR.charAt(pos);
          }
          return result;

    }

    //disable edit text
    private void disableEditText() {

        txtName.setEnabled(false);
        txtName.setFocusable(false);
        txtUsername.setEnabled(false);
        txtUsername.setFocusable(false);
        txtWebsite.setEnabled(false);
        txtWebsite.setFocusable(false);
        txtPassword.setEnabled(false);
        txtPassword.setFocusable(false);
        txt2FAKey.setEnabled(false);
        txt2FAKey.setFocusable(false);

    }

    //enable edit text
    private void enableEditText() {
        txtName.setEnabled(true);
        txtName.setFocusable(true);
        txtUsername.setEnabled(true);
        txtUsername.setFocusable(true);
        txtWebsite.setEnabled(true);
        txtWebsite.setFocusable(true);
        txtPassword.setEnabled(true);
        txtPassword.setFocusable(true);
        txt2FAKey.setEnabled(true);
        txt2FAKey.setFocusable(true);


    }

    //Get 2FA code
    public Runnable runnable2FA = new Runnable() {
        @Override
        public void run() {

            Calendar calendar = Calendar.getInstance();
            int timeLeft = 29 - (calendar.get(Calendar.SECOND))%30;
            txt2FATimer.setText(String.valueOf(timeLeft));
//
//            String secretKey = "GAUD NDRV NY6E ZISK 7V66 BH6H 3YL7 I75D PQ3V QLVP EPRM BFY3 7YTQ";
            String secretKey = txtValue2FAKey.getText().toString();
            try{
                if(first || timeLeft==29){
                    first = false;
                    Totp generator = new Totp(secretKey);
                    txt2FACode.setText(generator.now());
                }
                handler2FA.postDelayed(runnable2FA, 1000);
            }
            catch (Exception e){
                txt2FACode.setText("2FA disabled");
                txt2FATimer.setText("");
            }

        }
    };




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //edit credentials when edit button is cliked
        if (id == R.id.edit_item) {
            btnUpdateCredentials.setVisibility(View.VISIBLE);
            btnUpdateCredentials.setEnabled(true);
            enableEditText();

            return true;
        }

        //delete credentials when delete button is clicked
        else if (id == R.id.delete_item) {
            deleteDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }


}
