package com.wrath.client.user.concierge;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wrath.client.R;
import com.wrath.client.dto.Concierge;

import java.text.SimpleDateFormat;
import java.util.List;

public class ConciergeRecyclerViewAdapter extends RecyclerView.Adapter<ConciergeRecyclerViewAdapter.ViewHolder> {


    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    public List<Concierge> conciergeList;


    public ConciergeRecyclerViewAdapter(List<Concierge> conciergeList) {
        this.conciergeList = conciergeList;
    }

    @NonNull
    @Override
    public ConciergeRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_concierge_request, parent, false);
            return new ConciergeRecyclerViewAdapter.ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new ConciergeRecyclerViewAdapter.LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ConciergeRecyclerViewAdapter.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ConciergeRecyclerViewAdapter.ItemViewHolder) {
            populateItemRows((ConciergeRecyclerViewAdapter.ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof ConciergeRecyclerViewAdapter.LoadingViewHolder) {
            showLoadingView((ConciergeRecyclerViewAdapter.LoadingViewHolder) viewHolder, position);
        }

    }

    @Override
    public int getItemCount() {
        return conciergeList == null ? 0 : conciergeList.size();
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return conciergeList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private class ItemViewHolder extends ConciergeRecyclerViewAdapter.ViewHolder {

        TextView flatNumber, requirement, details, time, date, status;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            flatNumber = itemView.findViewById(R.id.event_title2);
            requirement = itemView.findViewById(R.id.event_title);
            details = itemView.findViewById(R.id.event_details);
            time = itemView.findViewById(R.id.event_time);
            date = itemView.findViewById(R.id.event_date);
            status = itemView.findViewById(R.id.event_title3);
        }
    }

    private class LoadingViewHolder extends ConciergeRecyclerViewAdapter.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(ConciergeRecyclerViewAdapter.LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed
    }

    private void populateItemRows(ConciergeRecyclerViewAdapter.ItemViewHolder viewHolder, int position) {
        Concierge item = conciergeList.get(position);
        viewHolder.flatNumber.setText(item.getBlockname() + " - " + item.getFlatnum());
        viewHolder.requirement.setText(item.getRequirement());
        viewHolder.details.setText(item.getDetails());
        viewHolder.time.setText(item.getTime_needed());
        String status;
        if (item.isResponded()) {
            status = item.isApproved() ? "Approved" : "Declined";
        } else {
            status = "Pending...";
        }
        viewHolder.status.setText(status);
        String pattern = "EEE, d MMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(item.getDate_needed());
        viewHolder.date.setText(date);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }
}