package com.wrath.client.user.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wrath.client.R;
import com.wrath.client.common.BaseNav;
import com.wrath.client.dto.AmenetiesResponse;
import com.wrath.client.dto.SocietyNameDto;
import com.wrath.client.dto.UpdateProfileRequest;
import com.wrath.client.dto.User;
import com.wrath.client.dto.UsersResponse;
import com.wrath.client.user.amenities.AmenityRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class EditProfilePage extends BaseNav implements ProfileRecyclerViewAdapter.OnProfileListener, AmenityRecyclerViewAdapter.OnAmenityClickListener {

    User userProfile;
    RecyclerView residentsRecyclerView, interestsRecyclerView;
    ProfileRecyclerViewAdapter residentsRecyclerViewAdapter;
    AmenityRecyclerViewAdapter amenitiesRecyclerViewAdapter;
    List<User> usersList = new ArrayList<>();
    List<String> interestsList = new ArrayList<>();
    List<String> amenitiesList = new ArrayList<>();
    Set<String> interestSet = new HashSet<>();
    MaterialEditText name, email;
    TextView flat;
    Spinner sport;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        setToolbar((Toolbar) findViewById(R.id.toolbar));
        setDrawerLayout((DrawerLayout) findViewById(R.id.drawer_layout));
        setNavigationView((NavigationView) findViewById(R.id.nav_view));
        initialize();

        Bundle bundle = getIntent().getExtras();
        userProfile = gson.fromJson(bundle.getString("editUser"), User.class);
        interestsList = userProfile.getInterests();
        interestSet.addAll(interestsList);

        name = findViewById(R.id.textView);
        flat = findViewById(R.id.textView2);
        email = findViewById(R.id.email);
        residentsRecyclerView = findViewById(R.id.recyclerView);
        interestsRecyclerView = findViewById(R.id.recyclerView2);

        getSocietyName();
        getResidents();

        name.setText(userProfile.getName());
        if (userProfile.getAddress().getBlockname() != null && userProfile.getAddress().getFlatnum() != null)
            flat.setText(userProfile.getAddress().getBlockname() + "-" + userProfile.getAddress().getFlatnum());
        else
            flat.setText("");
        email.setText(userProfile.getEmail());

        initAdapter();
        initScrollListener();

        Button save_btn = findViewById(R.id.button13);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser(name.getText().toString(), email.getText().toString(), interestsList);
                Intent i = new Intent(EditProfilePage.this, ProfilePage.class);
                Bundle extras = new Bundle();
                extras.putString("userProfile", gson.toJson(userProfile));
                i.putExtras(extras);
                startActivity(i);
            }
        });

        sport = findViewById(R.id.sportSpinner);
        getAmenitiesList();
    }

    private void saveUser(String name, String email, List<String> interestsList) {
        UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest();
        updateProfileRequest.set_id(userProfile.get_id());
        updateProfileRequest.setEmail(email);
        updateProfileRequest.setName(name);
        updateProfileRequest.setInterests(interestsList);
        RequestBody request = RequestBody.create(MediaType.parse("application/json"), gson.toJson(updateProfileRequest));
        compositeDisposable.add(iMyService.updateProfile(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        UsersResponse res = gson.fromJson(s, UsersResponse.class);
                        sharedPreferences.edit().clear().apply();
                        sharedPreferences.edit().putString("user",gson.toJson(res.getUser())).apply();
                        Toast.makeText(getBaseContext(), "Profile update successful", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(EditProfilePage.this, ProfilePage.class);
                        Bundle extras = new Bundle();
                        extras.putString("userProfile", gson.toJson(userProfile));
                        i.putExtras(extras);
                        startActivity(i);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getBaseContext(), "Please check network connectivity", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                    }
                })
        );
    }

    private void getSocietyName() {
        compositeDisposable.add(iMyService.getSocietyName(userProfile.getAddress().getSociety_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        SocietyNameDto societyNameDto = gson.fromJson(s, SocietyNameDto.class);
                        TextView society = findViewById(R.id.textView3);
                        society.setText(societyNameDto.getSociety().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getBaseContext(), "Please check network connectivity", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                    }
                })
        );
    }

    private void getResidents() {
        RequestBody request = RequestBody.create(MediaType.parse("application/json"), gson.toJson(userProfile.getAddress()));
        compositeDisposable.add(iMyService.getResidentsInSameFlat(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        UsersResponse res = gson.fromJson(s, UsersResponse.class);
                        usersList.addAll(res.getUsers());
                        residentsRecyclerViewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getBaseContext(), "Please check network connectivity", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                    }
                })
        );
    }

    private void initAdapter() {
        residentsRecyclerViewAdapter = new ProfileRecyclerViewAdapter(usersList, this);
        residentsRecyclerView.setAdapter(residentsRecyclerViewAdapter);
        residentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        amenitiesRecyclerViewAdapter = new AmenityRecyclerViewAdapter(interestsList, this, false);
        interestsRecyclerView.setAdapter(amenitiesRecyclerViewAdapter);
        interestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void initScrollListener() {
        residentsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        residentsRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
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

        interestsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        interestsRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
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
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(EditProfilePage.this, android.R.layout.simple_spinner_item, amenitiesList);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sport.setAdapter(arrayAdapter);
                        sport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                interestSet.add(amenitiesList.get(position));
                                interestsList.clear();
                                interestsList.addAll(interestSet);
                                amenitiesRecyclerViewAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(EditProfilePage.this, "Unable to fetch amenities. Check network connectivity.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }

    @Override
    public void onProfileClick(int position) {
        Intent i = new Intent(this, EditProfilePage.class);
        Bundle extras = new Bundle();
        extras.putString("userProfile", gson.toJson(usersList.get(position)));
        i.putExtras(extras);
        startActivity(i);
    }

    @Override
    public void onAmenityClick(int position) {
        interestsList.remove(position);
        amenitiesRecyclerViewAdapter.notifyDataSetChanged();
    }
}
