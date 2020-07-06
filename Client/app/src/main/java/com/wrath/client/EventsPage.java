package com.wrath.client;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class EventsPage extends BaseNav {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events);

//        setToolbar((Toolbar) findViewById(R.id.toolbar));
//        setDrawerLayout((DrawerLayout) findViewById(R.id.drawer_layout));
//        setNavigationView((NavigationView) findViewById(R.id.nav_view));
//        initialize();
    }
}
