package com.wrath.client.user.event;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.wrath.client.R;
import com.wrath.client.common.BaseNav;
import com.wrath.client.dto.EventDetails;
import com.wrath.client.dto.Programme;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EventsPage extends BaseNav implements CompetitonRecyclerViewAdapter.OnProgrammeListener{
    TextView eventTitle,description,date,time;
    RecyclerView recyclerView;
    CompetitonRecyclerViewAdapter recyclerViewAdapter;

    List<String> interestedCompetitions = new ArrayList<>();
    List<Programme> programmeList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_specific);
        setToolbar((Toolbar) findViewById(R.id.toolbar));
        setDrawerLayout((DrawerLayout) findViewById(R.id.drawer_layout));
        setNavigationView((NavigationView) findViewById(R.id.nav_view));
        initialize();
        Bundle extras = getIntent().getExtras();
        EventDetails eventDetails = gson.fromJson(extras.getString("eventDetails"),EventDetails.class);
        eventTitle = (TextView) findViewById(R.id.textView7);
        description = (TextView) findViewById(R.id.event_details);
        date = (TextView) findViewById(R.id.textView9);
        time = (TextView) findViewById(R.id.textView10);
        eventTitle.setText(eventDetails.getName());
        description.setText(eventDetails.getDescription());
        String pattern ="EEE, d MMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String dateString = simpleDateFormat.format(eventDetails.getStart_date());
        date.setText(dateString);
        time.setText(eventDetails.getTime());
        programmeList = eventDetails.getProgrammes();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        initialize();
        initAdapter();
        initScrollListener();
    }

    private void initAdapter() {
        recyclerViewAdapter = new CompetitonRecyclerViewAdapter(programmeList, this);
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
    @Override
    public void onProgrammeClick(int position) {
        interestedCompetitions.add(programmeList.get(position).getName());
    }
}
