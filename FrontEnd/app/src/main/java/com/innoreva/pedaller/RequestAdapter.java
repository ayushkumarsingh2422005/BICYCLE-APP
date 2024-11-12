package com.innoreva.pedaller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    private Context context;
    private List<Request> requestList;

    public RequestAdapter(Context context, List<Request> requestList) {
        this.context = context;
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        Request request = requestList.get(position);

        holder.tvName.setText("Name - " + request.getName());
        holder.tvRegistrationNumber.setText("Registration number - " + request.getRegistrationNumber());
        holder.tvTimeSlot.setText("Time slot - " + request.getTimeSlot());

        // Implement button click listeners as needed
        holder.btnAccept.setOnClickListener(v -> {
            // Handle accept action
        });

        holder.btnDeny.setOnClickListener(v -> {
            // Handle deny action
        });

        holder.btnCall.setOnClickListener(v -> {
            // Handle call action
        });

        holder.btnChat.setOnClickListener(v -> {
            // Handle chat action
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvRegistrationNumber, tvTimeSlot;
        Button btnAccept, btnDeny;
        ImageButton btnCall, btnChat;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvRegistrationNumber = itemView.findViewById(R.id.tvRegistrationNumber);
            tvTimeSlot = itemView.findViewById(R.id.tvTimeSlot);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnDeny = itemView.findViewById(R.id.btnDeny);
            btnCall = itemView.findViewById(R.id.btnCall);
            btnChat = itemView.findViewById(R.id.btnChat);
        }
    }
}
