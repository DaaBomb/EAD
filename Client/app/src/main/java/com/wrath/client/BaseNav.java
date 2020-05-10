package com.wrath.client;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.wrath.client.dto.User;

public class BaseNav extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedPreferences;
    String user;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    BroadcastReceiver broadcastReceiver;
    AlertDialog.Builder builder;
    TextView username;
    TextView houseDetails;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sidenav);

//        builder = new AlertDialog.Builder(this);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Home");
//        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();

        sharedPreferences = getSharedPreferences("swarm", MODE_PRIVATE);
        user = sharedPreferences.getString("user", "{}");
        User userObj = gson.fromJson(user, User.class);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                displayAlertDialog(intent);
            }
        };

        displayAlertDialog(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("INTENT_ACTION_SEND_MESSAGE");
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout:
                sharedPreferences.edit().clear().apply();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
            default:
                return false;
        }
    }

    public void displayAlertDialog(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null && extras.getString("title") != null && extras.getString("message") != null) {
            String title = extras.getString("title");
            String message = extras.getString("message");
            builder.setTitle(title).setMessage(message)
                    .setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .create().show();
        }

    }
}
