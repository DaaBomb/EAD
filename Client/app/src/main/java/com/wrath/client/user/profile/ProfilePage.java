package com.wrath.client.user.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.wrath.client.R;
import com.wrath.client.common.BaseNav;
import com.wrath.client.dto.SocietyNameDto;
import com.wrath.client.dto.User;
import com.wrath.client.dto.UserAddress;
import com.wrath.client.dto.UsersResponse;
import com.wrath.client.user.residentBook.ResidentBookRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ProfilePage extends BaseNav implements ProfileRecyclerViewAdapter.OnProfileListener {

    User userProfile;
    RecyclerView residentsRecyclerView;
    ProfileRecyclerViewAdapter residentsRecyclerViewAdapter;
    List<User> usersList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        setToolbar((Toolbar) findViewById(R.id.toolbar));
        setDrawerLayout((DrawerLayout) findViewById(R.id.drawer_layout));
        setNavigationView((NavigationView) findViewById(R.id.nav_view));
        initialize();

        Bundle bundle = getIntent().getExtras();
        userProfile = gson.fromJson(bundle.getString("userProfile"), User.class);
        getSocietyName();
        getResidents();

        TextView name = findViewById(R.id.textView);
        TextView flat = findViewById(R.id.textView2);
        TextView email = findViewById(R.id.email);
        residentsRecyclerView = findViewById(R.id.recyclerView);

        name.setText(userProfile.getName());
        if (userProfile.getAddress().getBlockname() != null && userProfile.getAddress().getFlatnum() != null)
            flat.setText(userProfile.getAddress().getBlockname() + "-" + userProfile.getAddress().getFlatnum());
        else
            flat.setText("");
        email.setText(userProfile.getEmail());

        initAdapter();
    }

    private void getSocietyName() {
        compositeDisposable.add(iMyService.getSocietyName(userProfile.getAddress().getSociety_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        SocietyNameDto societyNameDto = gson.fromJson(s, SocietyNameDto.class);
                        TextView society = findViewById(R.id.textView3);
                        society.setText(societyNameDto.getSociety().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getBaseContext(), "Please check network connectivity", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                    }
                })
        );
    }

    private void getResidents() {
        RequestBody request = RequestBody.create(MediaType.parse("application/json"), gson.toJson(userProfile.getAddress()));
        compositeDisposable.add(iMyService.getResidentsInSameFlat(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        UsersResponse res = gson.fromJson(s, UsersResponse.class);
                        usersList.addAll(res.getUsers());
                        residentsRecyclerViewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getBaseContext(), "Please check network connectivity", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                    }
                })
        );
    }

    private void initAdapter() {
        residentsRecyclerViewAdapter = new ProfileRecyclerViewAdapter(usersList, this);
        residentsRecyclerView.setAdapter(residentsRecyclerViewAdapter);
        residentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onProfileClick(int position) {
        Intent i = new Intent(this, ProfilePage.class);
        Bundle extras = new Bundle();
        extras.putString("userProfile", gson.toJson(usersList.get(position)));
        i.putExtras(extras);
        startActivity(i);
    }
}
