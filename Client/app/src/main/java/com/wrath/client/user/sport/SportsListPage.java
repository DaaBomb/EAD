package com.wrath.client.user.sport;

import android.content.Intent;
import android.os.Bundle;
import android.text.style.BulletSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.wrath.client.R;
import com.wrath.client.common.BaseNav;
import com.wrath.client.dto.GetSportsResponse;
import com.wrath.client.dto.RegisterSportRequest;
import com.wrath.client.dto.Sport;
import com.wrath.client.util.RulesUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SportsListPage extends BaseNav implements SportsRecyclerViewAdapter.OnSportListener {

    RecyclerView recyclerView;
    SportsRecyclerViewAdapter recyclerViewAdapter;
    List<Sport> sportList = new ArrayList<>();
    FloatingActionButton btn_add_sport;
    boolean isLoading = false;
    private int flag = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sporting_events);
        setToolbar((Toolbar) findViewById(R.id.toolbar));
        setDrawerLayout((DrawerLayout) findViewById(R.id.drawer_layout));
        setNavigationView((NavigationView) findViewById(R.id.nav_view));
        initialize();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        btn_add_sport = findViewById(R.id.fab);
        if (!RulesUtil.isResident(userObj)) {
            btn_add_sport.setVisibility(View.INVISIBLE);
        }
        btn_add_sport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SportsListPage.this, SportRequestForm.class);
                startActivity(i);
            }
        });

        populateData(userObj.getAddress().getSociety_id(), 1);
        initAdapter();
        initScrollListener();
    }

    private void populateData(String societyId, int flag) {
        compositeDisposable.add(iMyService.getSports(societyId, flag)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        GetSportsResponse res = gson.fromJson(s, GetSportsResponse.class);
                        sportList.clear();
                        sportList.addAll(res.getSports());
                        recyclerViewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(SportsListPage.this, "Unable to fetch sports. Check network connectivity.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }

    private void initAdapter() {
        recyclerViewAdapter = new SportsRecyclerViewAdapter(sportList, this, userObj);
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
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == sportList.size() - 1) {
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
        sportList.add(null);
        recyclerViewAdapter.notifyItemInserted(sportList.size() - 1);

        compositeDisposable.add(iMyService.getSports(societyId, flag)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        GetSportsResponse res = gson.fromJson(s, GetSportsResponse.class);
                        sportList.remove(sportList.size() - 1);
                        int scrollPosition = sportList.size();
                        recyclerViewAdapter.notifyItemRemoved(scrollPosition);
                        sportList.addAll(res.getSports());
                        recyclerViewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(SportsListPage.this, "Unable to fetch sports. Check network connectivity.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
        isLoading = false;
    }

    @Override
    public void onParticipateClick(int position) {
        RegisterSportRequest registerSportRequest = new RegisterSportRequest();
        registerSportRequest.set_id(sportList.get(position).get_id());
        registerSportRequest.setUser(userObj);
        RequestBody request = RequestBody.create(MediaType.parse("application/json"), gson.toJson(registerSportRequest));

        compositeDisposable.add(iMyService.registerForSport(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        Toast.makeText(SportsListPage.this, "Sport request Successful.", Toast.LENGTH_LONG).show();
                        populateData(userObj.getAddress().getSociety_id(), 1);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(SportsListPage.this, "Sport request failed. Try after sometime", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                    }
                })
        );
    }

    @Override
    public void onSportClick(int position) {
        Intent i = new Intent(SportsListPage.this, ParticipantsListPage.class);
        Bundle extras = new Bundle();
        extras.putString("sport", gson.toJson(sportList.get(position)));
        i.putExtras(extras);
        startActivity(i);
    }
}
