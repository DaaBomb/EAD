package com.wrath.client.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.gson.Gson;
import com.wrath.client.R;
import com.wrath.client.Retrofit.IMyService;

import io.reactivex.disposables.CompositeDisposable;

public class ProfilePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        Gson gson = new Gson();
        SharedPreferences sharedPreferences;
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        IMyService iMyService;
    }
}
