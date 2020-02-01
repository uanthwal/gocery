package com.mobilecomputing.one_sec.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import java.security.Provider;
import java.security.SecureRandom;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.mobilecomputing.one_sec.R;
import com.warrenstrange.googleauth.GoogleAuthenticator;

import org.jboss.aerogear.security.otp.Totp;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class TestActivity extends AppCompatActivity implements LocationListener {
    protected LocationManager locationManager;

    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    TextView latitude;
    TextView longitude;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layoout);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);

        getLocation();

    }




    private void getLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(ActivityCompat.checkSelfPermission(TestActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            System.out.println("No permissions");
            requestPermissions(LOCATION_PERMS, 190);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


    }

    @Override
    public void onLocationChanged(Location location) {
        if(location.getLatitude()<50){
            setContentView(R.layout.test_layoout);
        }else{
            setContentView(R.layout.test_layout1);
        }
        latitude.setText(String.valueOf(location.getLatitude()));
        longitude.setText(String.valueOf(location.getLongitude()));
        Toast.makeText(getApplicationContext(), String.valueOf(location.getLatitude()), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    //Returns distance between two points in metres
    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515 * 1.609344 * 1000;
            return (dist);
        }
    }


}
