package com.wrath.client.user.amenities;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.wrath.client.R;
import com.wrath.client.common.BaseNav;
import com.wrath.client.dto.AddAmenetiesRequest;
import com.wrath.client.dto.AmenetiesResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class AddAmenitiesPage extends BaseNav implements AmenityRecyclerViewAdapter.OnAmenityClickListener {

    List<String> amenitiesList = new ArrayList<>();
    RecyclerView recyclerView;
    AmenityRecyclerViewAdapter recyclerViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_amenities);
        setToolbar((Toolbar) findViewById(R.id.toolbar));
        setDrawerLayout((DrawerLayout) findViewById(R.id.drawer_layout));
        setNavigationView((NavigationView) findViewById(R.id.nav_view));
        initialize();
        getAmenitiesList();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView2);

        initAdapter();
        initScrollListener();

        final TextInputEditText amenityName = findViewById(R.id.amenityname);
        ImageButton addAmenity = findViewById(R.id.button2);
        addAmenity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amenitiesList.add(amenityName.getText().toString());
                recyclerViewAdapter.notifyDataSetChanged();
            }
        });

        Button saveAmenitiesBtn = findViewById(R.id.save_amenity_btn);
        saveAmenitiesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAmenities();
            }
        });
    }

    private void initAdapter() {
        recyclerViewAdapter = new AmenityRecyclerViewAdapter(amenitiesList, this, false);
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

    public void getAmenitiesList() {
        compositeDisposable.add(iMyService.getAmeneties(userObj.getAddress().getSociety_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        AmenetiesResponse res = gson.fromJson(s, AmenetiesResponse.class);
                        amenitiesList.addAll(res.getSociety().getAmeneties());
                        recyclerViewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(AddAmenitiesPage.this, "Unable to fetch amenities. Check network connectivity.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }

    public void saveAmenities() {
        AddAmenetiesRequest addAmenetiesRequest = new AddAmenetiesRequest();
        addAmenetiesRequest.setAmeneties(amenitiesList);
        addAmenetiesRequest.setSociety_id(userObj.getAddress().getSociety_id());
        RequestBody request = RequestBody.create(MediaType.parse("application/json"), gson.toJson(addAmenetiesRequest));
        compositeDisposable.add(iMyService.addAmeneties(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        Toast.makeText(AddAmenitiesPage.this, "Amenities submitted Successfully.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(AddAmenitiesPage.this, "Amenities request failed. Try after sometime", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                    }
                })
        );
    }

    @Override
    public void onAmenityClick(int position) {
        amenitiesList.remove(position);
        recyclerViewAdapter.notifyDataSetChanged();
    }
}
