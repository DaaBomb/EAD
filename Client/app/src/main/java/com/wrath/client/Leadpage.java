package com.wrath.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.wrath.client.dto.SecurityRequest;

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
                Intent i = new Intent(Leadpage.this, EventsPage.class);
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