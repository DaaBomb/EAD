package com.wrath.client.register;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.wrath.client.R;
import com.wrath.client.register.builder.RegisterBuilder;

public class HomepageActivity extends AppCompatActivity {
    Button btn_builder;
    Button btn_resident;
    Button btn_staff;
    Bundle extras = new Bundle();
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        btn_builder = (Button) findViewById(R.id.btn_builder);
        btn_resident = (Button) findViewById(R.id.btn_resident);
        btn_staff = (Button) findViewById(R.id.btn_staff);

        sharedPreferences = getSharedPreferences("swarm", MODE_PRIVATE);
        String user = sharedPreferences.getString("user", "{}");
        extras.putString("user", user);

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
