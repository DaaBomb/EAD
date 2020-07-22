package com.wrath.client.user.concierge;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.wrath.client.R;
import com.wrath.client.common.BaseNav;
import com.wrath.client.dto.Concierge;
import com.wrath.client.dto.ConciergeDetailsResponse;
import com.wrath.client.util.RulesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class ConciergeRequestsPage extends BaseNav implements ConciergeRecyclerViewAdapter.OnConciergeListener{

    RecyclerView recyclerView;
    ConciergeRecyclerViewAdapter recyclerViewAdapter;
    List<Concierge> conciergeList = new ArrayList<>();
    FloatingActionButton btn_add_concierge;
    boolean isLoading = false;
    private int flag = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.concierge_manager);
        setToolbar((Toolbar) findViewById(R.id.toolbar));
        setDrawerLayout((DrawerLayout) findViewById(R.id.drawer_layout));
        setNavigationView((NavigationView) findViewById(R.id.nav_view));
        initialize();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        btn_add_concierge = findViewById(R.id.fab);
        if (!RulesUtil.isResident(userObj)) {
            btn_add_concierge.setVisibility(View.INVISIBLE);
        }
        btn_add_concierge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ConciergeRequestsPage.this, ConciergeRequestForm.class);
                startActivity(i);
            }
        });

        if (userObj.getProfession() == null)
            populateUserData(userObj.getAddress().getSociety_id(), 1, userObj.getAddress().getBlockname(), userObj.getAddress().getFlatnum());
        else
            populateData(userObj.getAddress().getSociety_id(), 1);
        initAdapter();
        initScrollListener();
    }

    private void populateData(String societyId, int flag) {
        compositeDisposable.add(iMyService.getSocietyConcierge(societyId, flag)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        ConciergeDetailsResponse res = gson.fromJson(s, ConciergeDetailsResponse.class);
                        conciergeList.addAll(res.getConcierge());
                        recyclerViewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ConciergeRequestsPage.this, "Unable to fetch concierges. Check network connectivity.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }

    private void populateUserData(String societyId, int flag, String blockname, String flatnum) {
        compositeDisposable.add(iMyService.getFlatConcierge(societyId, flag, blockname, flatnum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        ConciergeDetailsResponse res = gson.fromJson(s, ConciergeDetailsResponse.class);
                        conciergeList.addAll(res.getConcierge());
                        recyclerViewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ConciergeRequestsPage.this, "Unable to fetch concierges. Check network connectivity.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }

    private void initAdapter() {
        recyclerViewAdapter = new ConciergeRecyclerViewAdapter(conciergeList, this);
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
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == conciergeList.size() - 1) {
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
        conciergeList.add(null);
        recyclerViewAdapter.notifyItemInserted(conciergeList.size() - 1);
        if (userObj.getProfession() == null) {
            compositeDisposable.add(iMyService.getFlatConcierge(societyId, flag, userObj.getAddress().getBlockname(), userObj.getAddress().getFlatnum())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<String>() {
                        @Override
                        public void onNext(String s) {
                            ConciergeDetailsResponse res = gson.fromJson(s, ConciergeDetailsResponse.class);
                            conciergeList.remove(conciergeList.size() - 1);
                            int scrollPosition = conciergeList.size();
                            recyclerViewAdapter.notifyItemRemoved(scrollPosition);
                            conciergeList.addAll(res.getConcierge());
                            recyclerViewAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(ConciergeRequestsPage.this, "Unable to fetch concierges. Check network connectivity.", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onComplete() {

                        }
                    })
            );
        } else {
            compositeDisposable.add(iMyService.getSocietyConcierge(societyId, flag)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<String>() {
                        @Override
                        public void onNext(String s) {
                            ConciergeDetailsResponse res = gson.fromJson(s, ConciergeDetailsResponse.class);
                            conciergeList.remove(conciergeList.size() - 1);
                            int scrollPosition = conciergeList.size();
                            recyclerViewAdapter.notifyItemRemoved(scrollPosition);
                            conciergeList.addAll(res.getConcierge());
                            recyclerViewAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(ConciergeRequestsPage.this, "Unable to fetch concierges. Check network connectivity.", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onComplete() {

                        }
                    })
            );
        }
        isLoading = false;
    }

    @Override
    public void onConciergeClick(int position) {
        Intent i = new Intent(ConciergeRequestsPage.this, IndividualConciergePage.class);
        Bundle extras = new Bundle();
        extras.putString("conciergeDetails",gson.toJson(conciergeList.get(position)));
        i.putExtras(extras);
        startActivity(i);
    }
}
