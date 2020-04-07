package com.example.client;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.client.Retrofit.IMyService;
import com.example.client.Retrofit.RetrofitClient;
import com.example.client.dto.BaseResponse;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class Register extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;
    MaterialEditText edt_register_email;
    MaterialEditText edt_register_name;
    MaterialEditText edt_register_password;
    Button btn_register;
    TextView txt_login;

    Gson gson = new Gson();

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
        edt_register_email = (MaterialEditText) findViewById(R.id.edt_email);
        edt_register_name = (MaterialEditText) findViewById(R.id.edt_name);
        edt_register_password = (MaterialEditText) findViewById(R.id.edt_password);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser(
                        edt_register_email.getText().toString(),
                        edt_register_name.getText().toString(),
                        edt_register_password.getText().toString()
                );
            }
        });
        txt_login = (TextView) findViewById(R.id.txt_login);
        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, MainActivity.class));
            }
        });
    }

    private void registerUser(String email, String name, String password) {
        if (TextUtils.isEmpty(edt_register_email.getText().toString())) {
            Toast.makeText(Register.this, "Email cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(edt_register_name.getText().toString())) {
            Toast.makeText(Register.this, "name cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(edt_register_password.getText().toString())) {
            Toast.makeText(Register.this, "password cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }

        compositeDisposable.add(iMyService.registerUser(email, name, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        BaseResponse res = gson.fromJson(response, BaseResponse.class);
                        Toast.makeText(Register.this, "" + res.getMsg(), Toast.LENGTH_LONG).show();
                        if (res.getMsg().equals("successful") || res.getMsg().equals("Confirmation email resent, maybe check your spam?")) {
                            Intent i = new Intent(Register.this, MainActivity.class);
                            startActivity(i);
                        }
                    }
                })
        );
    }
}
