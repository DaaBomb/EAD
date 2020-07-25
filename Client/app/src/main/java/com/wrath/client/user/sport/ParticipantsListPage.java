package com.wrath.client.user.sport;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.wrath.client.R;
import com.wrath.client.common.BaseNav;
import com.wrath.client.dto.Sport;
import com.wrath.client.dto.User;
import com.wrath.client.user.profile.ProfilePage;

import java.util.ArrayList;
import java.util.List;

public class ParticipantsListPage extends BaseNav implements ParticipantRecyclerViewAdapter.OnProfileListener {

    RecyclerView recyclerView;
    ParticipantRecyclerViewAdapter participantRecyclerViewAdapter;
    List<User> usersList = new ArrayList<>();
    List<User> participantsList = new ArrayList<>();
    Sport sport;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_users);

        setToolbar((Toolbar) findViewById(R.id.toolbar));
        setDrawerLayout((DrawerLayout) findViewById(R.id.drawer_layout));
        setNavigationView((NavigationView) findViewById(R.id.nav_view));
        initialize();

        Bundle bundle = getIntent().getExtras();
        sport = gson.fromJson(bundle.getString("sport"), Sport.class);
        usersList.addAll(sport.getParticipants());
        participantsList.addAll(sport.getParticipants());

        recyclerView = findViewById(R.id.recyclerView);
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
                    if(participant.getName().toLowerCase().contains(newText.toLowerCase())) {
                        usersList.add(participant);
                    }
                }
                participantRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            }
        });

        initAdapter();
        initScrollListener();
    }

    private void initAdapter() {
        participantRecyclerViewAdapter = new ParticipantRecyclerViewAdapter(usersList, this);
        recyclerView.setAdapter(participantRecyclerViewAdapter);
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
    public void onProfileClick(int position) {
        Intent i = new Intent(this, ProfilePage.class);
        Bundle extras = new Bundle();
        extras.putString("userProfile", gson.toJson(usersList.get(position)));
        i.putExtras(extras);
        startActivity(i);
    }
}
