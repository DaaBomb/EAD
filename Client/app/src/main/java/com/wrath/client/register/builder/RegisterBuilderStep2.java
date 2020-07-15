package com.wrath.client.register.builder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.wrath.client.R;
import com.wrath.client.user.Leadpage;

import java.util.ArrayList;
import java.util.List;

public class RegisterBuilderStep2 extends AppCompatActivity {

    TextView txt_rules;
    MaterialEditText temp;
    Button btn_add_rule;
    Button btn_continue;
    int flag=1;
    List<String> rules = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_builder_step2);
        final MaterialEditText[] editTextArray = new MaterialEditText[] {
                (MaterialEditText) findViewById(R.id.edt_rule0),
                (MaterialEditText) findViewById(R.id.edt_rule1),
                (MaterialEditText) findViewById(R.id.edt_rule2),
                (MaterialEditText) findViewById(R.id.edt_rule3),
                (MaterialEditText) findViewById(R.id.edt_rule4),
                (MaterialEditText) findViewById(R.id.edt_rule5),
                (MaterialEditText) findViewById(R.id.edt_rule6),
                (MaterialEditText) findViewById(R.id.edt_rule7),
                (MaterialEditText) findViewById(R.id.edt_rule8),
                (MaterialEditText) findViewById(R.id.edt_rule9),};
        btn_add_rule = (Button) findViewById(R.id.btn_add_rule);
        btn_add_rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag>9){
                    Toast.makeText(RegisterBuilderStep2.this,"Only 10 rules can be added at the moment",Toast.LENGTH_LONG).show();
                    return;
                }
                editTextArray[flag].setVisibility(View.VISIBLE);
                flag++;
            }
        });
        btn_continue= (Button) findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i =0;i<10;i++)
                {
                    if(!editTextArray[i].getText().toString().equals("")){
                        rules.add(editTextArray[i].getText().toString());
                    }
                }
                startActivity(new Intent(RegisterBuilderStep2.this, Leadpage.class));
            }
        });
    }
}
