package com.servicing.jobaer.fastfixclient.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.servicing.jobaer.fastfixclient.R;
import com.servicing.jobaer.fastfixclient.controller.retrofit.ApiClient;
import com.servicing.jobaer.fastfixclient.controller.retrofit.ApiInterface;
import com.servicing.jobaer.fastfixclient.model.AppData;
import com.servicing.jobaer.fastfixclient.model.ImageModel;
import com.servicing.jobaer.fastfixclient.model.RequestGenaratedModel;
import com.servicing.jobaer.fastfixclient.model.requests.CreateRequestBody;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRequestMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ArrayList<String> images;
    private FusedLocationProviderClient mFusedLocationClient;
    private double lon=0,lat=0;
    private GoogleMap mMap;
    String name,mobile,description,service_type;
    private ArrayList<ImageModel> imageModels=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request_map);
        initUi();
    }

    private void initUi() {
         name=getIntent().getStringExtra("name");
         mobile=getIntent().getStringExtra("mobile");
         description=getIntent().getStringExtra("description");
         service_type=getIntent().getStringExtra("service_type");
        images= AppData.getImagesBase64();
        for (String i: images) {
            ImageModel imageModel=new ImageModel(i);
            imageModels.add(imageModel);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        } else {
            // already permission granted
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    Log.d("myLocation",location.getLatitude()+" "+location.getLongitude());
                    lat=location.getLatitude();
                    lon=location.getLongitude();
                    if (mMap!=null) {
                        LatLng currentLocation = new LatLng(lat, lon);
                        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Your Location"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                    }
                }
            });
        }
        Button createRequest=findViewById(R.id.creaateRequest);
        createRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lat!=0 && lon!=0) {
                    createToDatabase();
                }
                else {
                    Toast.makeText(CreateRequestMapActivity.this, "Waiting For Your Current Location!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createToDatabase() {

        ProgressDialog progressDialog=new ProgressDialog(CreateRequestMapActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Creating Request....");
        progressDialog.show();
        CreateRequestBody createRequestBody=new CreateRequestBody(name,mobile,description,lat,lon," ","Open",service_type,imageModels);
        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);
        Call<RequestGenaratedModel> call=apiInterface.createRequest(createRequestBody);
        call.enqueue(new Callback<RequestGenaratedModel>() {
            @Override
            public void onResponse(Call<RequestGenaratedModel> call, Response<RequestGenaratedModel> response) {
                RequestGenaratedModel temp=response.body();
                if (temp!=null) {
                    progressDialog.dismiss();
                    Intent intent=new Intent(CreateRequestMapActivity.this,RequestNumberActivity.class);
                    intent.putExtra("code",temp.getCode());
                    startActivity(intent);
                    finish();
                    Log.d("genaratedCode",temp.getCode());

                }
            }

            @Override
            public void onFailure(Call<RequestGenaratedModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CreateRequestMapActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                    if (location != null) {
                        Log.d("myLocation",location.getLatitude()+" "+location.getLongitude());
                        lat=location.getLatitude();
                        lon=location.getLongitude();
                        if (mMap!=null) {
                            LatLng currentLocation = new LatLng(lat, lon);
                            mMap.addMarker(new MarkerOptions().position(currentLocation).title("Your Location"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));

                        }
                    }
                });
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(15.0f);
        // Add a marker in Sydney, Australia, and move the camera.
    }
}
