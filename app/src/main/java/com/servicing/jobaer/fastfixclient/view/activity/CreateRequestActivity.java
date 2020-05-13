package com.servicing.jobaer.fastfixclient.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.servicing.jobaer.fastfixclient.R;
import com.servicing.jobaer.fastfixclient.controller.retrofit.ApiClient;
import com.servicing.jobaer.fastfixclient.controller.retrofit.ApiInterface;
import com.servicing.jobaer.fastfixclient.model.AppData;
import com.servicing.jobaer.fastfixclient.model.ServicesModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRequestActivity extends AppCompatActivity {
    Spinner spinner;
    private ArrayList<ServicesModel> servicesModels=new ArrayList<>();
    private int selectedSpinner=-1;
    private ImageView image1,image2,image3,image4;
    private ArrayList<String> imagesBase64=new ArrayList<>(4);
    private EditText name,phoneNumber,descriptionText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);

        initUi();
    }

    private void initUi() {
        spinner=findViewById(R.id.categorySpinner);
        name=findViewById(R.id.name);
        phoneNumber=findViewById(R.id.phoneNumber);
        descriptionText=findViewById(R.id.descriptionText);
        image1=findViewById(R.id.image1);
        image2=findViewById(R.id.image2);
        image3=findViewById(R.id.image3);
        image4=findViewById(R.id.image4);
        Button nextButton=findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(CreateRequestActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    ActivityCompat.requestPermissions(CreateRequestActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    pickImage(1);
                }
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(CreateRequestActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    ActivityCompat.requestPermissions(CreateRequestActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    pickImage(2);
                }
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(CreateRequestActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    ActivityCompat.requestPermissions(CreateRequestActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    pickImage(3);
                }
            }
        });
        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(CreateRequestActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CreateRequestActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    pickImage(4);
                }
            }
        });
        getSpinnerItems();

    }

    private void validateData() {
        String nameText=name.getText().toString();
        String mobile=phoneNumber.getText().toString();
        String description=descriptionText.getText().toString();
        if (nameText.isEmpty() && mobile.isEmpty() && description.isEmpty() && selectedSpinner==-1)
        {
            Toast.makeText(this, "Please Fill All Required Info", Toast.LENGTH_SHORT).show();
        }
        else {
            String spinerItem=servicesModels.get(selectedSpinner).getId();
            Intent intent=new Intent(CreateRequestActivity.this,CreateRequestMapActivity.class);
            intent.putExtra("name",nameText);
            intent.putExtra("mobile",mobile);
            intent.putExtra("description",description);
            intent.putExtra("service_type",spinerItem);
            AppData.setImagesBase64(imagesBase64);
            //intent.putStringArrayListExtra("images",imagesBase64);
            startActivity(intent);
            finish();



        }

        
    
        
    }

    private void getSpinnerItems() {
        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<ServicesModel>> call=apiInterface.getServiceList();
        call.enqueue(new Callback<ArrayList<ServicesModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ServicesModel>> call, Response<ArrayList<ServicesModel>> response) {
                ArrayList<ServicesModel> temp=response.body();
                if (temp!=null) {
                    servicesModels.addAll(temp);
                    ArrayList<String> spinnerArray=new ArrayList<>();
                    for (ServicesModel servicesModel:servicesModels) {
                        spinnerArray.add(servicesModel.getCategory());
                    }
                    ArrayAdapter<String> adp = new ArrayAdapter<String> (CreateRequestActivity.this,android.R.layout.simple_spinner_dropdown_item,spinnerArray);
                    spinner.setAdapter(adp);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Log.d("SpinnerClicked",position+"");
                            selectedSpinner=position;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            selectedSpinner=-1;
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<ArrayList<ServicesModel>> call, Throwable t) {

            }
        });
    }

    public void pickImage(int requestCode) {

        Log.d("CodePicked",requestCode+"");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            Uri uri = data.getData();
            Log.d("requestCode",requestCode+"");
            try {
                Bitmap  bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                if (requestCode==1) {
                    image1.setImageBitmap(bitmap);
                    storeImage(uri,0);
                }
                else if (requestCode==2) {
                    image2.setImageBitmap(bitmap);
                    storeImage(uri,1);
                }
                else if (requestCode==3) {
                    image3.setImageBitmap(bitmap);
                    storeImage(uri,2);
                }
                else if (requestCode==4) {
                    image4.setImageBitmap(bitmap);
                    storeImage(uri,3);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permission", "Granted");
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
            } else {
                Toast.makeText(this, "Allow Permission To Get Image From Your Storage", Toast.LENGTH_SHORT).show();
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
        }

    }
    private void storeImage(Uri uri,int index) {
        try {
            // get uri from Intent
            // get bitmap from uri
            final InputStream imageStream = getContentResolver().openInputStream(uri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            String encodedImage = "data:image/jpg;base64," + encodeImage(selectedImage);
            imagesBase64.add(index,encodedImage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }

}
