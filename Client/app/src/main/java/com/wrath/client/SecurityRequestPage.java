package com.wrath.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.wrath.client.Retrofit.IMyService;
import com.wrath.client.Retrofit.RetrofitClient;
import com.wrath.client.dto.NotificationDetails;
import com.wrath.client.dto.NotificationDetailsResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class SecurityRequestPage extends BaseNav {

    RecyclerView recyclerView;
    SecurityRecyclerViewAdapter recyclerViewAdapter;
    List<NotificationDetails> notificationDetailsList = new ArrayList<>();
    FloatingActionButton btn_add_topic;
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
        setContentView(R.layout.security_requests);
        setToolbar((Toolbar) findViewById(R.id.toolbar));
        setDrawerLayout((DrawerLayout) findViewById(R.id.drawer_layout));
        setNavigationView((NavigationView) findViewById(R.id.nav_view));
        initialize();
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        btn_add_topic = findViewById(R.id.fab);
        btn_add_topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SecurityRequestPage.this, SecurityPage.class);
                startActivity(i);
            }
        });
        if(userObj.getProfession()==null)
            populateUserData(userObj.getAddress().getSociety_id(), 1,userObj.getAddress().getBlockname(),userObj.getAddress().getFlatnum());
        else
            populateData(userObj.getAddress().getSociety_id(), 1);
        initAdapter();
        initScrollListener();
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recreate();
            }
        });
    }

    private void populateData(String societyId, int flag) {
        compositeDisposable.add(iMyService.getSecurityRequests(societyId, flag)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        NotificationDetailsResponse res = gson.fromJson(response, NotificationDetailsResponse.class);
                        if (res.getMsg().equalsIgnoreCase("successful"))
                            notificationDetailsList.addAll(res.getNotificationDetails());
                        recyclerViewAdapter.notifyDataSetChanged();
                    }
                })
        );
    }

    private void populateUserData(String societyId, int flag,String blockname,String flatnum) {
        compositeDisposable.add(iMyService.getSecurityUserRequests(societyId, flag,blockname,flatnum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        NotificationDetailsResponse res = gson.fromJson(response, NotificationDetailsResponse.class);
                        if (res.getMsg().equalsIgnoreCase("successful"))
                            notificationDetailsList.addAll(res.getNotificationDetails());
                        recyclerViewAdapter.notifyDataSetChanged();
                    }
                })
        );
    }

    private void initAdapter() {
        recyclerViewAdapter = new SecurityRecyclerViewAdapter(notificationDetailsList);
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
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == notificationDetailsList.size() - 1) {
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

        notificationDetailsList.add(null);
        recyclerViewAdapter.notifyItemInserted(notificationDetailsList.size() - 1);
        if(userObj.getProfession()==null){
            compositeDisposable.add(iMyService.getSecurityUserRequests(societyId, flag,userObj.getAddress().getBlockname(),userObj.getAddress().getFlatnum())
                    .subscribeOn(Schedulers.io())
                    .delay(3, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String response) throws Exception {
                            NotificationDetailsResponse res = gson.fromJson(response, NotificationDetailsResponse.class);
                            notificationDetailsList.remove(notificationDetailsList.size() - 1);
                            int scrollPosition = notificationDetailsList.size();
                            recyclerViewAdapter.notifyItemRemoved(scrollPosition);
                            if (res.getMsg().equalsIgnoreCase("successful")) {
                                notificationDetailsList.addAll(res.getNotificationDetails());
                                recyclerViewAdapter.notifyDataSetChanged();
                            }
                        }
                    })
            );
        }
        else{
            compositeDisposable.add(iMyService.getSecurityRequests(societyId, flag)
                    .subscribeOn(Schedulers.io())
                    .delay(3, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String response) throws Exception {
                            NotificationDetailsResponse res = gson.fromJson(response, NotificationDetailsResponse.class);
                            notificationDetailsList.remove(notificationDetailsList.size() - 1);
                            int scrollPosition = notificationDetailsList.size();
                            recyclerViewAdapter.notifyItemRemoved(scrollPosition);
                            if (res.getMsg().equalsIgnoreCase("successful")) {
                                notificationDetailsList.addAll(res.getNotificationDetails());
                                recyclerViewAdapter.notifyDataSetChanged();
                            }
                        }
                    })
            );
        }
        recyclerViewAdapter.notifyDataSetChanged();
        isLoading = false;
    }
}
