package com.wrath.client.user.concierge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.wrath.client.R;
import com.wrath.client.common.BaseNav;
import com.wrath.client.dto.Concierge;
import com.wrath.client.dto.ConciergeDetailsResponse;
import com.wrath.client.util.RulesUtil;

import java.text.SimpleDateFormat;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class IndividualConciergePage extends BaseNav {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newconcierge);
        setToolbar((Toolbar) findViewById(R.id.toolbar));
        setDrawerLayout((DrawerLayout) findViewById(R.id.drawer_layout));
        setNavigationView((NavigationView) findViewById(R.id.nav_view));
        initialize();

        Bundle extras = getIntent().getExtras();
        final Concierge conciergeDetails = gson.fromJson(extras.getString("conciergeDetails"), Concierge.class);
        TextView title = findViewById(R.id.textView);
        TextView description = findViewById(R.id.textView4);
        TextView requestedBy = findViewById(R.id.textView2);
        TextView date = findViewById(R.id.textView3);

        title.setText(conciergeDetails.getRequirement());
        description.setText(conciergeDetails.getDetails());
        requestedBy.setText(conciergeDetails.getBlockname() + " - " + conciergeDetails.getFlatnum());
        String pattern = "EEE, d MMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String dateNeeded = simpleDateFormat.format(conciergeDetails.getDate_needed());
        date.setText(dateNeeded);

        ImageButton approved = findViewById(R.id.button2);
        ImageButton declined = findViewById(R.id.button);

        if(RulesUtil.isResident(userObj)) {
            if(!conciergeDetails.isResponded() || !conciergeDetails.isApproved() || conciergeDetails.isResident_responded()) {
                approved.setVisibility(View.INVISIBLE);
                declined.setVisibility(View.INVISIBLE);
            } else {
                approved.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateConciergeDoneStatus(conciergeDetails.get_id(), true);
                    }
                });

                declined.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateConciergeDoneStatus(conciergeDetails.get_id(), false);
                    }
                });
            }
        } else {
            if(conciergeDetails.isResponded()) {
                approved.setVisibility(View.INVISIBLE);
                declined.setVisibility(View.INVISIBLE);
            } else {
                approved.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateConciergeApprovalStatus(conciergeDetails.get_id(), true);
                    }
                });

                declined.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateConciergeApprovalStatus(conciergeDetails.get_id(), false);
                    }
                });
            }
        }
    }

    private void updateConciergeApprovalStatus(String id, boolean isApproved) {
        Concierge conciergeRequest = new Concierge();
        conciergeRequest.set_id(id);
        conciergeRequest.setApproved(isApproved);
        RequestBody request = RequestBody.create(MediaType.parse("application/json"), gson.toJson(conciergeRequest));
        compositeDisposable.add(iMyService.updateConciergeResponded(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        Toast.makeText(IndividualConciergePage.this, "Concierge status updated.", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(IndividualConciergePage.this, ConciergeRequestsPage.class);
                        startActivity(i);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(IndividualConciergePage.this, "Concierge update failed. Try after sometime", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                    }
                })
        );
    }

    private void updateConciergeDoneStatus(String id, boolean isDone) {
        Concierge conciergeRequest = new Concierge();
        conciergeRequest.set_id(id);
        conciergeRequest.setDone(isDone);
        RequestBody request = RequestBody.create(MediaType.parse("application/json"), gson.toJson(conciergeRequest));
        compositeDisposable.add(iMyService.updateConciergeDone(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        Toast.makeText(IndividualConciergePage.this, "Concierge status updated.", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(IndividualConciergePage.this, ConciergeRequestsPage.class);
                        startActivity(i);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(IndividualConciergePage.this, "Concierge update failed. Try after sometime", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                    }
                })
        );
    }
}
