package com.servicing.jobaer.fastfixclient.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.view.ContextThemeWrapper;
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

import org.imaginativeworld.whynotimagecarousel.CarouselItem;
import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    ImageView messageView;
    List<CarouselItem> list = new ArrayList<>();
    public HomeFragment() {
        // Required empty public constructor
    }

    ImageCarousel carousel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // create ContextThemeWrapper from the original Activity Context with the custom theme
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.AppThemeMaterial);

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        // inflate the layout using the cloned inflater, not default inflater
        View v = localInflater.inflate(R.layout.fragment_home, container, false);

        CardView creayeRequestCard=v.findViewById(R.id.creayeRequestCard);

        carousel = v.findViewById(R.id.carousel);

        carousel.setAutoPlay(true);
        carousel.setShowNavigationButtons(false);
        creayeRequestCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TermsActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);

            }
        });
        Button sosButton=v.findViewById(R.id.sosButton);
        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TermsActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });
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
                    // Picasso.get().load(messageModel.get(0).getBody()).into(messageView);
                    //essageView.setText(messageModel.get(0).getBody());
//                    messageView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {

//                        }
//                    });
                    ArrayList<String> images = new ArrayList<>();
                    for (MessageModel i : messageModel) {
                        list.add(new CarouselItem(i.getBody(), ""));
                        images.add(i.getBody());
                    }
                    carousel.addData(list);

                    carousel.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onClick(int i, CarouselItem carouselItem) {
                            Log.d("PositionImage", i + "");
                            new StfalconImageViewer.Builder<>(getContext(), images, new ImageLoader<String>() {
                                @Override
                                public void loadImage(ImageView imageView, String image) {
                                    Picasso.get().load(image).into(imageView);
                                }
                            }).withStartPosition(i).show();
                        }

                        @Override
                        public void onLongClick(int i, CarouselItem carouselItem) {

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
