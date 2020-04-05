package com.example.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

public class RegisterBuilder extends AppCompatActivity {
    Button btn_continue;
    MaterialEditText edt_society_name;
    MaterialEditText edt_address;
    MaterialEditText edt_city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_builder);
        edt_society_name = (MaterialEditText) findViewById(R.id.edt_aprt_name);
        edt_address = (MaterialEditText) findViewById(R.id.edt_aprt_address);
        edt_city = (MaterialEditText) findViewById(R.id.edt_aprt_city);
        btn_continue=(Button) findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerSociety(
                        edt_society_name.getText().toString(),
                        edt_address.getText().toString(),
                        edt_city.getText().toString()
                );
            }
        });
    }
    public void registerSociety(String socity_name,String Address,String city ){
        if (TextUtils.isEmpty(edt_society_name.getText().toString()  )){
            Toast.makeText(RegisterBuilder.this,"Society name cannot be empty",Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(edt_address.getText().toString()  )){
            Toast.makeText(RegisterBuilder.this,"Address cannot be empty",Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(edt_city.getText().toString() )){
            Toast.makeText(RegisterBuilder.this,"City name cannot be empty",Toast.LENGTH_LONG).show();
            return;
        }
        startActivity(new Intent(RegisterBuilder.this,Leadpage.class));
    }
}
