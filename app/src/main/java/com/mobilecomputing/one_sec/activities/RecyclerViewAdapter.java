package com.mobilecomputing.one_sec.activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mobilecomputing.one_sec.R;
import com.mobilecomputing.one_sec.mvp.contract.LockMainContract;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Serializable {

    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private Context mContext;
    private DatabaseHelper myDB;

    public RecyclerViewAdapter(ArrayList<String> mImageNames, ArrayList<String> mImages, Context mContext, DatabaseHelper myDB) {
        this.mImageNames = mImageNames;
        this.mImages = mImages;
        this.mContext = mContext;
        this.myDB = myDB;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        try{
            Picasso.get().load(mImages.get(position)).into(holder.image);
//            Glide.with(mContext)
//                    .asBitmap()
//                    .load(mImages.get(position))
//                    .into(holder.image);
        }
        catch(Exception e){
            e.printStackTrace();
            holder.image.setImageResource(R.drawable.ic_launcher_background);
        }
//        Picasso.get().load(mImages.get(position)).into(holder.image);
        holder.imageName.setText(mImageNames.get(position));

        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Cursor data = myDB.getListValue(mImageNames.get(position));
                if(data.getCount() == 0){
                    Toast.makeText(mContext, "Database is empty", Toast.LENGTH_LONG).show();
                }
                else{
                    while(data.moveToNext()) {
                        String name = data.getString(1);
                        String username = data.getString(2);
                        String password = data.getString(3);
                        String website = data.getString(4);
                        String secretKey = data.getString(5);
                        Intent intent = new Intent();
                        intent.setClass( mContext, LoginCredentialDetail.class);
                        intent.putExtra("NAME", name);
                        intent.putExtra("USERNAME", username);
                        intent.putExtra("PASSWORD", password);
                        intent.putExtra("WEBSITE", website);
                        intent.putExtra("SECRETKEY", secretKey);
//                        intent.putExtra("database", myDB);


                        Pair[] pairs = new Pair[1];
                        pairs[0] = new Pair<View, String>(holder.image, ViewCompat.getTransitionName(holder.image));
//                        pairs[1] = new Pair<View, String>(holder.imageName, ViewCompat.getTransitionName(holder.imageName));
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(((Activity) mContext), pairs);
                        mContext.startActivity(intent, options.toBundle());

                        ((Activity) mContext).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

//                        ((Activity) mContext).finish();

                        break;
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image ;
        TextView imageName;
        RelativeLayout parent_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            imageName = itemView.findViewById(R.id.imageName);
            parent_layout = itemView.findViewById(R.id.parent_layout);
        }
    }

}
