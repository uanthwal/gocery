package com.mobilecomputing.one_sec.activities;

/*
 * created by Avinash
 */

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.mobilecomputing.one_sec.R;

public class QRScanner extends AppCompatActivity {

    SurfaceView cameraPreview;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
    boolean once = true;
    ImageView checkoutBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Order Summary");
        setContentView(R.layout.order_summ_activity);
        checkoutBtn = findViewById(R.id.imgBtn);

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QRScanner.this, ViewCredentials.class);
                startActivity(intent);
            }
        });
    }
}
