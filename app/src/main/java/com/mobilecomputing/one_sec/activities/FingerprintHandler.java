package com.mobilecomputing.one_sec.activities;

/*
 * created by Avinash
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.Manifest;
import android.media.Image;
import android.os.Build;
import android.os.CancellationSignal;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.mobilecomputing.one_sec.R;

import java.util.concurrent.TimeUnit;

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private CancellationSignal cancellationSignal;
    private Context context;
    ImageView fingerprintImage;

    public FingerprintHandler(Context mContext) {
        context = mContext;
        fingerprintImage = ((Activity)context).findViewById(R.id.imageView);
    }

    //start fingerprint authentication
    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {

        cancellationSignal = new CancellationSignal();
        //check for permissions
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override

    //Authentication Error
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        System.out.println("Authentication error: "+ errString);
    }

    @Override

    //Authentication failed
    public void onAuthenticationFailed() {
        fingerprintImage.setImageResource(R.mipmap.fp_fail);
        shakeImage();
        Toast.makeText(context, "Authentication failed", Toast.LENGTH_LONG).show();
    }

    //Shake image in a linear way - horizontally
    public void shakeImage() {
        Animation shake;
        shake = AnimationUtils.loadAnimation(context, R.anim.linear_interpolator);
        fingerprintImage.startAnimation(shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fingerprintImage.setImageResource(R.mipmap.fp_default);
                Animation fade_in;
                fade_in = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
                fingerprintImage.startAnimation(fade_in);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        Toast.makeText(context, "Authentication help\n" + helpString, Toast.LENGTH_LONG).show();
    }@Override

    //Authentication Success
    public void onAuthenticationSucceeded(
            FingerprintManager.AuthenticationResult result) {
        fingerprintImage.setImageResource(R.mipmap.fp_succes);
        //Display animation when fingerprint is successful
        Animation fade_out;
        fade_out = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        fingerprintImage.startAnimation(fade_out);
        fade_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fingerprintImage.setImageResource(R.mipmap.fp_accepted);
                Animation fade_in;
                fade_in = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
                fingerprintImage.startAnimation(fade_in);
                fade_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Toast.makeText(context, "Authentication success!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.setClass(context, MainActivity.class);

                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

}