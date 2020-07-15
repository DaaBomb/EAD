package com.wrath.client.register;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.wrath.client.R;
import com.wrath.client.Retrofit.IMyService;
import com.wrath.client.Retrofit.RetrofitClient;
import com.wrath.client.dto.BaseResponse;
import com.wrath.client.dto.StaffSociety;
import com.wrath.client.dto.User;
import com.wrath.client.security.SecurityPage;
import com.wrath.client.user.Leadpage;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

public class RegisterStaff extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button btn_continue;
    SharedPreferences sharedPreferences;
    Gson gson = new Gson();
    String token;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_staff);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
        sharedPreferences = getSharedPreferences("swarm", MODE_PRIVATE);

        radioGroup = findViewById(R.id.radio_group);
        radioButton = findViewById(R.id.staff_man);

        btn_continue = (Button) findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = radioGroup.getCheckedRadioButtonId();
                if (radioId == radioButton.getId()) {
                    registerStaff("manager");
                } else {
                    registerStaff("security");
                }
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                token = instanceIdResult.getToken();
            }
        });
    }

    public void onRadioButtonClicked(View view) {
    }

    public void registerStaff(final String staff_role) {

        Bundle extras = getIntent().getExtras();
        String city = extras.getString("city");
        String society_name = extras.getString("society_name");
        User user = gson.fromJson(extras.getString("user"), User.class);
        user.setToken(token);
        StaffSociety staffSociety = new StaffSociety();
        staffSociety.setName(society_name);
        staffSociety.setProfession(staff_role.toLowerCase());
        staffSociety.setCity(city.trim());
        staffSociety.setUser(user);
        RequestBody request = RequestBody.create(MediaType.parse("application/json"), gson.toJson(staffSociety));
        compositeDisposable.add(iMyService.addStaff(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        BaseResponse res = gson.fromJson(response, BaseResponse.class);
                        Toast.makeText(RegisterStaff.this, "" + res.getMsg(), Toast.LENGTH_LONG).show();
                        if (res.getMsg().equals("successful")) {
                            Intent i;
                            if (staff_role.equalsIgnoreCase("manager")) {
                                i = new Intent(RegisterStaff.this, Leadpage.class);
                            } else {
                                i = new Intent(RegisterStaff.this, SecurityPage.class);
                            }
                            Bundle extras = new Bundle();
                            sharedPreferences.edit().putString("user", gson.toJson(res.getUser())).apply();
                            extras.putString("user", gson.toJson(res.getUser()));
                            i.putExtras(extras);
                            startActivity(i);
                        }
                    }
                })
        );

    }
}

