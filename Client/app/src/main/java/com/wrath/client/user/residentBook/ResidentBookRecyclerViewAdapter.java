package com.wrath.client.user.residentBook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wrath.client.R;
import com.wrath.client.dto.User;

import java.util.List;

public class ResidentBookRecyclerViewAdapter extends RecyclerView.Adapter<ResidentBookRecyclerViewAdapter.ViewHolder> {


    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    public List<User> userList;
    public OnChatClickListener mOnChatClickListener;


    public ResidentBookRecyclerViewAdapter(List<User> users, OnChatClickListener onChatClickListener) {
        this.userList = users;
        this.mOnChatClickListener = onChatClickListener;
    }

    @NonNull
    @Override
    public ResidentBookRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_resident_display, parent, false);
            return new ResidentBookRecyclerViewAdapter.ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new ResidentBookRecyclerViewAdapter.LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ResidentBookRecyclerViewAdapter.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ResidentBookRecyclerViewAdapter.ItemViewHolder) {
            populateItemRows((ResidentBookRecyclerViewAdapter.ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof ResidentBookRecyclerViewAdapter.LoadingViewHolder) {
            showLoadingView((ResidentBookRecyclerViewAdapter.LoadingViewHolder) viewHolder, position);
        }

    }

    @Override
    public int getItemCount() {
        return userList == null ? 0 : userList.size();
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return userList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private class ItemViewHolder extends ResidentBookRecyclerViewAdapter.ViewHolder {

        TextView details;
        ImageView chat;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView, mOnChatClickListener);
            details = itemView.findViewById(R.id.event_details);
            chat = itemView.findViewById(R.id.imageView7);
        }
    }

    private class LoadingViewHolder extends ResidentBookRecyclerViewAdapter.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView, mOnChatClickListener);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(ResidentBookRecyclerViewAdapter.LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed
    }

    private void populateItemRows(ResidentBookRecyclerViewAdapter.ItemViewHolder viewHolder, int position) {
        User item = userList.get(position);
        viewHolder.details.setText(item.getName() + "\n" + item.getAddress().getBlockname() + " - " + item.getAddress().getFlatnum());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnChatClickListener onChatClickListener;

        public ViewHolder(@NonNull View itemView, OnChatClickListener onChatClickListener) {
            super(itemView);
            this.onChatClickListener = onChatClickListener;
            itemView.findViewById(R.id.imageView7).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onChatClickListener.onChatClick(getPosition());
        }
    }

    public interface OnChatClickListener {
        void onChatClick(int position);
    }
}