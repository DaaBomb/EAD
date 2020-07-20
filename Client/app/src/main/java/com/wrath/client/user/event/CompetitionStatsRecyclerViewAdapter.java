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

public class CompetitionStatsRecyclerViewAdapter extends RecyclerView.Adapter<CompetitionStatsRecyclerViewAdapter.ViewHolder>  {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    public List<Programme> mProgrammeList;
    public CompetitionStatsRecyclerViewAdapter.OnProgrammeListener mOnProgrammeListener;
    public User userObj;
    public CompetitionStatsRecyclerViewAdapter(List<Programme> programmeList, CompetitionStatsRecyclerViewAdapter.OnProgrammeListener onProgrammeListener, User userobj) {
        this.mProgrammeList = programmeList;
        this.mOnProgrammeListener = onProgrammeListener;
        this.userObj=userobj;
    }

    @NonNull
    @Override
    public CompetitionStatsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_competition_stats, parent, false);
            return new CompetitionStatsRecyclerViewAdapter.ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new CompetitionStatsRecyclerViewAdapter.LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CompetitionStatsRecyclerViewAdapter.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof CompetitionStatsRecyclerViewAdapter.ItemViewHolder) {
            populateItemRows((CompetitionStatsRecyclerViewAdapter.ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof CompetitionStatsRecyclerViewAdapter.LoadingViewHolder) {
            showLoadingView((CompetitionStatsRecyclerViewAdapter.LoadingViewHolder) viewHolder, position);
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

    private class ItemViewHolder extends CompetitionStatsRecyclerViewAdapter.ViewHolder {

        TextView competitionName;
        Button participants_count;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView, mOnProgrammeListener);
            competitionName = itemView.findViewById(R.id.textView13);
            participants_count = itemView.findViewById(R.id.participants_count_btn);

        }
    }

    private class LoadingViewHolder extends CompetitionStatsRecyclerViewAdapter.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView, mOnProgrammeListener);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(CompetitionStatsRecyclerViewAdapter.LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(CompetitionStatsRecyclerViewAdapter.ItemViewHolder viewHolder, int position) {
        Programme item = mProgrammeList.get(position);
        viewHolder.competitionName.setText(item.getName());
        viewHolder.participants_count.setText(String.valueOf(item.getParticipants().size()));
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CompetitionStatsRecyclerViewAdapter.OnProgrammeListener onProgrammeListener;

        public ViewHolder(@NonNull View itemView, CompetitionStatsRecyclerViewAdapter.OnProgrammeListener onProgrammeListener) {
            super(itemView);
            this.onProgrammeListener = onProgrammeListener;
            itemView.findViewById(R.id.participants_count_btn).setOnClickListener(this);
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
