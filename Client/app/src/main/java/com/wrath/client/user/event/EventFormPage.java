package com.wrath.client.user.event;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TimePicker;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.wrath.client.R;
import com.wrath.client.common.BaseNav;

import java.util.Date;

public class EventFormPage extends BaseNav {

    String dateSelected;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);
        setToolbar((Toolbar) findViewById(R.id.toolbar));
        setDrawerLayout((DrawerLayout) findViewById(R.id.drawer_layout));
        setNavigationView((NavigationView) findViewById(R.id.nav_view));
        final TextInputEditText eventName = (TextInputEditText) findViewById(R.id.textInputEditText2);
        final TextInputEditText eventDesc = (TextInputEditText) findViewById(R.id.textInputEditText);
        final CalendarView eventDate = (CalendarView) findViewById(R.id.calendarView);
        dateSelected = new Date(eventDate.getDate()).toString();
        eventDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                dateSelected = dayOfMonth + "/" + month + "/" + year;
            }
        });
        final TimePicker eventTime = (TimePicker) findViewById(R.id.timePicker);
        Button next = (Button) findViewById(R.id.button3);
        next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventFormPage.this, EventFormPage2.class);
                Bundle bundle = new Bundle();
                bundle.putString("eventName", eventName.getText().toString());
                bundle.putString("eventDesc", eventDesc.getText().toString());
                bundle.putString("eventDate", dateSelected);
                bundle.putString("eventTime", eventTime.getHour()+":"+eventTime.getMinute());
                i.putExtras(bundle);
                startActivity(i);
            }
        });
        initialize();
    }
}
