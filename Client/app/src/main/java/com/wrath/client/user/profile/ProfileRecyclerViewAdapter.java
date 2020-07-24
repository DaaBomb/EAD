package com.wrath.client.user.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wrath.client.R;
import com.wrath.client.dto.User;

import java.util.List;

public class ProfileRecyclerViewAdapter extends RecyclerView.Adapter<ProfileRecyclerViewAdapter.ViewHolder> {


    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    public List<User> userList;
    public OnProfileListener mOnProfileListener;


    public ProfileRecyclerViewAdapter(List<User> userList, OnProfileListener onProfileListener) {
        this.userList = userList;
        this.mOnProfileListener = onProfileListener;
    }

    @NonNull
    @Override
    public ProfileRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_resident, parent, false);
            return new ProfileRecyclerViewAdapter.ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new ProfileRecyclerViewAdapter.LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileRecyclerViewAdapter.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ProfileRecyclerViewAdapter.ItemViewHolder) {
            populateItemRows((ProfileRecyclerViewAdapter.ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof ProfileRecyclerViewAdapter.LoadingViewHolder) {
            showLoadingView((ProfileRecyclerViewAdapter.LoadingViewHolder) viewHolder, position);
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

    private class ItemViewHolder extends ProfileRecyclerViewAdapter.ViewHolder {

        TextView name;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView, mOnProfileListener);
            name = itemView.findViewById(R.id.textView27);
        }
    }

    private class LoadingViewHolder extends ProfileRecyclerViewAdapter.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView, mOnProfileListener);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(ProfileRecyclerViewAdapter.LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed
    }

    private void populateItemRows(ProfileRecyclerViewAdapter.ItemViewHolder viewHolder, int position) {
        User item = userList.get(position);
        viewHolder.name.setText(item.getName());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnProfileListener onProfileListener;

        public ViewHolder(@NonNull View itemView, OnProfileListener onProfileListener) {
            super(itemView);
            this.onProfileListener = onProfileListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onProfileListener.onProfileClick(getPosition());
        }
    }

    public interface OnProfileListener {
        void onProfileClick(int position);
    }
}