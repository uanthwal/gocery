
package com.mobilecomputing.one_sec.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.text.InputType;
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class PhotoVaultActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<HashMap<String, String>> albumLst = new ArrayList<>();
    TextView textViewNoAlbums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_vault);
        checkForFirstLaunch();
        recyclerView = findViewById(R.id.albumListRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        albumLst = getAlbumList();
        mAdapter = new AlbumListAdapter(getApplicationContext(), albumLst);
        recyclerView.setAdapter(mAdapter);
        textViewNoAlbums = findViewById(R.id.noAlbumLbl);
        checkForAlbumCount();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PhotoVaultActivity.this);
                builder.setTitle("Enter Album Name");
                final EditText input = new EditText(PhotoVaultActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString();
                        if (null != albumLst && albumLst.contains(m_Text)) {
                            Toast.makeText(getApplicationContext(), AppConstants.ALBUM_EXISTS_ERR, Toast.LENGTH_SHORT).show();
                        } else {
                            File folder = getFilesDir();
                            File f = new File(folder + "/" + AppConstants.MEDIA_FOLDER, m_Text);
                            f.mkdir();
                            HashMap<String, String> newItem = new HashMap<>();
                            newItem.put("albumName", m_Text);
                            newItem.put("imageCount", "0");
                            albumLst.add(albumLst.size(), newItem);
                            mAdapter.notifyDataSetChanged();
                        }
                        checkForAlbumCount();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    private ArrayList<HashMap<String, String>> getAlbumList() {
        ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> dataList = new ArrayList<>();
        ArrayList<HashMap<String, String>> productList = new ArrayList<>();
        productList.add("")
        dataList.add();

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
        return mediaFolderList;
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
        if (null != albumLst && albumLst.size() > 0) {
            textViewNoAlbums.setVisibility(View.INVISIBLE);
        } else {
            textViewNoAlbums.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PhotoVaultActivity.this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}