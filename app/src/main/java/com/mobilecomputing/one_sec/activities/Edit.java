package com.mobilecomputing.one_sec.activities;

import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mobilecomputing.one_sec.R;

import java.util.Calendar;

public class Edit extends AppCompatActivity {
    Toolbar toolbar;
    EditText nTitle,ncardnum,nexpiry,ncvv;
    Calendar c;
    String todaysDate;
    String currentTime;
    long nId;
    ImageView imageicon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent i = getIntent();
        nId = i.getLongExtra("ID",0);
        SimpleDatabase db = new SimpleDatabase(this);
        Card card = db.getNote(nId);

        //Setting the same on display card
        TextView dispcard = findViewById(R.id.card_preview_number);
        dispcard.setText(card.getCardnum());
        TextView dcardname = findViewById(R.id.card_preview_name);
        dcardname.setText(card.getTitle());
        TextView dexp = findViewById(R.id.card_preview_expiry);
        dexp.setText(card.getCardnum());

        final String title = card.getTitle();
        final String cardnum = card.getCardnum();
        String expiry = card.getExpiry();
        String cvv = card.getCvv();
        nTitle = findViewById(R.id.noteTitle);
        ncardnum = findViewById(R.id.CardNumber);
        nexpiry = findViewById(R.id.Expirydate);
        ncvv = findViewById(R.id.cvv);
        imageicon = findViewById(R.id.cardicon);

        nTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                getSupportActionBar().setTitle(title);
                if(cardnum.startsWith("1")|| cardnum.startsWith("4"))
                {
                    imageicon.setImageResource(R.drawable.visa);
                }
                else if(cardnum.startsWith("2")|| cardnum.startsWith("5"))
                {
                    imageicon.setImageResource(R.drawable.mastercard);
                }
                else if(cardnum.startsWith("3")||cardnum.startsWith("6"))
                {
                    imageicon.setImageResource(R.drawable.american_express);
                }
                else
                {
                    imageicon.setImageResource(R.drawable.maestro);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0){
                    getSupportActionBar().setTitle(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        nTitle.setText(title);
        ncardnum.setText(cardnum);
        nexpiry.setText(expiry);
        ncvv.setText(cvv);

        // set current date and time
        c = Calendar.getInstance();
        todaysDate = c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.DAY_OF_MONTH);
        Log.d("DATE", "Date: "+todaysDate);
        currentTime = pad(c.get(Calendar.HOUR))+":"+pad(c.get(Calendar.MINUTE));
        Log.d("TIME", "Time: "+currentTime);
    }


    private String pad(int time) {
        if(time < 10)
            return "0"+time;
        return String.valueOf(time);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.save){
            Card card = new Card(nId,nTitle.getText().toString(),ncardnum.getText().toString(),nexpiry.getText().toString(),ncvv.getText().toString(),todaysDate,currentTime);
            Log.d("EDITED", "edited: before saving id -> " + card.getId());
            SimpleDatabase sDB = new SimpleDatabase(getApplicationContext());
            long id = sDB.editNote(card);
            Log.d("EDITED", "EDIT: id " + id);
            goToMain();
            Toast.makeText(this, "card Edited.", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToMain() {
        Intent i = new Intent(this,CreditCardActivity.class);
        startActivity(i);
    }


}
