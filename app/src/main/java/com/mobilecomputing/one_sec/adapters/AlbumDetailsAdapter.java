package com.mobilecomputing.one_sec.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.mobilecomputing.one_sec.R;

import java.io.File;
import java.util.ArrayList;

public class AlbumDetailsAdapter extends RecyclerView.Adapter<AlbumDetailsAdapter.MyViewHolder> {
    private ArrayList<File> mDataset;
    private OnItemClickListener mOnItemClickListener;
    private Context context;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }




    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgThumbnail;

        public MyViewHolder(View v) {
            super(v);
            imgThumbnail = v.findViewById(R.id.img_thumbnail);
        }
    }


    public AlbumDetailsAdapter(Context context, ArrayList<File> myDataset, OnItemClickListener onItemClickListener) {
        mDataset = myDataset;
        this.context = context;
        mOnItemClickListener = onItemClickListener;
    }


    @Override
    public AlbumDetailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {

        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.thumbnail_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (null != mDataset && mDataset.size() > 0) {


            final int THUMBNAIL_SIZE = 256;
            Bitmap thumbImage = ThumbnailUtils.extractThumbnail(
                    BitmapFactory.decodeFile(mDataset.get(position).getAbsolutePath()),
                    THUMBNAIL_SIZE ,
                    THUMBNAIL_SIZE );
            holder.imgThumbnail.setImageBitmap(thumbImage);
        }
    }


    @Override
    public int getItemCount() {
        if (null != mDataset && mDataset.size() > 0)
            return mDataset.size();
        else
            return 0;
    }
}