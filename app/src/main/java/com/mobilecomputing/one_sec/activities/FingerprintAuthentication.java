package com.mobilecomputing.one_sec.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mobilecomputing.one_sec.R;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class FingerprintAuthentication extends AppCompatActivity implements LocationListener {

    private static final String KEY_NAME = "yourKey";
    private Cipher cipher;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private TextView textView;
    private FingerprintManager.CryptoObject cryptoObject;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    protected LocationManager locationManager;
    Button btnAddTrustedPlace;
    private double latitude;
    private double longitude;
    boolean addTrustedLocation = false;

    SharedPreferences sharedpreferences;
    public static final String mypreference = "location";
    public static final String trustedLatitude = "latitude";
    public static final String trustedLongitude = "longitude";

    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private FusedLocationProviderClient fusedLocationClient;
    Location lastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint_authentication);
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

//        if(ActivityCompat.checkSelfPermission(FingerprintAuthentication.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//            System.out.println("No permissions");
//            requestPermissions(LOCATION_PERMS, 190);
////            return;
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && (ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(FingerprintAuthentication.this, LOCATION_PERMS,
                    190);
            return;
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        lastKnownLocation = location;
                        if (location != null) {
                            // Logic to handle location object
                            System.out.println("last loc " + location.getLatitude()+" "+location.getLongitude());
                            latitude = sharedpreferences.getFloat(trustedLatitude, 0.0f);
                            longitude = sharedpreferences.getFloat(trustedLongitude, 0.0f);
                            System.out.println("latitude "+latitude+" longitude "+longitude);
                            double distance = distance(latitude, longitude,
                                    location.getLatitude(), location.getLongitude());
                            System.out.println(addTrustedLocation);
                            System.out.println("distance is "+ distance);
                            if(!addTrustedLocation && distance < 500){
                                Intent intent = new Intent();
                                intent.setClass(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                        else{
                            System.out.println("No last known location");
                        }
                    }
                });

//        Location lastKnownLoc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


        btnAddTrustedPlace = findViewById(R.id.btnAddTrustedPlace);


        btnAddTrustedPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(FingerprintAuthentication.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    System.out.println("No permissions");
                    requestPermissions(LOCATION_PERMS, 190);
                }
                addTrustedLocation = true;





                Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(loc == null){
                    loc = lastKnownLocation;
                }
                try{
                    Geocoder myLocation = new Geocoder(getApplicationContext(), Locale.getDefault());
                    List<Address> myList = myLocation.getFromLocation(loc.getLatitude(),loc.getLongitude(), 1);
                    Address obj = myList.get(0);
                    String add = obj.getAddressLine(0);
                    add = add + "\n" + obj.getCountryName();
                    add = add + "\n" + obj.getCountryCode();
                    add = add + "\n" + obj.getLocality();
                    System.out.println(add);
                }catch(Exception e){
                    e.printStackTrace();
                }


                System.out.println("abc" + loc.getLongitude()+" "+loc.getLatitude());
                latitude = loc.getLatitude();
                longitude = loc.getLongitude();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putFloat(trustedLatitude, (float) latitude);
                editor.putFloat(trustedLongitude, (float) longitude);
                editor.commit();
                Toast.makeText(getApplicationContext(), "Authenticate with fingerprint to save trusted location", Toast.LENGTH_SHORT).show();
                initFingerprint();
            }
        });

        getLocation();

        initFingerprint();
    }

    private void getLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(ActivityCompat.checkSelfPermission(FingerprintAuthentication.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            System.out.println("No permissions");
            requestPermissions(LOCATION_PERMS, 190);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

    }

    @Override
    public void onLocationChanged(Location location) {



        System.out.println("location "+location.getLatitude()+ " " + location.getLongitude());
//        Toast.makeText(getApplicationContext(), String.valueOf(location.getLatitude()), Toast.LENGTH_SHORT).show();
//        System.out.println("test " + sharedpreferences.getFloat(trustedLatitude, 0.0f)+ " "+ sharedpreferences.getFloat(trustedLongitude, 0.0f));


        latitude = sharedpreferences.getFloat(trustedLatitude, 0.0f);
        longitude = sharedpreferences.getFloat(trustedLongitude, 0.0f);
        System.out.println("latitude "+latitude+" longitude "+longitude);
        double distance = distance(latitude, longitude,
                location.getLatitude(), location.getLongitude());
        System.out.println("zee "+ String.valueOf(addTrustedLocation));
        System.out.println("distance "+distance);
        if(!addTrustedLocation && distance < 5){
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
            locationManager.removeUpdates(this);
        }
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
    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
//            double theta = lon1 - lon2;
//            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
//            dist = Math.acos(dist);
//            dist = Math.toDegrees(dist);
//            dist = dist * 60 * 1.1515 * 1.609344 * 1000;
            double dist = org.apache.lucene.util.SloppyMath.haversinMeters(lat1, lon1, lat2, lon2);
            return (dist);

        }
    }


    private void initFingerprint() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Get an instance of KeyguardManager and FingerprintManager//
            keyguardManager =
                    (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            fingerprintManager =
                    (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

            if(fingerprintManager == null){
                Intent intent = new Intent();
                intent.setClass(this, MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }

            textView = (TextView) findViewById(R.id.textView);

            //Check whether the device has a fingerprint sensor//
            if (!fingerprintManager.isHardwareDetected()) {
                // If a fingerprint sensor isn’t available, then inform the user that they’ll be unable to use your app’s fingerprint functionality//
                textView.setText("Your device doesn't support fingerprint authentication");
            }
            //Check whether the user has granted your app the USE_FINGERPRINT permission//
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                // If your app doesn't have this permission, then display the following text//
                textView.setText("Please enable the fingerprint permission");
            }

            //Check that the user has registered at least one fingerprint//
            if (!fingerprintManager.hasEnrolledFingerprints()) {
                // If the user hasn’t configured any fingerprints, then display the following message//
                textView.setText("No fingerprint configured. Please register at least one fingerprint in your device's Settings");
            }

            //Check that the lockscreen is secured//
            if (!keyguardManager.isKeyguardSecure()) {
                // If the user hasn’t secured their lockscreen with a PIN password or pattern, then display the following text//
                textView.setText("Please enable lockscreen security in your device's Settings");
            } else {
                try {
                    generateKey();
                } catch (FingerprintAuthentication.FingerprintException e) {
                    e.printStackTrace();
                }

                if (initCipher()) {
                    //If the cipher is initialized successfully, then create a CryptoObject instance//
                    cryptoObject = new FingerprintManager.CryptoObject(cipher);

                    // Referencing the FingerprintHandler class
                    FingerprintHandler helper = new FingerprintHandler(this);
                    helper.startAuth(fingerprintManager, cryptoObject);
                }
            }
        }
    }


    private void generateKey() throws FingerprintAuthentication.FingerprintException {
        try {
            // Obtain a reference to the Keystore using the standard Android keystore container identifier (“AndroidKeystore”)//
            keyStore = KeyStore.getInstance("AndroidKeyStore");

            //Generate the key
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            //Initialize an empty KeyStore
            keyStore.load(null);

            //Initialize the KeyGenerator//
            keyGenerator.init(new

                    //key operations
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)

                    //Configure this key so that the user has to confirm their identity with a fingerprint each time they want to use it//
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());

            //Generate the key//
            keyGenerator.generateKey();

        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | CertificateException
                | IOException exc) {
            exc.printStackTrace();
            throw new FingerprintAuthentication.FingerprintException(exc);
        }
    }

    //method to initialize  cipher//
    public boolean initCipher() {
        try {
            //Obtain a cipher instance and configure it with the properties required for fingerprint authentication//
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //Return true if the cipher has been initialized successfully//
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {

            //Return false if cipher initialization failed//
            return false;
        } catch (KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    private class FingerprintException extends Exception {
        public FingerprintException(Exception e) {
            super(e);
        }
    }
}
