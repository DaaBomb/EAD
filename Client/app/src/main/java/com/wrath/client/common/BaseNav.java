package com.wrath.client.common;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.wrath.client.MainActivity;
import com.wrath.client.R;
import com.wrath.client.Retrofit.IMyService;
import com.wrath.client.Retrofit.RetrofitClient;
import com.wrath.client.dto.BaseResponse;
import com.wrath.client.dto.NotificationDetails;
import com.wrath.client.dto.PermissionRequest;
import com.wrath.client.dto.PermissionResponse;
import com.wrath.client.dto.User;
import com.wrath.client.user.ProfilePage;

import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

public class BaseNav extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedPreferences;
    BroadcastReceiver broadcastReceiver;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    NavigationView navigationView;
    TextView username;
    TextView house_details;

    public String user;
    public User userObj;
    public Gson gson = new Gson();
    public CompositeDisposable compositeDisposable = new CompositeDisposable();
    public IMyService iMyService;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
        sharedPreferences = getSharedPreferences("swarm", MODE_PRIVATE);
        user = sharedPreferences.getString("user", "{}");
        userObj = gson.fromJson(user, User.class);
        builder = new AlertDialog.Builder(this);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                displayAlertDialog(intent);
            }
        };

        displayAlertDialog(getIntent());
    }

    public void setDrawerLayout(DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    public void setNavigationView(NavigationView navigationView) {
        this.navigationView = navigationView;
    }

    public void initialize() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        initializeNavBar();
    }

    public void initializeNavBar() {
        View headerLayout = navigationView.getHeaderView(0);
        username = (TextView) headerLayout.findViewById(R.id.txt_username);
        house_details = (TextView) headerLayout.findViewById(R.id.txt_house_details);
        username.setText(userObj.getName());
        house_details.setText(userObj.getAddress().getBlockname() + "-" + userObj.getAddress().getFlatnum());
        if (userObj.getAddress().getBlockname() == null)
            house_details.setVisibility(View.GONE);
        navigationView.setNavigationItemSelectedListener(this);
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
        else{
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout:
                sharedPreferences.edit().clear().apply();
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.nav_profile:
                startActivity(new Intent(this, ProfilePage.class));
                return true;
            default:
                return false;
        }
    }

    private void displayAlertDialog(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null && extras.getString("title") != null && extras.getString("message") != null) {
            String title = extras.getString("title");
            String message = extras.getString("message");
            final NotificationDetails notificationDetails = gson.fromJson(extras.getString("notificationDetails"),NotificationDetails.class);
            if(userObj.getProfession()==null) {
                final View customLayout = getLayoutInflater().inflate(R.layout.notification_dialog, null);
                builder.setTitle(title).setView(customLayout);
                TextView visitor_name = customLayout.findViewById(R.id.visitor_name);
                TextView purpose = customLayout.findViewById(R.id.purpose);
                visitor_name.setText(notificationDetails.getVisitor_name());
                purpose.setText(notificationDetails.getPurpose());
                customLayout.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        givePermission(notificationDetails.get_id(), true);
                        alertDialog.dismiss();
                    }
                });
                customLayout.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        givePermission(notificationDetails.get_id(), false);
                        alertDialog.dismiss();
                    }
                });
                alertDialog= builder.create();
                alertDialog.show();

            }
            else{
                builder.setTitle(title).setMessage(message).create().show();
            }
        }
    }

    public void givePermission(String _id, Boolean confirmed){
        PermissionRequest permissionRequest = new PermissionRequest(_id, confirmed);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"),gson.toJson(permissionRequest));
        compositeDisposable.add(iMyService.sendPermission(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                       PermissionResponse res=gson.fromJson(response, PermissionResponse.class);
                       if(res.getMsg().equals("successful")){
                           //Do something
                       }
                    }
                })
        );

    }
}
