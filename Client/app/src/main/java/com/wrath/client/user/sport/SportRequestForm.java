package com.wrath.client.user.sport;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.wrath.client.R;
import com.wrath.client.common.BaseNav;
import com.wrath.client.dto.AddSportRequest;
import com.wrath.client.dto.AmenetiesResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SportRequestForm extends BaseNav {

    Date dateSelected;
    List<String> amenitiesList = new ArrayList<>();
    Spinner sport;
    TextView sportName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sports_form);
        setToolbar((Toolbar) findViewById(R.id.toolbar));
        setDrawerLayout((DrawerLayout) findViewById(R.id.drawer_layout));
        setNavigationView((NavigationView) findViewById(R.id.nav_view));
        initialize();

        sportName = findViewById(R.id.textView15);

        final TextInputEditText otherSports = findViewById(R.id.textInputEditText4);
        final TextInputEditText details = findViewById(R.id.textInputEditText5);
        final TextInputEditText noOfPlayersRequired = findViewById(R.id.textInputEditText);
        final TimePicker time = (TimePicker) findViewById(R.id.timePicker);
//        dateSelected = new Date(date.getDate());
//        date.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//                String sDate1 = dayOfMonth + "/" + month + "/" + year;
//                try {
//                    dateSelected = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        Button submit = (Button) findViewById(R.id.button3);
        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                AddSportRequest addSportRequest = new AddSportRequest();
                addSportRequest.setUser(userObj);
                addSportRequest.setDescription(details.getText().toString());
                addSportRequest.setNumberOfPlayers(Integer.parseInt(noOfPlayersRequired.getText().toString()));
                addSportRequest.setTime(time.getHour() + ":" + time.getMinute());
                String name = sportName.getText().toString().equalsIgnoreCase("Others") ? otherSports.getText().toString() : sportName.getText().toString();
                addSportRequest.setSport(name);
                addSport(addSportRequest);
            }
        });
        getAmenitiesList();
    }

    public void addSport(AddSportRequest sportRequest) {
        RequestBody request = RequestBody.create(MediaType.parse("application/json"), gson.toJson(sportRequest));
        compositeDisposable.add(iMyService.addSport(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        Toast.makeText(SportRequestForm.this, "Sport request Successful.", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(SportRequestForm.this, SportsListPage.class);
                        startActivity(i);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(SportRequestForm.this, "Sport request failed. Try after sometime", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                    }
                })
        );
    }

    public void getAmenitiesList() {
        compositeDisposable.add(iMyService.getAmeneties(userObj.getAddress().getSociety_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        AmenetiesResponse res = gson.fromJson(s, AmenetiesResponse.class);
                        amenitiesList.addAll(res.getSociety().getAmeneties());
                        sport = findViewById(R.id.sportSpinner);
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SportRequestForm.this, android.R.layout.simple_spinner_item, amenitiesList);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sport.setAdapter(arrayAdapter);
                        sport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String sportSelected = amenitiesList.get(position);
                                sportName.setText(sportSelected);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(SportRequestForm.this, "Unable to fetch amenities. Check network connectivity.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }
}
