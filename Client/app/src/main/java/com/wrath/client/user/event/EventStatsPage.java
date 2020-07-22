package com.wrath.client.user.event;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.BaseBundle;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.wrath.client.R;
import com.wrath.client.common.BaseNav;
import com.wrath.client.dto.EventDetails;

import java.text.SimpleDateFormat;

public class EventStatsPage extends BaseNav implements CompetitionStatsRecyclerViewAdapter.OnProgrammeListener{
    EventDetails eventDetails;
    RecyclerView recyclerView;
    CompetitionStatsRecyclerViewAdapter recyclerViewAdapter;
    TextView event_title,description,date,time,attendees_count,veg_count,nonveg_count;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_stats);
        setToolbar((Toolbar) findViewById(R.id.toolbar));
        setDrawerLayout((DrawerLayout) findViewById(R.id.drawer_layout));
        setNavigationView((NavigationView) findViewById(R.id.nav_view));
        initialize();
        Bundle extras = getIntent().getExtras();
        eventDetails = gson.fromJson(extras.getString("eventDetails"), EventDetails.class);
        event_title =(TextView)findViewById(R.id.textView7) ;
        description= (TextView)findViewById(R.id.event_details) ;
        date = (TextView) findViewById(R.id.textView9);
        time= (TextView)findViewById(R.id.textView10);
        attendees_count= (TextView)findViewById(R.id.attendees_count) ;
        veg_count= (TextView)findViewById(R.id.veg_count);
        nonveg_count= (TextView)findViewById(R.id.nonveg_count);
        recyclerView =(RecyclerView) findViewById(R.id.recyclerView);
        event_title.setText(eventDetails.getName());
        description.setText(eventDetails.getDescription());
        time.setText(eventDetails.getTime());
        String pattern = "EEE, d MMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String dateString = simpleDateFormat.format(eventDetails.getStart_date());
        date.setText(dateString);
        attendees_count.setText(String.valueOf(eventDetails.getAttending().size()));
        veg_count.setText(String.valueOf(eventDetails.getVeg().size()));
        nonveg_count.setText(String.valueOf(eventDetails.getNon_veg().size()));
        initAdapter();
        initScrollListener();
    }
    private void initAdapter() {
        recyclerViewAdapter = new CompetitionStatsRecyclerViewAdapter(eventDetails.getProgrammes(), this,userObj);
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

    }
}
