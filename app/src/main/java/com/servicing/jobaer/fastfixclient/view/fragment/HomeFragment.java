package com.servicing.jobaer.fastfixclient.view.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.servicing.jobaer.fastfixclient.R;
import com.servicing.jobaer.fastfixclient.controller.retrofit.ApiClient;
import com.servicing.jobaer.fastfixclient.controller.retrofit.ApiInterface;
import com.servicing.jobaer.fastfixclient.model.MessageModel;
import com.servicing.jobaer.fastfixclient.view.activity.CreateRequestActivity;
import com.servicing.jobaer.fastfixclient.view.activity.TermsActivity;
import com.servicing.jobaer.fastfixclient.view.activity.ViewRequestActivity;
import com.squareup.picasso.Picasso;
import com.stfalcon.imageviewer.StfalconImageViewer;
import com.stfalcon.imageviewer.loader.ImageLoader;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    ImageView messageView;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_home, container, false);
        CardView creayeRequestCard=v.findViewById(R.id.creayeRequestCard);
        messageView=v.findViewById(R.id.messageView);
        creayeRequestCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CreateRequestActivity.class));
            }
        });
        Button sosButton=v.findViewById(R.id.sosButton);
        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TermsActivity.class));
            }
        });
        http:
//157.245.181.14:8030/media/Bilal%20jmal%20is%20working%20perfect
        getMessage();
        return v;

    }

    private void getMessage() {
        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<MessageModel>> call=apiInterface.getMessage();
        call.enqueue(new Callback<ArrayList<MessageModel>>() {
            @Override
            public void onResponse(Call<ArrayList<MessageModel>> call, Response<ArrayList<MessageModel>> response) {
                ArrayList<MessageModel> messageModel=response.body();
                if (messageModel!=null) {
                    Picasso.get().load(messageModel.get(0).getBody()).into(messageView);
                    //essageView.setText(messageModel.get(0).getBody());
                    messageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ArrayList<String> images = new ArrayList<>();
                            images.add(messageModel.get(0).getBody());
                            new StfalconImageViewer.Builder<>(getContext(), images, new ImageLoader<String>() {
                                @Override
                                public void loadImage(ImageView imageView, String image) {
                                    Picasso.get().load(image).into(imageView);
                                }
                            }).withStartPosition(0).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MessageModel>> call, Throwable t) {

            }
        });
    }
}
