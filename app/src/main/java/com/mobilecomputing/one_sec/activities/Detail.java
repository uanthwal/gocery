package com.mobilecomputing.one_sec.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobilecomputing.one_sec.R;

public class Detail extends AppCompatActivity {
    long id;
    ImageView imageicon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent i = getIntent();
        id = i.getLongExtra("ID", 0);
        SimpleDatabase db = new SimpleDatabase(this);
        Card card = db.getNote(id);
        getSupportActionBar().setTitle(card.getTitle());
        TextView details = findViewById(R.id.noteDesc);
        details.setText(card.getTitle());
        TextView cardnum = findViewById(R.id.cardnum);
        cardnum.setText(card.getCardnum());
        TextView exp = findViewById(R.id.exp);
        exp.setText(card.getExpiry());
        TextView cvv = findViewById(R.id.cvvv);
        cvv.setText(card.getCvv());
        imageicon = findViewById(R.id.cardicon);

        //Setting the same on display card
        TextView dispcard = findViewById(R.id.card_preview_number);
        dispcard.setText(card.getCardnum());
        TextView dcardname = findViewById(R.id.card_preview_name);
        dcardname.setText(card.getTitle());
        TextView dexp = findViewById(R.id.card_preview_expiry);
        dexp.setText(card.getCardnum());

        details.setMovementMethod(new ScrollingMovementMethod());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDatabase db = new SimpleDatabase(getApplicationContext());
                db.deleteNote(id);
                Toast.makeText(getApplicationContext(), "Card deleted successfully", Toast.LENGTH_LONG).show();
                goToMain();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String cardnum1 = card.getCardnum();
        if (cardnum1.startsWith("1") || cardnum1.startsWith("4")) {
            imageicon.setImageResource(R.drawable.visa);
        } else if (cardnum1.startsWith("2") || cardnum1.startsWith("5")) {
            imageicon.setImageResource(R.drawable.mastercard);
        } else if (cardnum1.startsWith("3") || cardnum1.startsWith("6")) {
            imageicon.setImageResource(R.drawable.american_express);
        } else {
            imageicon.setImageResource(R.drawable.maestro);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.edit) {
            Intent i = new Intent(this, Edit.class);
            i.putExtra("ID", id);
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void goToMain() {
        Intent i = new Intent(this, CreditCardActivity.class);
        startActivity(i);
    }
}
