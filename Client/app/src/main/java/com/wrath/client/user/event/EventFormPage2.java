package com.wrath.client.user.event;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.wrath.client.R;
import com.wrath.client.common.BaseNav;
import com.wrath.client.dto.BaseResponse;
import com.wrath.client.dto.EventDetails;
import com.wrath.client.dto.Programme;

import java.util.ArrayList;
import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class EventFormPage2 extends BaseNav implements ProgrammeRecyclerViewAdapter.OnProgrammeListener {

    RecyclerView recyclerView;
    ProgrammeRecyclerViewAdapter recyclerViewAdapter;
    ArrayList<Programme> programmeList = new ArrayList<>();
    Switch food_choice;

    public EventFormPage2() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event_step2);
        setToolbar((Toolbar) findViewById(R.id.toolbar));
        setDrawerLayout((DrawerLayout) findViewById(R.id.drawer_layout));
        setNavigationView((NavigationView) findViewById(R.id.nav_view));
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView2);
        food_choice = (Switch) findViewById(R.id.switch2);
        ImageButton addProgramme = (ImageButton) findViewById(R.id.button2);
        Button next = (Button) findViewById(R.id.button3);
        final TextInputEditText competitionName = (TextInputEditText) findViewById(R.id.addCompetitionName);
        final TextInputEditText competitionDesc = (TextInputEditText) findViewById(R.id.addCompetitionDesc);
        initialize();
        initAdapter();
        initScrollListener();
        addProgramme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (competitionName.getText() != null && competitionDesc.getText() != null) {
                    programmeList.add(new Programme(competitionName.getText().toString(), competitionDesc.getText().toString()));
                    competitionName.setText("");
                    competitionDesc.setText("");
                    recyclerViewAdapter.notifyDataSetChanged();
                }
            }
        });
        final Bundle bundle = getIntent().getExtras();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvent(bundle.getString("eventName"), bundle.getString("eventDesc"),
                        bundle.getString("eventDate"), bundle.getString("eventTime"));

            }
        });
    }

    private void initAdapter() {
        recyclerViewAdapter = new ProgrammeRecyclerViewAdapter(programmeList, this);
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
    public void onProgrammeClick(final int position) {
        final int pos = position;
        ImageButton remove = (ImageButton) recyclerView.getChildAt(position).findViewById(R.id.button6);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                programmeList.remove(pos);
                recyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }

    public void addEvent(String eventName, String description, String eventDate, String eventTime) {
        EventDetails eventDetails = new EventDetails();
        eventDetails.setName(eventName);
        eventDetails.setDescription(description);
        eventDetails.setStart_date(new Date(eventDate));
        eventDetails.setProgrammes(programmeList);
        eventDetails.setTime(eventTime);
        eventDetails.setUser(userObj);
        eventDetails.setFood_choice(food_choice.isChecked());

        RequestBody request = RequestBody.create(MediaType.parse("application/json"), gson.toJson(eventDetails));
        compositeDisposable.add(iMyService.addEvent(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        BaseResponse res = gson.fromJson(response, BaseResponse.class);
                        Intent i = new Intent(EventFormPage2.this, EventsListPage.class);
                        startActivity(i);
                    }
                })
        );
    }
}
