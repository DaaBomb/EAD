package com.wrath.client;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wrath.client.Retrofit.IMyService;
import com.wrath.client.Retrofit.RetrofitClient;
import com.wrath.client.dto.BaseResponse;
import com.wrath.client.dto.SecurityRequest;
import com.wrath.client.dto.User;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

public class SecurityPage extends BaseNav {

    MaterialEditText visitor_name;
    MaterialEditText purpose;
    MaterialEditText block_name;
    MaterialEditText flat_num;
    Button approval;
    User user;
    Gson gson = new Gson();
    SharedPreferences sharedPreferences;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_page);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        visitor_name = (MaterialEditText) findViewById(R.id.edt_visitor_name);
        purpose = (MaterialEditText) findViewById(R.id.edt_purpose);
        block_name = (MaterialEditText) findViewById(R.id.edt_block_name);
        flat_num = (MaterialEditText) findViewById(R.id.edt_flat_num);
        approval = (Button) findViewById(R.id.btn_approval);

        sharedPreferences = getSharedPreferences("swarm", MODE_PRIVATE);
        user = gson.fromJson(sharedPreferences.getString("user", "{}"), User.class);

        setToolbar((Toolbar) findViewById(R.id.toolbar));
        setDrawerLayout((DrawerLayout) findViewById(R.id.drawer_layout));
        setNavigationView((NavigationView) findViewById(R.id.nav_view));
        initialize();


        approval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(visitor_name.getText().toString())) {
                    Toast.makeText(SecurityPage.this, "visitor name cannot be empty", Toast.LENGTH_LONG).show();
                }
                if (TextUtils.isEmpty(purpose.getText().toString())) {
                    Toast.makeText(SecurityPage.this, "purpose cannot be empty", Toast.LENGTH_LONG).show();
                }
                if (TextUtils.isEmpty(block_name.getText().toString())) {
                    Toast.makeText(SecurityPage.this, "block_name cannot be empty", Toast.LENGTH_LONG).show();
                }
                if (TextUtils.isEmpty(flat_num.getText().toString())) {
                    Toast.makeText(SecurityPage.this, "flat_num cannot be empty", Toast.LENGTH_LONG).show();
                }

                SecurityRequest securityRequest = new SecurityRequest();
                securityRequest.setVisitor_name(visitor_name.getText().toString());
                securityRequest.setBlockname(block_name.getText().toString());
                securityRequest.setFlatnum(flat_num.getText().toString());
                securityRequest.setToken(user.getToken());
                securityRequest.setUser(user);
                gateRegister(securityRequest);

            }
        });

    }

    public void gateRegister(SecurityRequest securityRequest){
        RequestBody request = RequestBody.create(MediaType.parse("application/json"), gson.toJson(securityRequest));
        compositeDisposable.add(iMyService.sendnotification(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        BaseResponse res = gson.fromJson(response, BaseResponse.class);
                        Toast.makeText(SecurityPage.this, "" + res.getMsg(), Toast.LENGTH_LONG).show();
                    }
                })
        );
    }
}
