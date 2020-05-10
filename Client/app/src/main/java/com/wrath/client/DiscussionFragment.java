package com.wrath.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wrath.client.Retrofit.IMyService;
import com.wrath.client.dto.Topic;
import com.wrath.client.dto.TopicsResponse;
import com.wrath.client.dto.User;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscussionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscussionFragment extends Fragment implements RecyclerViewAdapter.OnTopicListener {

    RecyclerView recyclerView;
    com.wrath.client.RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<Topic> topicsList = new ArrayList<>();

    CompositeDisposable compositeDisposable;
    IMyService iMyService;

    User user;
    Gson gson;

    boolean isLoading = false;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int flag = 1;

    public DiscussionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiscussionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscussionFragment newInstance(String param1, String param2) {
        DiscussionFragment fragment = new DiscussionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        compositeDisposable = ((ForumPage) getActivity()).compositeDisposable;
        iMyService = ((ForumPage) getActivity()).iMyService;
        user = ((ForumPage) getActivity()).user;
        gson = ((ForumPage) getActivity()).gson;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_discussion, container, false);
        recyclerView = root.findViewById(R.id.recyclerViewDiscuss);

        populateData(user.getAddress().getSociety_id(), 1);
        initAdapter();
        initScrollListener();
        return root;
    }

    private void populateData(String societyId, int flag) {
        compositeDisposable.add(iMyService.getTopics(societyId, flag)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        TopicsResponse res = gson.fromJson(response, TopicsResponse.class);
                        if (res.getMsg().equalsIgnoreCase("successful"))
                            topicsList.addAll(res.getTopics());
                        recyclerViewAdapter.notifyDataSetChanged();
                    }
                })
        );
    }

    private void initAdapter() {
        recyclerViewAdapter = new RecyclerViewAdapter(topicsList, this);
        recyclerView.setAdapter(recyclerViewAdapter);
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

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == topicsList.size() - 1) {
                        //bottom of list!
                        flag++;
                        loadMore(user.getAddress().getSociety_id(), flag);
                        isLoading = true;
                    }
                }
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

    private void loadMore(String societyId, int flag) {

        topicsList.add(null);
        recyclerViewAdapter.notifyItemInserted(topicsList.size() - 1);

        compositeDisposable.add(iMyService.getTopics(societyId, flag)
                .subscribeOn(Schedulers.io())
                .delay(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        TopicsResponse res = gson.fromJson(response, TopicsResponse.class);
                        topicsList.remove(topicsList.size() - 1);
                        int scrollPosition = topicsList.size();
                        recyclerViewAdapter.notifyItemRemoved(scrollPosition);
                        if (res.getMsg().equalsIgnoreCase("successful")) {
                            topicsList.addAll(res.getTopics());
                            recyclerViewAdapter.notifyDataSetChanged();
                        }
                    }
                })
        );

        recyclerViewAdapter.notifyDataSetChanged();
        isLoading = false;
    }

    @Override
    public void onTopicClick(int position) {
        topicsList.get(position);
        Intent i = new Intent(getActivity(),IndividualTopic.class);
        Bundle extras = new Bundle();
        extras.putString("topicDetails",gson.toJson(topicsList.get(position)));
        extras.putString("user",gson.toJson(user));
        i.putExtras(extras);
        startActivity(i);
    }
}
