package com.example.client;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.client.Retrofit.IMyService;
import com.example.client.Retrofit.RetrofitClient;
import com.example.client.dto.BaseResponse;
import com.example.client.dto.TopicDescription;
import com.example.client.dto.User;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

public class ForumPage extends AppCompatActivity {

    private Toolbar forum_toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private DiscussionFragment discussionFragment;
    private HelpFragment helpFragment;

    Gson gson = new Gson();
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    User user;
    FloatingActionButton btn_add_topic;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_page);

        Bundle extras = getIntent().getExtras();
        user = gson.fromJson(extras.getString("user"), User.class);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        forum_toolbar = (Toolbar) findViewById(R.id.forum_toolbar);
        setSupportActionBar(forum_toolbar);

        btn_add_topic = findViewById(R.id.fab);
        btn_add_topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View add_forum_layout = LayoutInflater.from(ForumPage.this).inflate(R.layout.add_new_forum, null);
                new MaterialStyledDialog.Builder(ForumPage.this)
                        .setIcon(R.drawable.ic_edit_black_24dp)
                        .setTitle("add new topic")
                        .setDescription("Topic shouldn't exceed 15 words")
                        .setCustomView(add_forum_layout)
                        .setNegativeText("Cancel")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveText("Add new topic")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                MaterialEditText add_topic = (MaterialEditText) add_forum_layout.findViewById(R.id.add_topic);
                                MaterialEditText add_description = (MaterialEditText) add_forum_layout.findViewById(R.id.add_description);
                                Switch toggle = (Switch) add_forum_layout.findViewById(R.id.switch1);
                                Boolean is_discussion = !toggle.isChecked();
                                if (TextUtils.isEmpty(add_topic.getText().toString())) {
                                    Toast.makeText(ForumPage.this, "Topic cannot be empty", Toast.LENGTH_LONG).show();
                                    return;
                                }

                                if (TextUtils.isEmpty(add_description.getText().toString())) {
                                    Toast.makeText(ForumPage.this, "Description cannot be empty", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                addTopic(add_topic.getText().toString(), add_description.getText().toString(), is_discussion);
                            }
                        }).show();
            }
        });
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        discussionFragment = new DiscussionFragment();
        helpFragment = new HelpFragment();

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(discussionFragment, "Discussion");
        viewPagerAdapter.addFragment(helpFragment, "Complain");

        viewPager.setAdapter(viewPagerAdapter);


    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitles = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitles.add(title);

        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }
    }

    public void addTopic(String topic, String description, Boolean is_discussion) {
        TopicDescription topicDescription = new TopicDescription();
        topicDescription.setTopic(topic);
        topicDescription.setDescription(description);
        topicDescription.setIs_discussion(is_discussion);
        topicDescription.setUser(user);

        RequestBody request = RequestBody.create(MediaType.parse("application/json"), gson.toJson(topicDescription));
        compositeDisposable.add(iMyService.addTopic(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        BaseResponse res = gson.fromJson(response, BaseResponse.class);
                        Toast.makeText(ForumPage.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        startActivity(getIntent());
                    }
                })
        );
    }
}
