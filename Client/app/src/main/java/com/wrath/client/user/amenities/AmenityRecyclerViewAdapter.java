package com.wrath.client.user.amenities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wrath.client.R;

import java.util.List;

public class AmenityRecyclerViewAdapter extends RecyclerView.Adapter<AmenityRecyclerViewAdapter.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    public List<String> amenityList;
    public OnAmenityClickListener mOnAmenityClickListener;
    boolean isReadOnly;

    public AmenityRecyclerViewAdapter(List<String> amenities, OnAmenityClickListener onAmenityClickListener, boolean isReadOnly) {
        amenityList = amenities;
        this.mOnAmenityClickListener = onAmenityClickListener;
        this.isReadOnly = isReadOnly;
    }

    @NonNull
    @Override
    public AmenityRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_amenity, parent, false);
            return new AmenityRecyclerViewAdapter.ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new AmenityRecyclerViewAdapter.LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AmenityRecyclerViewAdapter.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof AmenityRecyclerViewAdapter.ItemViewHolder) {
            populateItemRows((AmenityRecyclerViewAdapter.ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof AmenityRecyclerViewAdapter.LoadingViewHolder) {
            showLoadingView((AmenityRecyclerViewAdapter.LoadingViewHolder) viewHolder, position);
        }

    }

    @Override
    public int getItemCount() {
        return amenityList == null ? 0 : amenityList.size();
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return amenityList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private class ItemViewHolder extends AmenityRecyclerViewAdapter.ViewHolder {

        TextView amenity;
        ImageButton removeAmenity;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView, mOnAmenityClickListener);
            amenity = itemView.findViewById(R.id.textInputEditText2);
            removeAmenity = itemView.findViewById(R.id.button6);
        }
    }

    private class LoadingViewHolder extends AmenityRecyclerViewAdapter.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView, mOnAmenityClickListener);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(AmenityRecyclerViewAdapter.LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(AmenityRecyclerViewAdapter.ItemViewHolder viewHolder, int position) {
        String item = amenityList.get(position);
        viewHolder.amenity.setText(item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnAmenityClickListener onAmenityClickListener;

        public ViewHolder(@NonNull View itemView, OnAmenityClickListener onAmenityClickListener) {
            super(itemView);
            this.onAmenityClickListener = onAmenityClickListener;
            itemView.findViewById(R.id.textView18).setVisibility(View.INVISIBLE);
            if(!isReadOnly)
                itemView.findViewById(R.id.button6).setOnClickListener(this);
            else
                itemView.findViewById(R.id.button6).setVisibility(View.INVISIBLE);
        }

        @Override
        public void onClick(View v) {
            onAmenityClickListener.onAmenityClick(getPosition());
        }
    }

    public interface OnAmenityClickListener {
        void onAmenityClick(int position);
    }

}
