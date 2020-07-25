package com.wrath.client.user.sport;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.wrath.client.R;
import com.wrath.client.dto.Sport;
import com.wrath.client.dto.User;

import java.text.SimpleDateFormat;
import java.util.List;

public class SportsRecyclerViewAdapter extends RecyclerView.Adapter<SportsRecyclerViewAdapter.ViewHolder> {


    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    public List<Sport> sportList;
    public OnSportListener mOnSportListener;
    private User user;

    public SportsRecyclerViewAdapter(List<Sport> sportList, OnSportListener onSportListener, User user) {
        this.sportList = sportList;
        this.mOnSportListener = onSportListener;
        this.user = user;
    }

    @NonNull
    @Override
    public SportsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_esport, parent, false);
            return new SportsRecyclerViewAdapter.ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new SportsRecyclerViewAdapter.LoadingViewHolder(view);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(@NonNull SportsRecyclerViewAdapter.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof SportsRecyclerViewAdapter.ItemViewHolder) {
            populateItemRows((SportsRecyclerViewAdapter.ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof SportsRecyclerViewAdapter.LoadingViewHolder) {
            showLoadingView((SportsRecyclerViewAdapter.LoadingViewHolder) viewHolder, position);
        }

    }

    @Override
    public int getItemCount() {
        return sportList == null ? 0 : sportList.size();
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return sportList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private class ItemViewHolder extends SportsRecyclerViewAdapter.ViewHolder {

        TextView competitionName, organiser, remainingSlots, time, date, roomId;
        Button participate_btn;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView, mOnSportListener);
            competitionName = itemView.findViewById(R.id.competitionName);
            organiser = itemView.findViewById(R.id.competitionName2);
            remainingSlots = itemView.findViewById(R.id.competitionName4);
            time = itemView.findViewById(R.id.event_time);
            date = itemView.findViewById(R.id.competitonDetails);
            roomId = itemView.findViewById(R.id.event_details2);
            participate_btn = itemView.findViewById(R.id.participate_btn);
        }
    }

    private class LoadingViewHolder extends SportsRecyclerViewAdapter.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView, mOnSportListener);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(SportsRecyclerViewAdapter.LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void populateItemRows(SportsRecyclerViewAdapter.ItemViewHolder viewHolder, int position) {
        Sport item = sportList.get(position);
        viewHolder.competitionName.setText(item.getSport());
        viewHolder.organiser.setText(item.getCreated_by().getName() + " (" + item.getCreated_by().getAddress().getBlockname() + " - " + item.getCreated_by().getAddress().getFlatnum() + ")");
        String freeSlots = "" + (item.getNumberOfPlayers() - item.getParticipants().size());
        viewHolder.remainingSlots.setText(freeSlots);
        String[] arr = item.getTime().split(":");
        String event_time="";
        if(arr.length>0 ){
            if(Integer.parseInt(arr[0])>12)
                event_time=item.getTime() +" PM";
            else
                event_time=item.getTime() +" AM";
        }
        viewHolder.time.setText(event_time);
        viewHolder.roomId.setText(item.getDescription());
        String pattern = "EEE, d MMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(item.getDate_created());
        viewHolder.date.setText(date);
        if (item.getParticipants().contains(user) || item.getNumberOfPlayers() - item.getParticipants().size() == 0)
            viewHolder.participate_btn.setVisibility(View.INVISIBLE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnSportListener onSportListener;

        public ViewHolder(@NonNull View itemView, OnSportListener onSportListener) {
            super(itemView);
            this.onSportListener = onSportListener;
            itemView.setOnClickListener(this);
            Button participateBtn = itemView.findViewById(R.id.participate_btn);
            if (participateBtn != null)
                participateBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.participate_btn)
                onSportListener.onParticipateClick(getPosition());
            else
                onSportListener.onSportClick(getPosition());
        }
    }

    public interface OnSportListener {
        void onSportClick(int position);

        void onParticipateClick(int position);
    }
}