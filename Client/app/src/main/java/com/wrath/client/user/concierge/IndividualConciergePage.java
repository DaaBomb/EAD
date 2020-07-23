package com.wrath.client.user.concierge;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wrath.client.R;
import com.wrath.client.common.BaseNav;
import com.wrath.client.dto.Announcement;
import com.wrath.client.dto.BaseResponse;
import com.wrath.client.dto.Comment;
import com.wrath.client.dto.CommentUser;
import com.wrath.client.dto.Concierge;
import com.wrath.client.dto.ConciergeDetailsResponse;
import com.wrath.client.dto.Model;
import com.wrath.client.user.announcement.AnnouncementRecyclerViewAdapter;
import com.wrath.client.user.forum.ForumPage;
import com.wrath.client.user.forum.IndividualTopic;
import com.wrath.client.user.forum.MultiViewTypeAdapter;
import com.wrath.client.util.RulesUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
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
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        title.setText(conciergeDetails.getRequirement());
        description.setText(conciergeDetails.getDetails());
        requestedBy.setText(conciergeDetails.getBlockname() + " - " + conciergeDetails.getFlatnum());
        String pattern = "EEE, d MMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String dateNeeded = simpleDateFormat.format(conciergeDetails.getDate_needed());
        date.setText(dateNeeded);
        ImageButton approved = findViewById(R.id.button2);
        ImageButton declined = findViewById(R.id.button);

        ArrayList<Model> list = new ArrayList<>();
        List<Comment> comments = conciergeDetails.getComments();
        for (Comment comment : comments) {
            String date1 = simpleDateFormat.format(comment.getDate_created());
            list.add(new Model(Model.IMAGE_TYPE, comment.getComment(), date1, comment.getPerson_name(), R.drawable.ic_account_circle_black_24dp));
        }
        MultiViewTypeAdapter adapter = new MultiViewTypeAdapter(list, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View add_comment_layout = LayoutInflater.from(IndividualConciergePage.this)
                        .inflate(R.layout.comment_topic, null);
                new MaterialStyledDialog.Builder(IndividualConciergePage.this)
                        .setIcon(R.drawable.ic_send_white_36dp)
                        .setTitle("add new comment")
                        .setDescription("comment shouldn't exceed 20 words")
                        .setCustomView(add_comment_layout)
                        .setNegativeText("Cancel")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveText("Add new comment")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                MaterialEditText add_comment = (MaterialEditText) add_comment_layout.findViewById(R.id.add_comment);

                                if (TextUtils.isEmpty(add_comment.getText().toString())) {
                                    Toast.makeText(IndividualConciergePage.this, "Topic cannot be empty", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                addComment(add_comment.getText().toString(), conciergeDetails.get_id());
                            }
                        }).show();
            }
        });
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
    public void addComment(String comment, String topic_id) {
        CommentUser commentUser = new CommentUser();
        commentUser.setComment(comment);
        commentUser.setTopic_id(topic_id);
        commentUser.setUser(userObj);
        RequestBody request = RequestBody.create(MediaType.parse("application/json"), gson.toJson(commentUser));
        compositeDisposable.add(iMyService.addCommentToConcierge(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        BaseResponse res = gson.fromJson(response, BaseResponse.class);
                        Intent i = new Intent(IndividualConciergePage.this, ConciergeRequestsPage.class);
                        Bundle extras = new Bundle();
                        extras.putString("user", gson.toJson(user));
                        i.putExtras(extras);
                        Toast.makeText(IndividualConciergePage.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        startActivity(i);
                    }
                })
        );

    }
}
