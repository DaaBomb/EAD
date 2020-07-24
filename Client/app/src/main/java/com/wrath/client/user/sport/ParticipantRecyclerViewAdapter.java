package com.wrath.client.user.sport;

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

public class ParticipantRecyclerViewAdapter extends RecyclerView.Adapter<ParticipantRecyclerViewAdapter.ViewHolder> {


    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    public List<User> userList;
    public OnProfileListener mOnProfileListener;


    public ParticipantRecyclerViewAdapter(List<User> userList, OnProfileListener onProfileListener) {
        this.userList = userList;
        this.mOnProfileListener = onProfileListener;
    }

    @NonNull
    @Override
    public ParticipantRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_user, parent, false);
            return new ParticipantRecyclerViewAdapter.ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new ParticipantRecyclerViewAdapter.LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantRecyclerViewAdapter.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ParticipantRecyclerViewAdapter.ItemViewHolder) {
            populateItemRows((ParticipantRecyclerViewAdapter.ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof ParticipantRecyclerViewAdapter.LoadingViewHolder) {
            showLoadingView((ParticipantRecyclerViewAdapter.LoadingViewHolder) viewHolder, position);
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

    private class ItemViewHolder extends ParticipantRecyclerViewAdapter.ViewHolder {

        TextView name, flat;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView, mOnProfileListener);
            name = itemView.findViewById(R.id.name);
            flat = itemView.findViewById(R.id.type3);
        }
    }

    private class LoadingViewHolder extends ParticipantRecyclerViewAdapter.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView, mOnProfileListener);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(ParticipantRecyclerViewAdapter.LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed
    }

    private void populateItemRows(ParticipantRecyclerViewAdapter.ItemViewHolder viewHolder, int position) {
        User item = userList.get(position);
        viewHolder.name.setText(item.getName());
        viewHolder.flat.setText(item.getAddress().getBlockname() + " - " + item.getAddress().getFlatnum());
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