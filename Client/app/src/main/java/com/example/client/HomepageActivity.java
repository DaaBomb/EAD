package com.example.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class HomepageActivity extends AppCompatActivity {
    Button btn_builder;
    Button btn_resident;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        btn_builder = (Button) findViewById(R.id.btn_builder);
        btn_resident = (Button) findViewById(R.id.btn_resident);
        btn_builder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getIntent();

                String jsonString = i.getStringExtra("user");
                JSONObject user =  null;
                try {
                     user = new JSONObject(jsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(HomepageActivity.this,""+ user.toString(),Toast.LENGTH_LONG).show();
                startActivity(new Intent(HomepageActivity.this,RegisterBuilder.class));
            }
        });
        btn_resident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomepageActivity.this,ResidentRegister.class));
            }
        });
    }
}
