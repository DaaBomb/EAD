package com.wrath.client.user.announcement;

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
import com.wrath.client.dto.Announcement;
import com.wrath.client.dto.AnnouncementsResponse;
import com.wrath.client.util.RulesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class AnnouncementsPage extends BaseNav {

    RecyclerView recyclerView;
    AnnouncementRecyclerViewAdapter recyclerViewAdapter;
    List<Announcement> announcementList = new ArrayList<>();
    FloatingActionButton btn_add_announcement;
    boolean isLoading = false;
    private int flag = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcement_display);
        setToolbar((Toolbar) findViewById(R.id.toolbar));
        setDrawerLayout((DrawerLayout) findViewById(R.id.drawer_layout));
        setNavigationView((NavigationView) findViewById(R.id.nav_view));
        initialize();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        btn_add_announcement = findViewById(R.id.fab);
        if (!(RulesUtil.isBuilder(userObj) || RulesUtil.isManager(userObj))) {
            btn_add_announcement.setVisibility(View.INVISIBLE);
        }
        btn_add_announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AnnouncementsPage.this, AnnouncementForm.class);
                startActivity(i);
            }
        });

        populateData(userObj.getAddress().getSociety_id(), 1);
        initAdapter();
        initScrollListener();
    }

    private void populateData(String societyId, int flag) {
        compositeDisposable.add(iMyService.getSocietyAnnouncements(societyId, flag)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        AnnouncementsResponse res = gson.fromJson(s, AnnouncementsResponse.class);
                        announcementList.addAll(res.getAnnouncements());
                        recyclerViewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(AnnouncementsPage.this, "Unable to fetch announcements. Check network connectivity.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }

    private void initAdapter() {
        recyclerViewAdapter = new AnnouncementRecyclerViewAdapter(announcementList);
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
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == announcementList.size() - 1) {
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
        announcementList.add(null);
        recyclerViewAdapter.notifyItemInserted(announcementList.size() - 1);

        compositeDisposable.add(iMyService.getSocietyAnnouncements(societyId, flag)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        AnnouncementsResponse res = gson.fromJson(s, AnnouncementsResponse.class);
                        announcementList.remove(announcementList.size() - 1);
                        int scrollPosition = announcementList.size();
                        recyclerViewAdapter.notifyItemRemoved(scrollPosition);
                        announcementList.addAll(res.getAnnouncements());
                        recyclerViewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(AnnouncementsPage.this, "Unable to fetch announcements. Check network connectivity.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
        isLoading = false;
    }
}
