package com.wrath.client;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wrath.client.Retrofit.IMyService;
import com.wrath.client.Retrofit.RetrofitClient;
import com.wrath.client.dto.CitiesResponse;
import com.wrath.client.dto.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class SelectCommunity extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;
    ArrayList<String> items = new ArrayList<>();
    ArrayList<String> communities = new ArrayList<>();
    Button btn_city;
    Button btn_community;
    Button btn_continue;
    String city;
    String society_name;
    SpinnerDialog spinnerDialogcommunity;
    SpinnerDialog spinnerDialog;

    Gson gson = new Gson();

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_community);
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        Bundle extras = getIntent().getExtras();
        String jsonString = extras.getString("user");
        final String staff = extras.getString("staff");
        final User user = gson.fromJson(jsonString, User.class);

        initItems();
        spinnerDialogcommunity = new SpinnerDialog(SelectCommunity.this, communities, "select community name");
        spinnerDialog = new SpinnerDialog(SelectCommunity.this, items, "select city");

        btn_city = (Button) findViewById(R.id.btn_city);
        btn_community = (Button) findViewById(R.id.btn_community);
        btn_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshSpinnerDialog();
                spinnerDialog.showSpinerDialog();
            }
        });
        btn_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(city)) {
                    Toast.makeText(SelectCommunity.this, "Select a city to view communities", Toast.LENGTH_SHORT).show();
                    return;
                }
                refreshSpinnerDialogCommunity();
                spinnerDialogcommunity.showSpinerDialog();
            }
        });
        btn_continue = (Button) findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(city)) {
                    Toast.makeText(SelectCommunity.this, "Please select a city to proceed", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(society_name)) {
                    Toast.makeText(SelectCommunity.this, "Please select a society to proceed", Toast.LENGTH_SHORT).show();
                    return;
                }

                Bundle extras = new Bundle();
                extras.putString("user", gson.toJson(user));
                extras.putString("city", city);
                extras.putString("society_name", society_name);

                if (staff.equals("0")) {
                    Intent k = new Intent(SelectCommunity.this, ResidentRegister.class);
                    k.putExtras(extras);
                    startActivity(k);
                }
                if (staff.equals("1")) {
                    Intent k = new Intent(SelectCommunity.this, RegisterStaff.class);
                    k.putExtras(extras);
                    startActivity(k);
                }

            }
        });
    }

    private void initItems() {
        compositeDisposable.add(iMyService.getCities()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        CitiesResponse res = gson.fromJson(response, CitiesResponse.class);
                        items = (ArrayList<String>) res.getCities();
                    }
                })
        );
    }

    public void changeCommunities(String city) {
        compositeDisposable.add(iMyService.changeSocieties(city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        JSONObject res = new JSONObject(response);
                        JSONArray societies = res.getJSONArray("societies");
                        communities = new ArrayList<>();
                        if (societies.length() > 0) {
                            int length = societies.length();
                            for (int i = 0; i < length; i++) {
                                JSONObject temp = societies.getJSONObject(i);
                                communities.add(temp.getString("name"));
                            }
                        }
                    }
                })
        );
    }

    public void refreshSpinnerDialogCommunity() {
        spinnerDialogcommunity = new SpinnerDialog(SelectCommunity.this, communities, "select community name");
        spinnerDialogcommunity.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                society_name = item;
                btn_community.setText(society_name);
                Toast.makeText(SelectCommunity.this, "Selected: " + item, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void refreshSpinnerDialog() {
        spinnerDialog = new SpinnerDialog(SelectCommunity.this, items, "select city");
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                city = item;
                btn_city.setText(city);
                btn_community.setText("Select community");
                society_name = null;
                changeCommunities(city);
                Toast.makeText(SelectCommunity.this, "Selected: " + item, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
