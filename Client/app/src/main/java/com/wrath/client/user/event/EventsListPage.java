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
import com.wrath.client.dto.GetEventsResponse;
import com.wrath.client.dto.NotificationDetailsResponse;
import com.wrath.client.dto.Programme;
import com.wrath.client.dto.TopicsResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
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
                Intent i = new Intent(EventsListPage.this, EventFormPage.class);
                startActivity(i);
            }
        });
        initAdapter();
        initScrollListener();
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
        compositeDisposable.add(iMyService.getEvents(societyId, flag)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        GetEventsResponse res = gson.fromJson(response, GetEventsResponse.class);
                        if (res.getMsg().equalsIgnoreCase("successful"))
                            eventDetailsList.addAll(res.getEventDetails());
                        recyclerViewAdapter.notifyDataSetChanged();
                    }
                })
        );
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

        eventDetailsList.add(null);
        recyclerViewAdapter.notifyItemInserted(eventDetailsList.size() - 1);

        compositeDisposable.add(iMyService.getEvents(societyId, flag)
                .subscribeOn(Schedulers.io())
                .delay(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        GetEventsResponse res = gson.fromJson(response, GetEventsResponse.class);
                        eventDetailsList.remove(eventDetailsList.size() - 1);
                        int scrollPosition = eventDetailsList.size();
                        recyclerViewAdapter.notifyItemRemoved(scrollPosition);
                        if (res.getMsg().equalsIgnoreCase("successful")) {
                            eventDetailsList.addAll(res.getEventDetails());
                            recyclerViewAdapter.notifyDataSetChanged();
                        }
                    }
                })
        );

        recyclerViewAdapter.notifyDataSetChanged();
        isLoading = false;
    }

    @Override
    public void onEventClick(int position) {
        Intent i;
        if(userObj.getProfession()==null)
            i = new Intent(EventsListPage.this,EventsPage.class);
        else
            i = new Intent(EventsListPage.this,EventStatsPage.class);
        Bundle extras = new Bundle();
        extras.putString("eventDetails",gson.toJson(eventDetailsList.get(position)));
        i.putExtras(extras);
        startActivity(i);
    }
}
