
package com.mobilecomputing.one_sec.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobilecomputing.one_sec.R;
import com.mobilecomputing.one_sec.adapters.AlbumListAdapter;
import com.mobilecomputing.one_sec.base.AppConstants;
import com.mobilecomputing.one_sec.utils.SpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class PhotoVaultActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<HashMap<String, String>> albumLst = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_vault);
//        checkForFirstLaunch();
        recyclerView = findViewById(R.id.albumListRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        albumLst = getAlbumList();
        mAdapter = new AlbumListAdapter(getApplicationContext(), albumLst);
        recyclerView.setAdapter(mAdapter);
        checkForAlbumCount();
        FloatingActionButton fab = findViewById(R.id.fab);

    }

    private ArrayList<HashMap<String, String>> getAlbumList() {

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("products");
            ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> m_li;

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);

                String i_name = jo_inside.getString("name");
                String i_price = jo_inside.getString("price");
                String i_img = jo_inside.getString("img");
                String i_cat = jo_inside.getString("category");
                String id = jo_inside.getString("id");


                //Add your values in your `ArrayList` as below:
                m_li = new HashMap<String, String>();
                m_li.put("name", i_name);
                m_li.put("price", i_price);
                m_li.put("id", id);
                m_li.put("img", i_img);
                m_li.put("cat", i_cat);

                formList.add(m_li);
            }
            return formList;
        } catch (JSONException e) {
            e.printStackTrace();
        }


/*
        ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> dataList = new ArrayList<>();
        ArrayList<HashMap<String, String>> productList = new ArrayList<>();


        File folder = getFilesDir();
        File[] files = folder.listFiles();
        ArrayList<HashMap<String, String>> mediaFolderList = new ArrayList<>();
        for (File f : files) {
            if (f.isDirectory() && f.getName().equals(AppConstants.MEDIA_FOLDER)) {
                File[] mfList = f.listFiles();
                for (File ff : mfList) {
                    HashMap<String, String> album = new HashMap<>();
                    album.put("albumName", ff.getName());
                    int c = 0;
                    if (ff.isDirectory()) {
                        c = ff.listFiles().length;
                    }
                    album.put("imageCount", "" + c);
                    mediaFolderList.add(album);
                }
                break;
            }
        }
        return mediaFolderList;*/
        return null;
    }

    private void checkForFirstLaunch() {
        if (SpUtil.getInstance().getBoolean("photoVaultFirstLaunch", true)) {
            File folder = getFilesDir();

            File[] files = folder.listFiles();
            if (!checkIfFolderExists(files)) {
                File f = new File(folder, AppConstants.MEDIA_FOLDER);
                f.mkdir();
            }
            SpUtil.getInstance().putBoolean("photoVaultFirstLaunch", false);
        }
    }


    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getApplicationContext().getAssets().open("products.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private boolean checkIfFolderExists(File[] files) {
        boolean folderExisits = false;
        for (File f : files) {
            if (f.isDirectory() && f.getName().equals(AppConstants.MEDIA_FOLDER)) {
                folderExisits = true;
                break;
            }
        }
        return folderExisits;
    }

    private void checkForAlbumCount() {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PhotoVaultActivity.this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}