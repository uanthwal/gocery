package com.mobilecomputing.one_sec.activities;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobilecomputing.one_sec.R;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<Card> cards;

    Adapter(Context context, List<Card> cards){
        this.inflater = LayoutInflater.from(context);
        this.cards = cards;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.custom_list_view,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String title    = cards.get(i).getTitle();
        String date     = cards.get(i).getDate();
        String time     = cards.get(i).getTime();
        String cardnum = cards.get(i).getCardnum();
        long    id       = cards.get(i).getId();
        Log.d("date on ", "Date on: "+date);

        viewHolder.nTitle.setText(title);
        viewHolder.nDate.setText(date);
        viewHolder.nTime.setText(time);
        viewHolder.nID.setText(String.valueOf(cards.get(i).getId()));
        if(cardnum.startsWith("1")|| cardnum.startsWith("4"))
        {

            viewHolder.nImage.setImageResource(R.drawable.visa);
        }
        else if(cardnum.startsWith("2")|| cardnum.startsWith("5"))
        {
            viewHolder.nImage.setImageResource(R.drawable.mastercard);
        }
        else if(cardnum.startsWith("3")||cardnum.startsWith("6"))
        {
            viewHolder.nImage.setImageResource((R.drawable.american_express));
        }
        else
        {
            viewHolder.nImage.setImageResource((R.drawable.maestro));
        }

    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nTitle,nDate,nTime,nID;
        ImageView nImage;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            nTitle  = itemView.findViewById(R.id.nTitle);
            nDate   = itemView.findViewById(R.id.nDate);
            nTime   = itemView.findViewById(R.id.nTime);
            nID     = itemView.findViewById(R.id.listId);
            nImage  = (ImageView) itemView.findViewById(R.id.cardimage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(),Detail.class);
                    i.putExtra("ID", cards.get(getAdapterPosition()).getId());
                    v.getContext().startActivity(i);
                }
            });
        }
    }
}
