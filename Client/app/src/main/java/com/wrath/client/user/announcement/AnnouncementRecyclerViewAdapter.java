package com.wrath.client.user.announcement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wrath.client.R;
import com.wrath.client.dto.Announcement;

import java.text.SimpleDateFormat;
import java.util.List;

public class AnnouncementRecyclerViewAdapter extends RecyclerView.Adapter<AnnouncementRecyclerViewAdapter.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    public List<Announcement> announcementList;

    public AnnouncementRecyclerViewAdapter(List<Announcement> announcements) {
        announcementList = announcements;
    }

    @NonNull
    @Override
    public AnnouncementRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_announcement, parent, false);
            return new AnnouncementRecyclerViewAdapter.ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new AnnouncementRecyclerViewAdapter.LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementRecyclerViewAdapter.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof AnnouncementRecyclerViewAdapter.ItemViewHolder) {
            populateItemRows((AnnouncementRecyclerViewAdapter.ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof AnnouncementRecyclerViewAdapter.LoadingViewHolder) {
            showLoadingView((AnnouncementRecyclerViewAdapter.LoadingViewHolder) viewHolder, position);
        }

    }

    @Override
    public int getItemCount() {
        return announcementList == null ? 0 : announcementList.size();
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return announcementList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private class ItemViewHolder extends AnnouncementRecyclerViewAdapter.ViewHolder {

        TextView announcement, dateCreated;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            announcement = itemView.findViewById(R.id.event_details);
            dateCreated = itemView.findViewById(R.id.event_date);
        }
    }

    private class LoadingViewHolder extends AnnouncementRecyclerViewAdapter.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(AnnouncementRecyclerViewAdapter.LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(AnnouncementRecyclerViewAdapter.ItemViewHolder viewHolder, int position) {
        Announcement item = announcementList.get(position);
        viewHolder.announcement.setText(item.getAnnouncement());
        String pattern = "EEE, d MMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(item.getDate_created());
        viewHolder.dateCreated.setText(date);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
