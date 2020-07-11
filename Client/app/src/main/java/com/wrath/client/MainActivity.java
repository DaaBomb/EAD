package com.wrath.client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wrath.client.Retrofit.IMyService;
import com.wrath.client.Retrofit.RetrofitClient;
import com.wrath.client.dto.BaseResponse;
import com.wrath.client.dto.User;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    TextView txt_create_account;
    MaterialEditText edt_Login_email, edt_Login_password;
    Button btn_login;
    SharedPreferences sharedPreferences;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;
    Gson gson = new Gson();
    User user;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        forwardUserToNextActivity(user);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        FirebaseMessaging.getInstance().subscribeToTopic("security");

        sharedPreferences = getSharedPreferences("swarm", MODE_PRIVATE);
        user = gson.fromJson(sharedPreferences.getString("user", "{}"), User.class);

        forwardUserToNextActivity(user);

        edt_Login_email = (MaterialEditText) findViewById(R.id.edt_email);
        edt_Login_password = (MaterialEditText) findViewById(R.id.edt_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(edt_Login_email.getText().toString(),
                        edt_Login_password.getText().toString());
            }
        });

        txt_create_account = (TextView) findViewById(R.id.txt_create_account);
        txt_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Register.class));
            }
        });
    }

    private void forwardUserToNextActivity(User user) {
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            if (user.getApproved()) {
                if (user.getProfession() != null) {
                    if (user.getProfession().equals("security"))
                        goToSecurityPage();
                    else
                        goToManagerPage();
                } else
                    goToLeadPage();
            } else
                goToHomePage();
        }
    }

    private void registerUser(String email, String name, String password) {
        compositeDisposable.add(iMyService.registerUser(email, name, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText(MainActivity.this, "" + response, Toast.LENGTH_LONG).show();
                    }
                })
        );
    }

    private void loginUser(String email, String password) {

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        compositeDisposable.add(iMyService.loginUser(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        BaseResponse res = gson.fromJson(response, BaseResponse.class);
                        Toast.makeText(MainActivity.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        if (res.getMsg().equals("login successful")) {
                            User user = res.getUser();
                            sharedPreferences.edit().putBoolean("isLoggedIn", true).apply();
                            sharedPreferences.edit().putString("user", gson.toJson(user)).apply();
                            if (user.getApproved()) {
                                if (user.getProfession() != null) {
                                    if (user.getProfession().equals("security"))
                                        goToSecurityPage();
                                    else
                                        goToManagerPage();
                                } else
                                    goToLeadPage();
                            } else
                                goToHomePage();
                        }

                    }
                })
        );
    }

    private void goToHomePage() {
        Intent i = new Intent(MainActivity.this, HomepageActivity.class);
        startActivity(i);
    }

    private void goToLeadPage() {
        Intent i = new Intent(MainActivity.this, Leadpage.class);
        if (getIntent() != null && getIntent().getExtras() != null)
            i.putExtras(getIntent().getExtras());
        startActivity(i);
    }

    private void goToSecurityPage() {
        Intent i = new Intent(MainActivity.this, SecurityRequestPage.class);
        if (getIntent() != null && getIntent().getExtras() != null)
            i.putExtras(getIntent().getExtras());
        startActivity(i);
    }

    private void goToManagerPage() {
        Intent i = new Intent(MainActivity.this, Leadpage.class);
        startActivity(i);
    }
}
