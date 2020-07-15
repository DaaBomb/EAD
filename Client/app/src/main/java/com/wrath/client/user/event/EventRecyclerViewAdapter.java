package com.wrath.client.user.event;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wrath.client.R;
import com.wrath.client.dto.EventDetails;

import java.text.SimpleDateFormat;
import java.util.List;

public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder>{

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    public List<EventDetails> mEventList;
    public EventRecyclerViewAdapter.OnEventListener mOnEventListener;
    public EventRecyclerViewAdapter(List<EventDetails> EventList, EventRecyclerViewAdapter.OnEventListener onEventListener) {
        mEventList = EventList;
        this.mOnEventListener = onEventListener;
    }

    @NonNull
    @Override
    public EventRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_event, parent, false);
            return new EventRecyclerViewAdapter.ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new EventRecyclerViewAdapter.LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull EventRecyclerViewAdapter.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof EventRecyclerViewAdapter.ItemViewHolder) {
            populateItemRows((EventRecyclerViewAdapter.ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof EventRecyclerViewAdapter.LoadingViewHolder) {
            showLoadingView((EventRecyclerViewAdapter.LoadingViewHolder) viewHolder, position);
        }

    }

    @Override
    public int getItemCount() {
        return mEventList == null ? 0 : mEventList.size();
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return mEventList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private class ItemViewHolder extends EventRecyclerViewAdapter.ViewHolder {

        TextView name,description,starting_date,event_time,number;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView, mOnEventListener);
            name = itemView.findViewById(R.id.event_title);
            description=itemView.findViewById(R.id.event_details);
            starting_date=itemView.findViewById(R.id.event_date);
            event_time=itemView.findViewById(R.id.event_time);
            number=itemView.findViewById(R.id.number);
        }
    }

    private class LoadingViewHolder extends EventRecyclerViewAdapter.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView, mOnEventListener);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(EventRecyclerViewAdapter.LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(EventRecyclerViewAdapter.ItemViewHolder viewHolder, int position) {
        EventDetails item = mEventList.get(position);
        viewHolder.name.setText(item.getName());
        String pattern ="EEE, d MMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(item.getStart_date());
        viewHolder.starting_date.setText(date);
        viewHolder.description.setText(item.getDescription());
        viewHolder.event_time.setText("10:00 AM");
        viewHolder.number.setText(String.valueOf(item.getAttending().size()));
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        EventRecyclerViewAdapter.OnEventListener onEventListener;

        public ViewHolder(@NonNull View itemView, EventRecyclerViewAdapter.OnEventListener onEventListener) {
            super(itemView);
            this.onEventListener = onEventListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onEventListener.onEventClick(getPosition());
        }
    }

    public interface OnEventListener {
        void onEventClick(int position);
    }
}
