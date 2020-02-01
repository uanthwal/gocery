package com.mobilecomputing.one_sec.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobilecomputing.one_sec.R;
import com.mobilecomputing.one_sec.adapters.AlbumDetailsAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class AlbumDetailActivity extends AppCompatActivity {
    private static final int SELECT_IMAGE = 1;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<File> imagesInAlbum = new ArrayList<>();
    String albumName;
    TextView textViewNoImgs, albumTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);
        Intent intent = getIntent();
        albumTitle = findViewById(R.id.album_title);
        albumName = intent.getStringExtra("albumName");
        String imgCount = intent.getStringExtra("imageCount");
        albumTitle.setText(albumName + " (" + imgCount + ")");
        textViewNoImgs = findViewById(R.id.no_img_lbl);
        imagesInAlbum = getImagesForAlbum(albumName);
        checkForImgCount();
        recyclerView = findViewById(R.id.albumImagesRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.addItemDecoration(new GridSpacingItem(3, 50, true));

        mAdapter = new AlbumDetailsAdapter(getApplicationContext(), imagesInAlbum, new AlbumDetailsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                System.out.println(position);
            }
        });
        recyclerView.setAdapter(mAdapter);

        FloatingActionButton addImagesBtn = findViewById(R.id.fab_add_images);
        addImagesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                        && (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    ActivityCompat.requestPermissions(AlbumDetailActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);
                    return;
                }
                openImageGallery();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageGallery();
            } else {
                // User refused to grant permission.
            }
        }
    }

    private void openImageGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            intent = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ArrayList<File> selectedImages = new ArrayList<>();
        if (requestCode == SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                final int takeFlags = data.getFlags() & Intent.FLAG_GRANT_READ_URI_PERMISSION;
                ContentResolver resolver = getApplicationContext().getContentResolver();
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        resolver.takePersistableUriPermission(imageUri, takeFlags);
                        String realPath = ImagePathUtil.getPath(AlbumDetailActivity.this, imageUri);
                        File srcFile = new File(realPath);
                        String fileName = srcFile.getName();
                        File destFile = new File("/data/user/0/com.mobilecomputing.one_sec/files/Media/" + albumName + "/" + fileName);
                        selectedImages.add(destFile);
                        try {
                            copyFile(GetBytesFromFile.getBytesFromFile(srcFile), destFile);
                            deleteFileFromMediaStore(getContentResolver(), srcFile);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    imagesInAlbum.addAll(selectedImages);
                    mAdapter.notifyDataSetChanged();
                } else if (data.getData() != null) {
                    resolver.takePersistableUriPermission(data.getData(), takeFlags);
                    String realPath = ImagePathUtil.getPath(AlbumDetailActivity.this, data.getData());
                    File srcFile = new File(realPath);
                    String fileName = srcFile.getName();
                    File destFile = new File("/data/user/0/com.mobilecomputing.one_sec/files/Media/" + albumName + "/" + fileName);
                    try {
                        copyFile(GetBytesFromFile.getBytesFromFile(srcFile), destFile);
                        deleteFileFromMediaStore(getContentResolver(), srcFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    imagesInAlbum.add(destFile);
                    mAdapter.notifyDataSetChanged();
                }
                checkForImgCount();
                updateImgCount();
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<File> getImagesForAlbum(String albumName) {
        File folder = new File("/data/user/0/com.mobilecomputing.one_sec/files/Media/" + albumName);
        File[] files = folder.listFiles();
        for (File f : files) {
            imagesInAlbum.add(f);
        }
        return imagesInAlbum;
    }

    private void copyFile(byte[] data, File destFile) {
        OutputStream out;
        try {
            destFile.createNewFile();
            out = new FileOutputStream(destFile);
            out.write(data);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void callBroadCast() {
        if (Build.VERSION.SDK_INT >= 14) {
            Log.e("-->", " >= 14");
            MediaScannerConnection.scanFile(this, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    Log.e("ExternalStorage", "Scanned " + path + ":");
                    Log.e("ExternalStorage", "-> uri=" + uri);
                }
            });
        } else {
            Log.e("-->", " < 14");
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }

    public static void deleteFileFromMediaStore(final ContentResolver contentResolver, final File file) {
        String canonicalPath;
        try {
            canonicalPath = file.getCanonicalPath();
        } catch (IOException e) {
            canonicalPath = file.getAbsolutePath();
        }
        final Uri uri = MediaStore.Files.getContentUri("external");
        final int result = contentResolver.delete(uri,
                MediaStore.Files.FileColumns.DATA + "=?", new String[]{canonicalPath});
        if (result == 0) {
            final String absolutePath = file.getAbsolutePath();
            if (!absolutePath.equals(canonicalPath)) {
                contentResolver.delete(uri,
                        MediaStore.Files.FileColumns.DATA + "=?", new String[]{absolutePath});
            } else {
                Log.d("In else block", "");
            }
        }
    }

    private void checkForImgCount() {
        if (null != imagesInAlbum && imagesInAlbum.size() > 0) {
            textViewNoImgs.setVisibility(View.INVISIBLE);
        } else {
            textViewNoImgs.setVisibility(View.VISIBLE);
        }
    }

    private void updateImgCount() {
        if (null != imagesInAlbum)
            albumTitle.setText(albumName + " (" + imagesInAlbum.size() + ")");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AlbumDetailActivity.this, PhotoVaultActivity.class);
        startActivity(intent);
    }
}