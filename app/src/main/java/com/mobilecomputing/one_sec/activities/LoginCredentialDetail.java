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

public class LoginCredentialDetail extends AppCompatActivity  {
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
        setContentView(R.layout.final_page_activity1);
        setTitle("Order Status");
//        initAnimation();

    }

}
