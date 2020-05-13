package com.servicing.jobaer.fastfixclient.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.servicing.jobaer.fastfixclient.R;
import com.servicing.jobaer.fastfixclient.controller.adapter.ServicesAdapter;
import com.servicing.jobaer.fastfixclient.controller.retrofit.ApiClient;
import com.servicing.jobaer.fastfixclient.controller.retrofit.ApiInterface;
import com.servicing.jobaer.fastfixclient.model.ServicesModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServicesFragment extends Fragment {
    private ArrayList<ServicesModel> servicesModels=new ArrayList<>();
    private ServicesAdapter servicesAdapter;
    public ServicesFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_services, container, false);
        RecyclerView serviceRecycler=v.findViewById(R.id.serviceRecycler);
        serviceRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        servicesAdapter=new ServicesAdapter(getContext(),servicesModels);
        serviceRecycler.setAdapter(servicesAdapter);

        getServiceList();

        return v;
    }

    private void getServiceList() {
        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<ServicesModel>> call=apiInterface.getServiceList();
        call.enqueue(new Callback<ArrayList<ServicesModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ServicesModel>> call, Response<ArrayList<ServicesModel>> response) {
                ArrayList<ServicesModel> temp=response.body();
                if (temp!=null) {
                    servicesModels.addAll(temp);
                    servicesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ServicesModel>> call, Throwable t) {

            }
        });
    }
}
