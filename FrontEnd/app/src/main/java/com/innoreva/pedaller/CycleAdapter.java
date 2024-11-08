package com.innoreva.pedaller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CycleAdapter extends RecyclerView.Adapter<CycleAdapter.CycleViewHolder> {

    private List<Cycle> cycleList;

    public CycleAdapter(List<Cycle> cycleList) {
        this.cycleList = cycleList;
    }

    @NonNull
    @Override
    public CycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cycle_item, parent, false);
        return new CycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CycleViewHolder holder, int position) {
        Cycle cycle = cycleList.get(position);
        holder.locationTextView.setText(cycle.getLocation());
        holder.costTextView.setText(cycle.getCostPerHour());
        holder.timeTextView.setText(cycle.getAvailableTime());
        holder.cycleImageView.setImageResource(cycle.getImageResourceId());
    }

    @Override
    public int getItemCount() {
        return cycleList.size();
    }

    static class CycleViewHolder extends RecyclerView.ViewHolder {
        TextView locationTextView, costTextView, timeTextView;
        ImageView cycleImageView;

        public CycleViewHolder(@NonNull View itemView) {
            super(itemView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            costTextView = itemView.findViewById(R.id.costTextView);
            timeTextView = itemView.findViewById(R.id.availabilityTextView);
            cycleImageView = itemView.findViewById(R.id.cycleImageView);
        }
    }
}
