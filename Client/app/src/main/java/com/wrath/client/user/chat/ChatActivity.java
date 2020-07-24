package com.wrath.client.user.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.wrath.client.R;
import com.wrath.client.common.BaseNav;
import com.wrath.client.dto.ChatMessage;
import com.wrath.client.dto.ChatMessageResponse;
import com.wrath.client.dto.NotificationDetails;
import com.wrath.client.dto.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;


public class ChatActivity extends BaseNav {

    RecyclerView recyclerView;
    ChatRecyclerViewAdapter recyclerViewAdapter;
    List<ChatMessage> chatMessages = new ArrayList<>();
    User chatTo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        setToolbar((Toolbar) findViewById(R.id.toolbar));
        setDrawerLayout((DrawerLayout) findViewById(R.id.drawer_layout));
        setNavigationView((NavigationView) findViewById(R.id.nav_view));
        initialize();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        Bundle extras = getIntent().getExtras();
        chatTo = gson.fromJson(extras.getString("chatTo"), User.class);
        TextView name = findViewById(R.id.textView22);
        name.setText(chatTo.getName());
        populateData();
        initAdapter();
        initScrollListener();

        final TextInputEditText message = findViewById(R.id.message);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = message.getText().toString();
                sendMessage(msg);
                message.setText("");
            }
        });
    }

    private void sendMessage(String msg) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setFrom(userObj);
        chatMessage.setTo(chatTo);
        chatMessage.setMessage(msg);
        RequestBody request = RequestBody.create(MediaType.parse("application/json"), gson.toJson(chatMessage));
        compositeDisposable.add(iMyService.sendMessage(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        ChatMessageResponse response = gson.fromJson(s, ChatMessageResponse.class);
                        chatMessages.add(response.getChat());
                        recyclerViewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ChatActivity.this, "Message send failed. Try after sometime", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                    }
                })
        );
    }

    private void populateData() {
        compositeDisposable.add(iMyService.getMessages(userObj.get_id(), chatTo.get_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        ChatMessageResponse res = gson.fromJson(s, ChatMessageResponse.class);
                        chatMessages.addAll(res.getChats());
                        recyclerViewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ChatActivity.this, "Unable to fetch announcements. Check network connectivity.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }

    private void initAdapter() {
        recyclerViewAdapter = new ChatRecyclerViewAdapter(chatMessages);
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

    @Override
    public void displayAlertDialog(Intent intent) {
        super.displayAlertDialog(intent);
        Bundle extras = intent.getExtras();
        if (extras != null && extras.getString("title") != null && extras.getString("message") != null) {
            String title = extras.getString("title");
            String message = extras.getString("message");
            final NotificationDetails notificationDetails = gson.fromJson(extras.getString("notificationDetails"), NotificationDetails.class);
            if ("New Message".equalsIgnoreCase(title)) {
                chatMessages.add(notificationDetails.getChat());
                recyclerViewAdapter.notifyDataSetChanged();
            }

        }
    }
}

