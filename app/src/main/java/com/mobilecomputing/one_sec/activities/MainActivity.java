package com.mobilecomputing.one_sec.activities;
import info.androidhive.sqlite.view.NotesActivity;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;


import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.mobilecomputing.one_sec.R;
import com.mobilecomputing.one_sec.base.AppConstants;
import com.mobilecomputing.one_sec.utils.SpUtil;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    GridLayout gridLayoutHomeGrid;
    int backButtonPressCount = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAnimation();




        setContentView(R.layout.activity_main);



        Toolbar toolbar = findViewById(R.id.toolbar);
      toolbar.dismissPopupMenus();
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.user_name);
        TextView userEmail = headerView.findViewById(R.id.user_email);
        navUsername.setText(SpUtil.getInstance().getString("name"));
        userEmail.setText(SpUtil.getInstance().getString("email"));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        gridLayoutHomeGrid = findViewById(R.id.home_grid);
        onClickGridItemListener(gridLayoutHomeGrid);
    }





    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (backButtonPressCount >= 1) {
//                Intent intent = new Intent(Intent.ACTION_MAIN);

//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
            } else {
                Toast.makeText(this, "Press the back button once again to exit.", Toast.LENGTH_SHORT).show();
                backButtonPressCount++;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_contact) {
            Intent intent = new Intent(MainActivity.this, ContactUsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent i = new Intent(android.content.Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject test");
            i.putExtra(android.content.Intent.EXTRA_TEXT, "extra text that you want to put");
            startActivity(Intent.createChooser(i, "Share via"));
        } else if (id == R.id.nav_feedback) {
            Intent intent = new Intent(MainActivity.this, FeedbackActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_tnc) {
            Intent intent = new Intent(MainActivity.this, PolicyActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(MainActivity.this, AboutAppActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            SpUtil.getInstance().clear();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initAnimation() {
        Explode enterTransition = new Explode();
        enterTransition.setDuration(500);
        getWindow().setEnterTransition(enterTransition);

//        Slide enterTransition = new Slide();
//        enterTransition.setSlideEdge(Gravity.RIGHT);
//        enterTransition.setDuration(500);
//        getWindow().setEnterTransition(enterTransition);
    }

    private void onClickGridItemListener(GridLayout mainGrid) {
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (finalI == 0) {
                        Intent intent = new Intent(MainActivity.this, ViewCredentials.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else if (finalI == 1) {
                        Intent intent = new Intent(MainActivity.this, CreditCardActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else if (finalI == 2) {
                        SpUtil.getInstance().getBoolean(AppConstants.LOCK_FROM_LOCK_MAIN_ACITVITY, true);
                        Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else if (finalI == 3) {
                        Intent intent = new Intent(MainActivity.this, PhotoVaultActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else if (finalI == 4) {
                        Intent intent = new Intent(MainActivity.this, NotesActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                }
            });
        }
    }


}
