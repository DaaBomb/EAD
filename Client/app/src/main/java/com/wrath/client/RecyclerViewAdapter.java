package com.wrath.client;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wrath.client.dto.Topic;

import java.text.SimpleDateFormat;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    public List<Topic> mTopicList;
    public OnTopicListener mOnTopicListener;
    public RecyclerViewAdapter(List<Topic> topicList, OnTopicListener onTopicListener) {
        mTopicList = topicList;
        this.mOnTopicListener = onTopicListener;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ItemViewHolder) {
            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }

    }

    @Override
    public int getItemCount() {
        return mTopicList == null ? 0 : mTopicList.size();
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return mTopicList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private class ItemViewHolder extends RecyclerViewAdapter.ViewHolder {

        TextView tvItem;
        TextView date_topic;
        TextView creator_name;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView, mOnTopicListener);
            tvItem = itemView.findViewById(R.id.tvItem);
            date_topic=itemView.findViewById(R.id.date_topic);
            creator_name=itemView.findViewById(R.id.creator_name);
        }
    }

    private class LoadingViewHolder extends RecyclerViewAdapter.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView, mOnTopicListener);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(ItemViewHolder viewHolder, int position) {
        Topic item = mTopicList.get(position);
        viewHolder.tvItem.setText(item.getTopic());
        String pattern ="EEE, d MMM yyyy, HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(item.getDate_created());
        viewHolder.date_topic.setText(date);
        viewHolder.creator_name.setText(item.getCreator_name());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnTopicListener onTopicListener;

        public ViewHolder(@NonNull View itemView, OnTopicListener onTopicListener) {
            super(itemView);
            this.onTopicListener = onTopicListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onTopicListener.onTopicClick(getPosition());
        }
    }

    public interface OnTopicListener {
        void onTopicClick(int position);
    }
}

