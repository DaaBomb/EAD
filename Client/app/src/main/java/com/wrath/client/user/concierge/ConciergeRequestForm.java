package com.wrath.client.user.concierge;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.wrath.client.R;
import com.wrath.client.common.BaseNav;
import com.wrath.client.dto.Concierge;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ConciergeRequestForm extends BaseNav {

    Date dateSelected;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.concierge_form);
        setToolbar((Toolbar) findViewById(R.id.toolbar));
        setDrawerLayout((DrawerLayout) findViewById(R.id.drawer_layout));
        setNavigationView((NavigationView) findViewById(R.id.nav_view));
        initialize();

        final TextInputEditText title = (TextInputEditText) findViewById(R.id.textInputEditText2);
        final TextInputEditText details = (TextInputEditText) findViewById(R.id.textInputEditText);
        final CalendarView date = (CalendarView) findViewById(R.id.calendarView);
        final TimePicker time = (TimePicker) findViewById(R.id.timePicker);
        dateSelected = new Date(date.getDate());
        date.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String sDate1 = dayOfMonth + "/" + month + "/" + year;
                try {
                    dateSelected = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        Button submit = (Button) findViewById(R.id.button3);
        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Concierge concierge = new Concierge();
                concierge.setUser(userObj);
                concierge.setRequirement(title.getText().toString());
                concierge.setDetails(Objects.requireNonNull(details.getText()).toString());
                concierge.setDate_needed(dateSelected);
                concierge.setTime_needed(time.getHour() + ":" + time.getMinute());
                requestConcierge(concierge);
            }
        });
    }

    public void requestConcierge(Concierge conciergeRequest) {
        RequestBody request = RequestBody.create(MediaType.parse("application/json"), gson.toJson(conciergeRequest));
        compositeDisposable.add(iMyService.addConcierge(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        Toast.makeText(ConciergeRequestForm.this, "Concierge request Successful.", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(ConciergeRequestForm.this, ConciergeRequestsPage.class);
                        startActivity(i);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ConciergeRequestForm.this, "Concierge request failed. Try after sometime", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                    }
                })
        );
    }
}
