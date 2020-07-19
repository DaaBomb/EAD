package com.wrath.client.user.event;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class EventsPage extends BaseNav implements CompetitonRecyclerViewAdapter.OnProgrammeListener {
    TextView eventTitle, description, date, time, food_title, veg_btn, nonveg_btn;
    RecyclerView recyclerView;
    CompetitonRecyclerViewAdapter recyclerViewAdapter;
    Boolean veg_selected = false;
    Boolean nonveg_selected = false;
    List<String> interestedCompetitions = new ArrayList<>();
    List<Programme> programmeList;
    EventDetails eventDetails;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_specific);
        setToolbar((Toolbar) findViewById(R.id.toolbar));
        setDrawerLayout((DrawerLayout) findViewById(R.id.drawer_layout));
        setNavigationView((NavigationView) findViewById(R.id.nav_view));
        initialize();
        Bundle extras = getIntent().getExtras();
        eventDetails = gson.fromJson(extras.getString("eventDetails"), EventDetails.class);
        eventTitle = (TextView) findViewById(R.id.textView7);
        description = (TextView) findViewById(R.id.event_details);
        date = (TextView) findViewById(R.id.textView9);
        time = (TextView) findViewById(R.id.textView10);
        food_title = (TextView) findViewById(R.id.textView11);
        veg_btn = (Button) findViewById(R.id.button9);
        nonveg_btn = (Button) findViewById(R.id.button5);
        veg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                veg_selected = true;
                nonveg_selected = false;
                veg_btn.setBackgroundColor(Color.parseColor("#00b300"));
                veg_btn.setTextColor(Color.parseColor("#FFFFFF"));
                nonveg_btn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                nonveg_btn.setTextColor(Color.parseColor("#e00000"));
            }
        });
        nonveg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nonveg_selected = true;
                veg_selected = false;
                nonveg_btn.setBackgroundColor(Color.parseColor("#e00000"));
                nonveg_btn.setTextColor(Color.parseColor("#FFFFFF"));
                veg_btn.setTextColor(Color.parseColor("#00b300"));
                veg_btn.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });
        if (!eventDetails.getFood_choice()) {
            food_title.setVisibility(View.GONE);
            veg_btn.setVisibility(View.GONE);
            nonveg_btn.setVisibility(View.GONE);
        }
        eventTitle.setText(eventDetails.getName());
        description.setText(eventDetails.getDescription());
        String pattern = "EEE, d MMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String dateString = simpleDateFormat.format(eventDetails.getStart_date());
        date.setText(dateString);
        time.setText(eventDetails.getTime());

        programmeList = eventDetails.getProgrammes();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        initialize();
        initList(eventDetails);
        initAdapter();
        initScrollListener();
    }

    private void initList(EventDetails eventDetails) {
        for (Programme programme : eventDetails.getProgrammes()) {
            if (programme.getParticipants().contains(userObj.get_id())) {
                interestedCompetitions.add(programme.getName());
            }
        }
    }

    private void initAdapter() {
        recyclerViewAdapter = new CompetitonRecyclerViewAdapter(programmeList, this,userObj);
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
        final int pos = position;
        final Button participate_btn = recyclerView.getChildAt(position).findViewById(R.id.participate_btn);
        if (participate_btn.getText().equals("Yes, I'm participating")) {
            participate_btn.setText("I won't be participating");
            if (!interestedCompetitions.contains(programmeList.get(pos).getName()))
                interestedCompetitions.add(programmeList.get(pos).getName());
        } else {
            participate_btn.setText("Yes, I'm participating");
            if (interestedCompetitions.contains(programmeList.get(pos).getName()))
                interestedCompetitions.remove(programmeList.get(pos).getName());
        }

    }
}
