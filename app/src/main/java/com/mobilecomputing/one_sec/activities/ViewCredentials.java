package com.mobilecomputing.one_sec.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.transition.Explode;

import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobilecomputing.one_sec.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ViewCredentials extends AppCompatActivity implements Serializable {

    DatabaseHelper myDB;

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    FloatingActionButton fab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewcontents_layout);
        setTitle("Credentials");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        fab = findViewById(R.id.fab);
        myDB = new DatabaseHelper(this);
        System.out.println("test0");
        final ArrayList<String> list = new ArrayList<>();
        final Cursor data = myDB.getListContents();
        TextView noCredsLbl = findViewById(R.id.no_creds_lbl);
        if (data.getCount() == 0) {
            noCredsLbl.setVisibility(View.VISIBLE);
        } else {
            noCredsLbl.setVisibility(View.GONE);
            while (data.moveToNext()) {
                String name = data.getString(1);
                System.out.println("name "+name);
                String username = data.getString(2);
                String password = data.getString(3);
                String website = data.getString(4);

                mNames.add(name);
//                Favicon favicon = new Favicon();
//                favicon.execute(website);

                String imageURL = getImageURL(website);
                if (imageURL != "")
                    mImageUrls.add(imageURL);
                else
                    System.out.println("Error retrieving image");


            }
            initRecyclerView(myDB);
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ViewCredentials.this, AddCredential.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private String getImageURL(String name) {

//        return "https://"+name+"/favicon.ico";

        try {
            String url = "http://favicongrabber.com/api/grab/";
            url += name;
//            System.out.println(url);
            URL faviconURL = new URL(url);
            HttpURLConnection con = (HttpURLConnection) faviconURL.openConnection();
            con.setRequestMethod("GET");
//            System.out.println("picasso test");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
//            System.out.println(content);
            JSONObject myResponse = new JSONObject(content.toString());
            JSONObject image = ((JSONArray) myResponse.get("icons")).getJSONObject(0);
            String imageURL = image.get("src").toString();
            return imageURL;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private void initRecyclerView(DatabaseHelper myDB) {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mNames, mImageUrls, this, myDB);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


}
