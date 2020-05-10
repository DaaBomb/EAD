package com.wrath.client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.wrath.client.Retrofit.IMyService;
import com.wrath.client.Retrofit.RetrofitClient;
import com.wrath.client.dto.BaseResponse;
import com.wrath.client.dto.BuilderSociety;
import com.wrath.client.dto.User;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

public class RegisterBuilder extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;
    Button btn_continue;
    MaterialEditText edt_society_name;
    MaterialEditText edt_address;
    MaterialEditText edt_city;
    SharedPreferences sharedPreferences;
    String token;

    Gson gson = new Gson();

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_builder);
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        Bundle extras = getIntent().getExtras();
        final User user = gson.fromJson(extras.getString("user"), User.class);

        sharedPreferences = getSharedPreferences("swarm", MODE_PRIVATE);
        edt_society_name = (MaterialEditText) findViewById(R.id.edt_aprt_name);
        edt_address = (MaterialEditText) findViewById(R.id.edt_aprt_address);
        edt_city = (MaterialEditText) findViewById(R.id.edt_aprt_city);
        btn_continue = (Button) findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerSociety(
                        user,
                        edt_society_name.getText().toString(),
                        edt_address.getText().toString(),
                        edt_city.getText().toString()
                );
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                token = instanceIdResult.getToken();
            }
        });
    }

    public void registerSociety(User user, String society_name, String address, String city) {
        if (TextUtils.isEmpty(edt_society_name.getText().toString())) {
            Toast.makeText(RegisterBuilder.this, "Society name cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(edt_address.getText().toString())) {
            Toast.makeText(RegisterBuilder.this, "Address cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(edt_city.getText().toString())) {
            Toast.makeText(RegisterBuilder.this, "City name cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        BuilderSociety builderSociety = new BuilderSociety();
        builderSociety.setName(society_name);
        builderSociety.setAddress(address);
        builderSociety.setCity(city);
        user.setToken(token);
        builderSociety.setUser(user);
        RequestBody request = RequestBody.create(MediaType.parse("application/json"), gson.toJson(builderSociety));
        compositeDisposable.add(iMyService.addBuilder(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        BaseResponse res = gson.fromJson(response, BaseResponse.class);
                        Toast.makeText(RegisterBuilder.this, "" + res.getMsg(), Toast.LENGTH_LONG).show();
                        if (res.getMsg().equals("successful")) {
                            Intent i = new Intent(RegisterBuilder.this, Leadpage.class);
                            sharedPreferences.edit().putString("user", gson.toJson(res.getUser())).apply();
                            Bundle extras = new Bundle();
                            extras.putString("user", gson.toJson(res.getUser()));
                            i.putExtras(extras);
                            startActivity(i);
                        }
                    }
                })
        );
    }
}
