package com.wrath.client.user.announcement;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.wrath.client.R;
import com.wrath.client.common.BaseNav;
import com.wrath.client.dto.Announcement;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class AnnouncementForm extends BaseNav {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcement_form);
        setToolbar((Toolbar) findViewById(R.id.toolbar));
        setDrawerLayout((DrawerLayout) findViewById(R.id.drawer_layout));
        setNavigationView((NavigationView) findViewById(R.id.nav_view));
        initialize();

        final TextInputEditText announcement = (TextInputEditText) findViewById(R.id.textInputEditText2);
        Button submit = (Button) findViewById(R.id.button3);
        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Announcement announcementRequest = new Announcement();
                announcementRequest.setAnnouncement(announcement.getText().toString());
                announcementRequest.setSociety_id(userObj.getAddress().getSociety_id());
                makeAnnouncement(announcementRequest);
            }
        });
    }

    public void makeAnnouncement(Announcement announcement) {
        RequestBody request = RequestBody.create(MediaType.parse("application/json"), gson.toJson(announcement));
        compositeDisposable.add(iMyService.addAnnouncement(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        Toast.makeText(AnnouncementForm.this, "Announcement submitted Successfully.", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(AnnouncementForm.this, AnnouncementsPage.class);
                        startActivity(i);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(AnnouncementForm.this, "Announcement request failed. Try after sometime", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                    }
                })
        );
    }
}
