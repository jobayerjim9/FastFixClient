package com.servicing.jobaer.fastfixclient.controller.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.servicing.jobaer.fastfixclient.R;
import com.servicing.jobaer.fastfixclient.model.requests.RequestModel;
import com.servicing.jobaer.fastfixclient.view.activity.ViewRequestActivity;

import java.util.ArrayList;

public class RequestListAdapter extends RecyclerView.Adapter<RequestListAdapter.RequestListViewHolder> {
    private Context context;
    private ArrayList<RequestModel> requestModels;

    public RequestListAdapter(Context context, ArrayList<RequestModel> requestModels) {
        this.context = context;
        this.requestModels = requestModels;
    }

    @NonNull
    @Override
    public RequestListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RequestListViewHolder(LayoutInflater.from(context).inflate(R.layout.request_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RequestListViewHolder holder, int position) {
        holder.name_id.setText(requestModels.get(position).getCode());
        holder.date.setText(requestModels.get(position).getCreated());
        holder.status.setText(requestModels.get(position).getStatus());

        holder.viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewRequestActivity.class);
                intent.putExtra("id", requestModels.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestModels.size();
    }

    class RequestListViewHolder extends RecyclerView.ViewHolder {
        TextView name_id, date, status, viewButton;

        public RequestListViewHolder(@NonNull View itemView) {
            super(itemView);
            name_id = itemView.findViewById(R.id.name_id);
            date = itemView.findViewById(R.id.date);
            status = itemView.findViewById(R.id.status);
            viewButton = itemView.findViewById(R.id.viewButton);

        }
    }
}
