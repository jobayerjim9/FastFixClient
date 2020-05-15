package com.servicing.jobaer.fastfixclient.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.servicing.jobaer.fastfixclient.R;
import com.servicing.jobaer.fastfixclient.controller.retrofit.ApiClient;
import com.servicing.jobaer.fastfixclient.controller.retrofit.ApiInterface;
import com.servicing.jobaer.fastfixclient.model.AppData;
import com.servicing.jobaer.fastfixclient.model.ImageModel;
import com.servicing.jobaer.fastfixclient.model.RequestGenaratedModel;
import com.servicing.jobaer.fastfixclient.model.requests.CreateRequestBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRequestMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ArrayList<String> images;
    Marker marker=null;
    private FusedLocationProviderClient mFusedLocationClient;
    private double lon=0,lat=0;
    private GoogleMap mMap;
    String name,mobile,description,service_type;
    private boolean locationSelected=false;
    private ArrayList<ImageModel> imageModels=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request_map);
        initUi();
    }

    private void initUi() {
        Places.initialize(getApplicationContext(), getString(R.string.google_api));
        PlacesClient placesClient = Places.createClient(this);
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

// Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS,Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("placeAutoComplete", "Place: " + place.getName() + ", " + place.getId()+","+place.getLatLng());
                lat=place.getLatLng().latitude;
                lon=place.getLatLng().longitude;
                locationSelected=true;
                LatLng currentLocation = new LatLng(lat, lon);
                addMarker(currentLocation);

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("placeAutoComplete", "An error occurred: " + status);
            }
        });
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
                if (location != null && !locationSelected) {
                    Log.d("myLocation",location.getLatitude()+" "+location.getLongitude());
                    lat=location.getLatitude();
                    lon=location.getLongitude();
                    if (mMap!=null) {
                        LatLng currentLocation = new LatLng(lat, lon);
                        addMarker(currentLocation);
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
                    Toast.makeText(CreateRequestMapActivity.this, getString(R.string.waiting_for_location), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createToDatabase() {
        String add=getAddressFromLatLon(lat,lon);
        if (add!=null) {
            ProgressDialog progressDialog = new ProgressDialog(CreateRequestMapActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Creating Request....");
            progressDialog.show();
            CreateRequestBody createRequestBody = new CreateRequestBody(name, mobile, description, lat, lon, add, "Open", service_type, imageModels);
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<RequestGenaratedModel> call = apiInterface.createRequest(createRequestBody);
            call.enqueue(new Callback<RequestGenaratedModel>() {
                @Override
                public void onResponse(Call<RequestGenaratedModel> call, Response<RequestGenaratedModel> response) {
                    RequestGenaratedModel temp = response.body();
                    if (temp != null) {
                        progressDialog.dismiss();
                        Intent intent = new Intent(CreateRequestMapActivity.this, RequestNumberActivity.class);
                        intent.putExtra("code", temp.getCode());
                        Toast.makeText(CreateRequestMapActivity.this, getString(R.string.request_placed), Toast.LENGTH_SHORT).show();

                        startActivity(intent);
                        finish();
                        Log.d("genaratedCode", temp.getCode());


                    }
                }

                @Override
                public void onFailure(Call<RequestGenaratedModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(CreateRequestMapActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(this,getString(R.string.unable_to_get_address), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                    if (location != null && !locationSelected) {
                        Log.d("myLocation",location.getLatitude()+" "+location.getLongitude());
                        lat=location.getLatitude();
                        lon=location.getLongitude();
                        if (mMap!=null) {
                            LatLng currentLocation = new LatLng(lat, lon);
                            addMarker(currentLocation);

                        }
                    }
                });
            } else {
                Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(15.0f);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                lat=latLng.latitude;
                lon=latLng.longitude;
                locationSelected=true;
                addMarker(latLng);
            }
        });
        // Add a marker in Sydney, Australia, and move the camera.
    }

    private String getAddressFromLatLon(double latitude,double longitude){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(CreateRequestMapActivity.this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            return address +" "+city+" "+state+" "+country;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void addMarker(LatLng current) {
        if (marker!=null) {
            marker.remove();
        }
            marker=mMap.addMarker(new MarkerOptions().position(current).title(getString(R.string.slected_location)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(current));

    }
}
