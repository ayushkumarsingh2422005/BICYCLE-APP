package com.innoreva.pedaller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BicycleAdapter extends RecyclerView.Adapter<BicycleAdapter.BicycleViewHolder> {

    private Context context;
    private List<Bicycle> bicycleList;

    public BicycleAdapter(Context context, List<Bicycle> bicycleList) {
        this.context = context;
        this.bicycleList = bicycleList;
    }

    @NonNull
    @Override
    public BicycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bicycle, parent, false);
        return new BicycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BicycleViewHolder holder, int position) {
        Bicycle bicycle = bicycleList.get(position);

        holder.locationTextView.setText(bicycle.getLocation());
        holder.descriptionTextView.setText(bicycle.getDescription());
        holder.costTextView.setText("Cost per hour - Rs. " + bicycle.getCostPerHour());
        holder.ownerTextView.setText("Owner - " + bicycle.getOwner());
        holder.availabilityTextView.setText("Available time - " + bicycle.getAvailability());
        holder.bicycleImageView.setImageResource(bicycle.getImageResourceId());

        // Handle remove button click
        holder.removeButton.setOnClickListener(v -> {
            // Code to remove the item
        });

        // Handle edit button click
        holder.editButton.setOnClickListener(v -> {
            // Code to edit the item
        });
    }

    @Override
    public int getItemCount() {
        return bicycleList.size();
    }

    public static class BicycleViewHolder extends RecyclerView.ViewHolder {
        ImageView bicycleImageView;
        TextView locationTextView, descriptionTextView, costTextView, ownerTextView, availabilityTextView;
        Button removeButton, editButton;

        public BicycleViewHolder(@NonNull View itemView) {
            super(itemView);
            bicycleImageView = itemView.findViewById(R.id.bicycleImage);
            locationTextView = itemView.findViewById(R.id.bicycleLocation);
            descriptionTextView = itemView.findViewById(R.id.bicycleDescription);
            costTextView = itemView.findViewById(R.id.bicycleCost);
            ownerTextView = itemView.findViewById(R.id.bicycleOwner);
            availabilityTextView = itemView.findViewById(R.id.bicycleAvailability);
            removeButton = itemView.findViewById(R.id.btnRemove);
            editButton = itemView.findViewById(R.id.btnEdit);
        }
    }
}
