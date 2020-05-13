package com.servicing.jobaer.fastfixclient.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.servicing.jobaer.fastfixclient.R;
import com.servicing.jobaer.fastfixclient.model.ServicesModel;

import java.util.ArrayList;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ServicesViewHolder> {
    private Context context;
    private ArrayList<ServicesModel> servicesModels;

    public ServicesAdapter(Context context, ArrayList<ServicesModel> servicesModels) {
        this.context = context;
        this.servicesModels = servicesModels;
    }

    @NonNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServicesViewHolder(LayoutInflater.from(context).inflate(R.layout.services_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesViewHolder holder, int position) {
        holder.name.setText(servicesModels.get(position).getCategory());
    }

    @Override
    public int getItemCount() {
        return servicesModels.size();
    }

    class ServicesViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        public ServicesViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
        }
    }
}
