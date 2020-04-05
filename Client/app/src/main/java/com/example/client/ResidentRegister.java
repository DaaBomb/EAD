package com.example.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rengwuxian.materialedittext.MaterialEditText;

public class ResidentRegister extends AppCompatActivity {
    MaterialEditText edt_block_name;
    MaterialEditText edt_flat_num;
    Button btn_continue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident_register);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String block_name = edt_block_name.getText().toString();
                String flat_num = edt_flat_num.getText().toString();
                startActivity(new Intent(ResidentRegister.this,Leadpage.class));
            }
        });
    }
}
