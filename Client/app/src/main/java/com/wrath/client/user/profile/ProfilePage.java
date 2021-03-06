package com.wrath.client.user.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.wrath.client.R;
import com.wrath.client.common.BaseNav;
import com.wrath.client.dto.SocietyNameDto;
import com.wrath.client.dto.User;
import com.wrath.client.dto.UsersResponse;
import com.wrath.client.user.amenities.AmenityRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ProfilePage extends BaseNav implements ProfileRecyclerViewAdapter.OnProfileListener, AmenityRecyclerViewAdapter.OnAmenityClickListener {

    User userProfile;
    RecyclerView residentsRecyclerView, interestsRecyclerView;
    ProfileRecyclerViewAdapter residentsRecyclerViewAdapter;
    AmenityRecyclerViewAdapter amenitiesRecyclerViewAdapter;
    List<User> usersList = new ArrayList<>();
    List<String> amenitiesList = new ArrayList<>();
    TextView name, flat, email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        setToolbar((Toolbar) findViewById(R.id.toolbar));
        setDrawerLayout((DrawerLayout) findViewById(R.id.drawer_layout));
        setNavigationView((NavigationView) findViewById(R.id.nav_view));
        initialize();

        Bundle bundle = getIntent().getExtras();
        User userProfileInital = gson.fromJson(bundle.getString("userProfile"), User.class);
        getUserLatestInfo(userProfileInital.get_id());

        name = findViewById(R.id.textView);
        flat = findViewById(R.id.textView2);
        email = findViewById(R.id.email);
        residentsRecyclerView = findViewById(R.id.recyclerView);
        interestsRecyclerView = findViewById(R.id.recyclerView2);

        name.setText(userProfileInital.getName());
        if (userProfileInital.getAddress().getBlockname() != null && userProfileInital.getAddress().getFlatnum() != null)
            flat.setText(userProfileInital.getAddress().getBlockname() + "-" + userProfileInital.getAddress().getFlatnum());
        else
            flat.setText("");
        email.setText(userProfileInital.getEmail());

        Button edit_btn = findViewById(R.id.button13);
        if(!userProfileInital.get_id().equals(userObj.get_id())) {
            edit_btn.setVisibility(View.INVISIBLE);
        }
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfilePage.this, EditProfilePage.class);
                Bundle extras = new Bundle();
                extras.putString("editUser", gson.toJson(userProfile));
                i.putExtras(extras);
                startActivity(i);
            }
        });

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

    private void getUserLatestInfo(String userId) {
        Map<String, String> body = new HashMap<String, String>();
        body.put("_id", userId);
        RequestBody request = RequestBody.create(MediaType.parse("application/json"), gson.toJson(body));
        compositeDisposable.add(iMyService.getUserById(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        UsersResponse userResponse = gson.fromJson(s, UsersResponse.class);
                        userProfile = userResponse.getUser();
                        amenitiesList = userProfile.getInterests();
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

        amenitiesRecyclerViewAdapter = new AmenityRecyclerViewAdapter(amenitiesList, this, true);
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

    @Override
    public void onProfileClick(int position) {
        Intent i = new Intent(this, ProfilePage.class);
        Bundle extras = new Bundle();
        extras.putString("userProfile", gson.toJson(usersList.get(position)));
        i.putExtras(extras);
        startActivity(i);
    }

    @Override
    public void onAmenityClick(int position) {
        amenitiesList.remove(position);
        amenitiesRecyclerViewAdapter.notifyDataSetChanged();
    }
}
