package com.wrath.client.user.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wrath.client.R;
import com.wrath.client.dto.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.List;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    public List<ChatMessage> chatMessages;

    public ChatRecyclerViewAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public ChatRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_announcement, parent, false);
            return new ChatRecyclerViewAdapter.ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new ChatRecyclerViewAdapter.LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRecyclerViewAdapter.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ChatRecyclerViewAdapter.ItemViewHolder) {
            populateItemRows((ChatRecyclerViewAdapter.ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof ChatRecyclerViewAdapter.LoadingViewHolder) {
            showLoadingView((ChatRecyclerViewAdapter.LoadingViewHolder) viewHolder, position);
        }

    }

    @Override
    public int getItemCount() {
        return chatMessages == null ? 0 : chatMessages.size();
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return chatMessages.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private class ItemViewHolder extends ChatRecyclerViewAdapter.ViewHolder {

        TextView message, dateCreated;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.event_details);
            dateCreated = itemView.findViewById(R.id.event_date);
        }
    }

    private class LoadingViewHolder extends ChatRecyclerViewAdapter.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(ChatRecyclerViewAdapter.LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(ChatRecyclerViewAdapter.ItemViewHolder viewHolder, int position) {
        ChatMessage item = chatMessages.get(position);
        viewHolder.message.setText(item.getMessage());
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
