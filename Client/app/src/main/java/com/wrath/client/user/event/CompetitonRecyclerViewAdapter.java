package com.wrath.client.user.event;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wrath.client.R;
import com.wrath.client.dto.Programme;
import com.wrath.client.dto.User;

import java.util.List;

public class CompetitonRecyclerViewAdapter extends RecyclerView.Adapter<CompetitonRecyclerViewAdapter.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    public List<Programme> mProgrammeList;
    public CompetitonRecyclerViewAdapter.OnProgrammeListener mOnProgrammeListener;
    public User userObj;
    public CompetitonRecyclerViewAdapter(List<Programme> programmeList, CompetitonRecyclerViewAdapter.OnProgrammeListener onProgrammeListener, User userobj) {
        this.mProgrammeList = programmeList;
        this.mOnProgrammeListener = onProgrammeListener;
        this.userObj=userobj;
    }

    @NonNull
    @Override
    public CompetitonRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_competition, parent, false);
            return new CompetitonRecyclerViewAdapter.ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new CompetitonRecyclerViewAdapter.LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CompetitonRecyclerViewAdapter.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof CompetitonRecyclerViewAdapter.ItemViewHolder) {
            populateItemRows((CompetitonRecyclerViewAdapter.ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof CompetitonRecyclerViewAdapter.LoadingViewHolder) {
            showLoadingView((CompetitonRecyclerViewAdapter.LoadingViewHolder) viewHolder, position);
        }

    }

    @Override
    public int getItemCount() {
        return mProgrammeList == null ? 0 : mProgrammeList.size();
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return mProgrammeList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private class ItemViewHolder extends CompetitonRecyclerViewAdapter.ViewHolder {

        TextView competitionName;
        TextView description;
        Button participate_btn;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView, mOnProgrammeListener);
            competitionName = itemView.findViewById(R.id.competitionName);
            description = itemView.findViewById(R.id.competitonDetails);
            participate_btn = itemView.findViewById(R.id.participate_btn);
        }
    }

    private class LoadingViewHolder extends CompetitonRecyclerViewAdapter.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView, mOnProgrammeListener);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(CompetitonRecyclerViewAdapter.LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(CompetitonRecyclerViewAdapter.ItemViewHolder viewHolder, int position) {
        Programme item = mProgrammeList.get(position);
        viewHolder.competitionName.setText(item.getName());
        viewHolder.description.setText(item.getDescription());
        viewHolder.participate_btn.setText(item.getParticipants().contains(userObj.get_id())?"I won't be participating":"Yes, I'm participating");
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CompetitonRecyclerViewAdapter.OnProgrammeListener onProgrammeListener;

        public ViewHolder(@NonNull View itemView, CompetitonRecyclerViewAdapter.OnProgrammeListener onProgrammeListener) {
            super(itemView);
            this.onProgrammeListener = onProgrammeListener;
            itemView.findViewById(R.id.participate_btn).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onProgrammeListener.onProgrammeClick(getPosition());
        }
    }

    public interface OnProgrammeListener {
        void onProgrammeClick(int position);
    }
}
