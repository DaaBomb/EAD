package com.wrath.client.user.event;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.wrath.client.R;
import com.wrath.client.Retrofit.IMyService;
import com.wrath.client.Retrofit.RetrofitClient;
import com.wrath.client.common.BaseNav;
import com.wrath.client.dto.EventDetails;
import com.wrath.client.dto.Programme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Retrofit;

public class EventsListPage extends BaseNav implements EventRecyclerViewAdapter.OnEventListener {

    RecyclerView recyclerView;
    EventRecyclerViewAdapter recyclerViewAdapter;
    List<EventDetails> eventDetailsList = new ArrayList<>();
    FloatingActionButton btn_add_event;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;
    private SwipeRefreshLayout swipeContainer;
    boolean isLoading = false;
    private int flag = 1;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_recycler_page);
        setToolbar((Toolbar) findViewById(R.id.toolbar));
        setDrawerLayout((DrawerLayout) findViewById(R.id.drawer_layout));
        setNavigationView((NavigationView) findViewById(R.id.nav_view));
        initialize();
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        btn_add_event = findViewById(R.id.fab);
        if (userObj.getProfession() == null) {
            btn_add_event.setVisibility(View.INVISIBLE);
        }
        btn_add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EventsListPage.this, EventFormPage2.class);
                startActivity(i);
            }
        });
        initAdapter();
        initScrollListener();
        if (userObj.getProfession() == null)
            populateUserData(userObj.getAddress().getSociety_id(), 1, userObj.getAddress().getBlockname(), userObj.getAddress().getFlatnum());
        else
            populateData(userObj.getAddress().getSociety_id(), 1);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recreate();
            }
        });
    }

    private void populateData(String societyId, int flag) {
        Programme dummyProgramme = new Programme("running race",Arrays.asList("1", "2"));
        List<Programme> arr = Arrays.asList(dummyProgramme);
        List<EventDetails> dummy = Arrays.asList(new EventDetails("Diwali", userObj.getAddress().getSociety_id(), new Date(), "description", "creator_name", Arrays.asList("1", "2"), true, arr));
        eventDetailsList.addAll(dummy);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void populateUserData(String societyId, int flag, String blockname, String flatnum) {
        Programme dummyProgramme = new Programme("running race", Arrays.asList("1", "2"));
        List<Programme> arr = Arrays.asList(dummyProgramme);
        List<EventDetails> dummy = Arrays.asList(new EventDetails("Diwali", userObj.getAddress().getSociety_id(), new Date(), "description", "creator_name", Arrays.asList("1", "2"), true, arr));
        eventDetailsList.addAll(dummy);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void initAdapter() {
        recyclerViewAdapter = new EventRecyclerViewAdapter(eventDetailsList, this);
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

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == eventDetailsList.size() - 1) {
                        //bottom of list!
                        flag++;
                        loadMore(userObj.getAddress().getSociety_id(), flag);
                        isLoading = true;
                    }
                }
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

    private void loadMore(String societyId, int flag) {
        isLoading = false;
    }

    @Override
    public void onEventClick(int position) {
        Toast.makeText(EventsListPage.this, "" + position, Toast.LENGTH_LONG).show();
    }
}
