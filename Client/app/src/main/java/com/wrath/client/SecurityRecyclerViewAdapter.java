package com.wrath.client;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wrath.client.dto.NotificationDetails;

import java.text.SimpleDateFormat;
import java.util.List;

public class SecurityRecyclerViewAdapter extends RecyclerView.Adapter<SecurityRecyclerViewAdapter.ViewHolder> {


    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    public List<NotificationDetails> notificationDetailsList;


    public SecurityRecyclerViewAdapter(List<NotificationDetails> notificationList) {
        notificationDetailsList = notificationList;
    }

    @NonNull
    @Override
    public SecurityRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_request, parent, false);
            return new SecurityRecyclerViewAdapter.ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new SecurityRecyclerViewAdapter.LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull SecurityRecyclerViewAdapter.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof SecurityRecyclerViewAdapter.ItemViewHolder) {
            populateItemRows((SecurityRecyclerViewAdapter.ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof SecurityRecyclerViewAdapter.LoadingViewHolder) {
            showLoadingView((SecurityRecyclerViewAdapter.LoadingViewHolder) viewHolder, position);
        }

    }

    @Override
    public int getItemCount() {
        return notificationDetailsList == null ? 0 : notificationDetailsList.size();
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return notificationDetailsList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private class ItemViewHolder extends SecurityRecyclerViewAdapter.ViewHolder {

        TextView blockName, flatNumber, visitorName, purpose, status,date;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            blockName = itemView.findViewById(R.id.blockname);
            flatNumber = itemView.findViewById(R.id.flatnumber);
            visitorName = itemView.findViewById(R.id.visitor_name);
            purpose = itemView.findViewById(R.id.purpose);
            status = itemView.findViewById(R.id.status);
            date = itemView.findViewById(R.id.date);
        }
    }

    private class LoadingViewHolder extends SecurityRecyclerViewAdapter.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(SecurityRecyclerViewAdapter.LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(SecurityRecyclerViewAdapter.ItemViewHolder viewHolder, int position) {
        NotificationDetails item = notificationDetailsList.get(position);
        viewHolder.blockName.setText(item.getBlock_visiting());
        viewHolder.flatNumber.setText(item.getFlatnum_visiting());
        viewHolder.purpose.setText(item.getPurpose());
        viewHolder.visitorName.setText(item.getVisitor_name());
        String status;
        if (item.getResponded()) {
            status = item.getConfirmed() ? "Approved" : "Declined";
        } else {
            status = "Pending...";
        }
        viewHolder.status.setText(status);
        String pattern ="EEE, d MMM yyyy, HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(item.getDate_created());
        viewHolder.date.setText(date);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }


}
