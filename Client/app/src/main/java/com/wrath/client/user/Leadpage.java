package com.wrath.client.user;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.wrath.client.R;
import com.wrath.client.common.BaseNav;
import com.wrath.client.security.SecurityRequestPage;
import com.wrath.client.user.event.EventsListPage;
import com.wrath.client.user.forum.ForumPage;

public class Leadpage extends BaseNav {

    Button btn_forum;

    Button btn_event;


    Button btn_gate;

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

        ImageButton panic = findViewById(R.id.panic);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.sound);
        panic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mp.start();
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
    }
}