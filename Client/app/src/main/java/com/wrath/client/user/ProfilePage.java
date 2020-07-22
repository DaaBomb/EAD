package com.wrath.client.user;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.wrath.client.R;
import com.wrath.client.common.BaseNav;
import com.wrath.client.dto.SocietyNameDto;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class ProfilePage extends BaseNav {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        setToolbar((Toolbar) findViewById(R.id.toolbar));
        setDrawerLayout((DrawerLayout) findViewById(R.id.drawer_layout));
        setNavigationView((NavigationView) findViewById(R.id.nav_view));
        initialize();
        getSocietyName();

        TextView name = findViewById(R.id.textView);
        TextView flat = findViewById(R.id.textView2);
        TextView email = findViewById(R.id.email);

        name.setText(userObj.getName());
        if (userObj.getAddress().getBlockname() != null && userObj.getAddress().getFlatnum() != null)
            flat.setText(userObj.getAddress().getBlockname() + "-" + userObj.getAddress().getFlatnum());
        else
            flat.setText("");
        email.setText(userObj.getEmail());
    }

    private void getSocietyName() {
        compositeDisposable.add(iMyService.getSocietyName(userObj.getAddress().getSociety_id())
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
}
