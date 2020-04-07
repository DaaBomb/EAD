package com.example.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.client.dto.User;
import com.google.gson.Gson;

public class HomepageActivity extends AppCompatActivity {
    Button btn_builder;
    Button btn_resident;
    Button btn_staff;

    Gson gson = new Gson();
    Bundle extras = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        btn_builder = (Button) findViewById(R.id.btn_builder);
        btn_resident = (Button) findViewById(R.id.btn_resident);
        btn_staff = (Button) findViewById(R.id.btn_staff);

        Intent i = getIntent();
        String jsonString = i.getStringExtra("user");
        User user = gson.fromJson(jsonString, User.class);
        extras.putString("user", gson.toJson(user));

        btn_builder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(HomepageActivity.this, RegisterBuilder.class);
                k.putExtras(extras);
                startActivity(k);
            }
        });
        btn_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(HomepageActivity.this, SelectCommunity.class);
                extras.putString("staff", "1");
                k.putExtras(extras);
                startActivity(k);
            }
        });
        btn_resident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(HomepageActivity.this, SelectCommunity.class);
                extras.putString("staff", "0");
                k.putExtras(extras);
                startActivity(k);
            }
        });
    }
}
