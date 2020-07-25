package com.wrath.client.user.residentBook;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.wrath.client.R;
import com.wrath.client.common.BaseNav;
import com.wrath.client.dto.User;
import com.wrath.client.dto.UsersResponse;
import com.wrath.client.user.chat.ChatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ResidentBookPage extends BaseNav implements ResidentBookRecyclerViewAdapter.OnChatClickListener {

    RecyclerView recyclerView;
    ResidentBookRecyclerViewAdapter recyclerViewAdapter;
    List<User> usersList = new ArrayList<>();
    List<User> participantsList = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resident_book_display);
        setToolbar((Toolbar) findViewById(R.id.toolbar));
        setDrawerLayout((DrawerLayout) findViewById(R.id.drawer_layout));
        setNavigationView((NavigationView) findViewById(R.id.nav_view));
        initialize();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                usersList.clear();
                for(User participant: participantsList) {
                    if(participant.getName().contains(newText)) {
                        usersList.add(participant);
                    }
                }
                recyclerViewAdapter.notifyDataSetChanged();
                return true;
            }
        });
        populateData(userObj.getAddress().getSociety_id());
        initAdapter();
        initScrollListener();
    }

    private void populateData(String societyId) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("society_id",societyId);
        RequestBody request = RequestBody.create(MediaType.parse("application/json"), gson.toJson(requestBody));
        compositeDisposable.add(iMyService.getResidentsInSociety(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        UsersResponse res = gson.fromJson(s, UsersResponse.class);
                        usersList.addAll(res.getUsers());
                        participantsList.addAll(res.getUsers());
                        recyclerViewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ResidentBookPage.this, "Unable to fetch residents info. Check network connectivity.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }

    private void initAdapter() {
        recyclerViewAdapter = new ResidentBookRecyclerViewAdapter(usersList, this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    @Override
    public void onChatClick(int position) {
        Bundle extras = new Bundle();
        extras.putString("chatTo", gson.toJson(usersList.get(position)));
        Intent i = new Intent(ResidentBookPage.this, ChatActivity.class);
        i.putExtras(extras);
        startActivity(i);
    }
}
