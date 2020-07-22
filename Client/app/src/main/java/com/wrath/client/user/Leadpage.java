package com.wrath.client.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.wrath.client.R;
import com.wrath.client.common.BaseNav;
import com.wrath.client.dto.NotificationDetails;
import com.wrath.client.security.SecurityRequestPage;
import com.wrath.client.user.announcement.AnnouncementsPage;
import com.wrath.client.user.concierge.ConciergeRequestsPage;
import com.wrath.client.user.event.EventsListPage;
import com.wrath.client.user.forum.ForumPage;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class Leadpage extends BaseNav {

    Button btn_forum;
    Button btn_event;
    Button btn_gate;
    Button btn_concierge;
    Button btn_announcements;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leadpage);

        setToolbar((Toolbar) findViewById(R.id.toolbar));
        setDrawerLayout((DrawerLayout) findViewById(R.id.drawer_layout));
        setNavigationView((NavigationView) findViewById(R.id.nav_view));
        initialize();

        btn_forum = (Button) findViewById(R.id.btn_forum);
        btn_event = (Button) findViewById(R.id.btn_event);
        btn_gate = (Button) findViewById(R.id.btn_gate);
        btn_concierge = (Button) findViewById(R.id.btn_concierge);
        btn_announcements = (Button) findViewById(R.id.btn_announcements);

        ImageButton panic = findViewById(R.id.panic);
        panic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                activatePanicProtocol();
                return false;
            }
        });

        btn_forum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Leadpage.this, ForumPage.class);
                Bundle extras = new Bundle();
                extras.putString("user", user);
                i.putExtras(extras);
                startActivity(i);
            }
        });

        btn_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Leadpage.this, EventsListPage.class);
                startActivity(i);
            }
        });

        btn_gate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Leadpage.this, SecurityRequestPage.class);
                startActivity(i);
            }
        });

        btn_concierge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Leadpage.this, ConciergeRequestsPage.class);
                startActivity(i);
            }
        });

        btn_announcements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Leadpage.this, AnnouncementsPage.class);
                startActivity(i);
            }
        });
    }

    public void activatePanicProtocol() {
        NotificationDetails notificationDetails = new NotificationDetails();
        notificationDetails.setUser_id(userObj.get_id());
        notificationDetails.setBlock_visiting(userObj.getAddress().getBlockname());
        notificationDetails.setSociety_id(userObj.getAddress().getSociety_id());
        notificationDetails.setFlatnum_visiting(userObj.getAddress().getFlatnum());
        RequestBody request = RequestBody.create(MediaType.parse("application/json"), gson.toJson(notificationDetails));
        compositeDisposable.add(iMyService.engagePanicSequence(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        Toast.makeText(Leadpage.this, "Successfully alerted panic situation", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(Leadpage.this, "Alerting panic failed", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                    }
                })
        );
    }
}