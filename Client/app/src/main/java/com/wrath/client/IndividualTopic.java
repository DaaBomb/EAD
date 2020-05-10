package com.wrath.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.wrath.client.Retrofit.IMyService;
import com.wrath.client.Retrofit.RetrofitClient;
import com.wrath.client.dto.BaseResponse;
import com.wrath.client.dto.Comment;
import com.wrath.client.dto.CommentUser;
import com.wrath.client.dto.Topic;
import com.wrath.client.dto.User;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

public class IndividualTopic extends AppCompatActivity {
    private DrawerLayout drawer;
    TextView txt_description;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    TextView txt_topic;
    TextView txt_creator_name;
    TextView txt_date;
    Gson gson = new Gson();
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;
    User user;
    Topic topic;
    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_topic);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Details");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_open,R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
        txt_topic = (TextView) findViewById(R.id.textView);
        txt_creator_name = (TextView) findViewById(R.id.textView2);
        txt_date = (TextView) findViewById(R.id.textView3);
        txt_description = (TextView) findViewById(R.id.textView4);
        Bundle extras = getIntent().getExtras();
        final Topic topic = gson.fromJson(extras.getString("topicDetails"), Topic.class);
        user = gson.fromJson(extras.getString("user"), User.class);
        txt_topic.setText(topic.getTopic());
        txt_creator_name.setText(topic.getCreator_name());
        txt_description.setText(topic.getDescription());
        String pattern = "EEE, d MMM yyyy, HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(topic.getDate_created());
        txt_date.setText(date);

        FloatingActionButton fab;
        fab = findViewById(R.id.floatingActionButton);

        ArrayList<Model> list = new ArrayList<>();

//        list.add(new Model(Model.IMAGE_TYPE, "Hi. Commenttsss.", "John Doe", R.drawable.ic_account_circle_black_24dp));
//        list.add(new Model(Model.IMAGE_TYPE, "Hi. Commenttsss.", "John Doe", R.drawable.ic_account_circle_black_24dp));
//        list.add(new Model(Model.IMAGE_TYPE, "Hi. Commenttsss.", "John Doe", R.drawable.ic_account_circle_black_24dp));
//        list.add(new Model(Model.IMAGE_TYPE, "Hi. Commenttsss.", "John Doe", R.drawable.ic_account_circle_black_24dp));
//        list.add(new Model(Model.IMAGE_TYPE, "Hi. Commenttsss.", "John Doe", R.drawable.ic_account_circle_black_24dp));

        List<Comment>comments = topic.getComments();
        for(Comment comment: comments){
            String date1 = simpleDateFormat.format(comment.getDate_created());
            list.add(new Model(Model.IMAGE_TYPE, comment.getComment(),date1, comment.getPerson_name(), R.drawable.ic_account_circle_black_24dp));
        }

        MultiViewTypeAdapter adapter = new MultiViewTypeAdapter(list, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View add_comment_layout = LayoutInflater.from(IndividualTopic.this)
                        .inflate(R.layout.comment_topic, null);
                new MaterialStyledDialog.Builder(IndividualTopic.this)
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
                                    Toast.makeText(IndividualTopic.this, "Topic cannot be empty", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                addComment(add_comment.getText().toString(),topic.get_id());
                            }
                        }).show();
            }
        });


    }

    public void addComment(String comment, String topic_id){
        CommentUser commentUser = new CommentUser();
        commentUser.setComment(comment);
        commentUser.setTopic_id(topic_id);
        commentUser.setUser(user);
        RequestBody request = RequestBody.create(MediaType.parse("application/json"), gson.toJson(commentUser));
        compositeDisposable.add(iMyService.addcomment(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        BaseResponse res = gson.fromJson(response, BaseResponse.class);
                        Intent i = new Intent(IndividualTopic.this,ForumPage.class);
                        Bundle extras = new Bundle();
                        extras.putString("user",gson.toJson(user));
                        i.putExtras(extras);
                        Toast.makeText(IndividualTopic.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        startActivity(i);
                    }
                })
        );

    }
}
