package com.mobilecomputing.one_sec.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.mobilecomputing.one_sec.R;
import com.mobilecomputing.one_sec.activities.AlbumDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.datatype.Duration;

import static android.content.ContentValues.TAG;

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<HashMap<String, String>> mDataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView albumName;
        public TextView imgCount;
        public ImageView imageView;
        public ImageView selectBtn;


        public MyViewHolder(View v) {
            super(v);
            albumName = v.findViewById(R.id.p_title_1);
            imgCount = v.findViewById(R.id.i_price);
            imageView = v.findViewById(R.id.i_p_1);
            selectBtn = v.findViewById(R.id.selectBtn);
        }
    }


    public AlbumListAdapter(Context pContext, ArrayList<HashMap<String, String>> myDataset) {
        this.context = pContext;
        this.mDataset = myDataset;
    }


    @Override
    public AlbumListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {

        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v);

        return vh;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (null != mDataset && mDataset.size() > 0) {
            HashMap<String, String> currentItem = mDataset.get(position);
            holder.albumName.setText(currentItem.get("name"));
            holder.imgCount.setText(currentItem.get("price"));
            int resId = 0;
            if (currentItem.get("id").equals("1")) {
                resId = R.mipmap.silk_almond;
            } else  if (currentItem.get("id").equals("2")) {
                resId = R.mipmap.chc_milk;
            }
            else  if (currentItem.get("id").equals("3")) {
                resId = R.mipmap.bonless_ch;
            }
            else  if (currentItem.get("id").equals("4")) {
                resId = R.mipmap.ch_thighs;
            }
            else  if (currentItem.get("id").equals("5")) {
                resId = R.mipmap.oreo;
            }
            else  if (currentItem.get("id").equals("6")) {
                resId = R.mipmap.skimmed_milk_1;
            }
            else  if (currentItem.get("id").equals("7")) {
                resId = R.mipmap.skimmed_milk_3;
            }
            else  if (currentItem.get("id").equals("8")) {
                resId = R.mipmap.rice;
            }

            holder.imageView.setImageResource(resId);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(context, "Product added to cart",Toast.LENGTH_LONG).show();
                holder.selectBtn.setImageResource(R.mipmap.ticked);

//                Log.d(TAG, "onClick:  albumName" + position);
//                Intent intent = new Intent(context, AlbumDetailActivity.class);
//                intent.putExtra("albumName", mDataset.get(position).get("albumName"));
//                intent.putExtra("imageCount", mDataset.get(position).get("imageCount"));
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (null != mDataset && mDataset.size() > 0)
            return mDataset.size();
        else
            return 0;
    }
}