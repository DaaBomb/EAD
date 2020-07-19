package com.wrath.client.user.event;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wrath.client.R;
import com.wrath.client.dto.Programme;

import java.util.List;

public class ProgrammeRecyclerViewAdapter extends RecyclerView.Adapter<ProgrammeRecyclerViewAdapter.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    public List<Programme> mProgrammeList;
    public ProgrammeRecyclerViewAdapter.OnProgrammeListener mOnProgrammeListener;

    public ProgrammeRecyclerViewAdapter(List<Programme> programmeList, ProgrammeRecyclerViewAdapter.OnProgrammeListener onProgrammeListener) {
        this.mProgrammeList = programmeList;
        this.mOnProgrammeListener = onProgrammeListener;
    }

    @NonNull
    @Override
    public ProgrammeRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.competition_recycler_element, parent, false);
            return new ProgrammeRecyclerViewAdapter.ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new ProgrammeRecyclerViewAdapter.LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ProgrammeRecyclerViewAdapter.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ProgrammeRecyclerViewAdapter.ItemViewHolder) {
            populateItemRows((ProgrammeRecyclerViewAdapter.ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof ProgrammeRecyclerViewAdapter.LoadingViewHolder) {
            showLoadingView((ProgrammeRecyclerViewAdapter.LoadingViewHolder) viewHolder, position);
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

    private class ItemViewHolder extends ProgrammeRecyclerViewAdapter.ViewHolder {

        TextView competitionName;
        TextView description;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView, mOnProgrammeListener);
            competitionName = itemView.findViewById(R.id.textInputEditText2);
            description = itemView.findViewById(R.id.textInputEditText3);
        }
    }

    private class LoadingViewHolder extends ProgrammeRecyclerViewAdapter.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView, mOnProgrammeListener);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(ProgrammeRecyclerViewAdapter.LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(ProgrammeRecyclerViewAdapter.ItemViewHolder viewHolder, int position) {
        Programme item = mProgrammeList.get(position);
        viewHolder.competitionName.setText(item.getName());
        viewHolder.description.setText(item.getDescription());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ProgrammeRecyclerViewAdapter.OnProgrammeListener onProgrammeListener;

        public ViewHolder(@NonNull View itemView, ProgrammeRecyclerViewAdapter.OnProgrammeListener onProgrammeListener) {
            super(itemView);
            this.onProgrammeListener = onProgrammeListener;

            itemView.setOnClickListener(this);
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