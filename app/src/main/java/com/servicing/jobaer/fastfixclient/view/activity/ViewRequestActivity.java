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
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.servicing.jobaer.fastfixclient.R;
import com.servicing.jobaer.fastfixclient.controller.retrofit.ApiClient;
import com.servicing.jobaer.fastfixclient.controller.retrofit.ApiInterface;
import com.servicing.jobaer.fastfixclient.model.ResponseModel;
import com.servicing.jobaer.fastfixclient.model.requests.CloseRequestBody;
import com.servicing.jobaer.fastfixclient.model.requests.RequestModel;
import com.squareup.picasso.Picasso;
import com.stfalcon.imageviewer.StfalconImageViewer;
import com.stfalcon.imageviewer.loader.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewRequestActivity extends AppCompatActivity {
    TextView phoneNumber, name, addressView, status, codeView, dateView, categoryName, descriptionText;
    LinearLayout imageLayout;
    ImageView image1, image2, image3, image4;
    Button closeButton;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request);
        initUi();
    }
    private void initUi() {
        phoneNumber = findViewById(R.id.phoneNumber);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        image4 = findViewById(R.id.image4);
        image1 = findViewById(R.id.image1);
        imageLayout = findViewById(R.id.imageLayout);
        name = findViewById(R.id.name);
        addressView = findViewById(R.id.addressView);
        status = findViewById(R.id.status);
        codeView = findViewById(R.id.codeView);
        dateView = findViewById(R.id.dateView);
        closeButton = findViewById(R.id.closeButton);
        categoryName = findViewById(R.id.categoryName);
        descriptionText = findViewById(R.id.descriptionText);
        id = getIntent().getStringExtra("id");
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseRequestBody closeRequestBod = new CloseRequestBody("Closed", null);
                closeRequest(id, closeRequestBod);
            }
        });
        getRequestDetails(id);
    }

    private void closeRequest(String id, CloseRequestBody closeRequestBody) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseModel> call = apiInterface.closeOrder(id, closeRequestBody);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                ResponseModel responseModel = response.body();
                if (responseModel != null) {
                    Log.d("closingStatus", responseModel.getStatus());
                    if (responseModel.getStatus().equals("Closed")) {
                        Toast.makeText(ViewRequestActivity.this, "Request Closed Successfully", Toast.LENGTH_SHORT).show();
                        status.setText(responseModel.getStatus());

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(ViewRequestActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getRequestDetails(String id) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<RequestModel> call = apiInterface.getRequestDetail(id);
        call.enqueue(new Callback<RequestModel>() {
            @Override
            public void onResponse(Call<RequestModel> call, Response<RequestModel> response) {
                RequestModel requestModel = response.body();
                if (requestModel != null) {
                    name.setText(requestModel.getName());
                    phoneNumber.setText(requestModel.getPhone());
                    addressView.setText(requestModel.getAddres());
                    status.setText(requestModel.getStatus());
                    codeView.setText(requestModel.getCode());
                    String placeHolder = "Date: " + requestModel.getCreated();
                    if (requestModel.getMedia().size() == 1) {
                        ArrayList<Uri> images = new ArrayList<>();
                        images.add(Uri.parse(requestModel.getMedia().get(0).getPic()));
                        Picasso.get().load(requestModel.getMedia().get(0).getPic()).into(image1);
                        image1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new StfalconImageViewer.Builder<>(ViewRequestActivity.this, images, new ImageLoader<Uri>() {
                                    @Override
                                    public void loadImage(ImageView imageView, Uri image) {
                                        Picasso.get().load(image).into(imageView);
                                    }
                                }).withStartPosition(0).show();
                            }
                        });
                        image2.setVisibility(View.GONE);
                        image3.setVisibility(View.GONE);
                        image4.setVisibility(View.GONE);
                    } else if (requestModel.getMedia().size() == 2) {
                        ArrayList<Uri> images = new ArrayList<>();
                        images.add(Uri.parse(requestModel.getMedia().get(0).getPic()));
                        images.add(Uri.parse(requestModel.getMedia().get(1).getPic()));
                        Picasso.get().load(requestModel.getMedia().get(0).getPic()).into(image1);
                        Picasso.get().load(requestModel.getMedia().get(1).getPic()).into(image2);
                        image1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new StfalconImageViewer.Builder<>(ViewRequestActivity.this, images, new ImageLoader<Uri>() {
                                    @Override
                                    public void loadImage(ImageView imageView, Uri image) {
                                        Picasso.get().load(image).into(imageView);
                                    }
                                }).withStartPosition(0).show();
                            }
                        });
                        image2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new StfalconImageViewer.Builder<>(ViewRequestActivity.this, images, new ImageLoader<Uri>() {
                                    @Override
                                    public void loadImage(ImageView imageView, Uri image) {
                                        Picasso.get().load(image).into(imageView);
                                    }
                                }).withStartPosition(1).show();
                            }
                        });
                        image3.setVisibility(View.GONE);
                        image4.setVisibility(View.GONE);
                    } else if (requestModel.getMedia().size() == 3) {
                        ArrayList<Uri> images = new ArrayList<>();
                        images.add(Uri.parse(requestModel.getMedia().get(0).getPic()));
                        images.add(Uri.parse(requestModel.getMedia().get(1).getPic()));
                        images.add(Uri.parse(requestModel.getMedia().get(2).getPic()));
                        Picasso.get().load(requestModel.getMedia().get(0).getPic()).into(image1);
                        Picasso.get().load(requestModel.getMedia().get(1).getPic()).into(image2);
                        Picasso.get().load(requestModel.getMedia().get(2).getPic()).into(image3);
                        image1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new StfalconImageViewer.Builder<>(ViewRequestActivity.this, images, new ImageLoader<Uri>() {
                                    @Override
                                    public void loadImage(ImageView imageView, Uri image) {
                                        Picasso.get().load(image).into(imageView);
                                    }
                                }).withStartPosition(0).show();
                            }
                        });
                        image2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new StfalconImageViewer.Builder<>(ViewRequestActivity.this, images, new ImageLoader<Uri>() {
                                    @Override
                                    public void loadImage(ImageView imageView, Uri image) {
                                        Picasso.get().load(image).into(imageView);
                                    }
                                }).withStartPosition(1).show();
                            }
                        });

                        image3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new StfalconImageViewer.Builder<>(ViewRequestActivity.this, images, new ImageLoader<Uri>() {
                                    @Override
                                    public void loadImage(ImageView imageView, Uri image) {
                                        Picasso.get().load(image).into(imageView);
                                    }
                                }).withStartPosition(2).show();
                            }
                        });
                        image4.setVisibility(View.GONE);

                    } else if (requestModel.getMedia().size() == 4) {
                        ArrayList<Uri> images = new ArrayList<>();
                        images.add(Uri.parse(requestModel.getMedia().get(0).getPic()));
                        images.add(Uri.parse(requestModel.getMedia().get(1).getPic()));
                        images.add(Uri.parse(requestModel.getMedia().get(2).getPic()));
                        images.add(Uri.parse(requestModel.getMedia().get(3).getPic()));
                        Picasso.get().load(requestModel.getMedia().get(0).getPic()).into(image1);
                        Picasso.get().load(requestModel.getMedia().get(1).getPic()).into(image2);
                        Picasso.get().load(requestModel.getMedia().get(2).getPic()).into(image3);
                        Picasso.get().load(requestModel.getMedia().get(3).getPic()).into(image4);
                        image1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new StfalconImageViewer.Builder<>(ViewRequestActivity.this, images, new ImageLoader<Uri>() {
                                    @Override
                                    public void loadImage(ImageView imageView, Uri image) {
                                        Picasso.get().load(image).into(imageView);
                                    }
                                }).withStartPosition(0).show();
                            }
                        });
                        image2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new StfalconImageViewer.Builder<>(ViewRequestActivity.this, images, new ImageLoader<Uri>() {
                                    @Override
                                    public void loadImage(ImageView imageView, Uri image) {
                                        Picasso.get().load(image).into(imageView);
                                    }
                                }).withStartPosition(1).show();
                            }
                        });

                        image3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new StfalconImageViewer.Builder<>(ViewRequestActivity.this, images, new ImageLoader<Uri>() {
                                    @Override
                                    public void loadImage(ImageView imageView, Uri image) {
                                        Picasso.get().load(image).into(imageView);
                                    }
                                }).withStartPosition(2).show();
                            }
                        });
                        image4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new StfalconImageViewer.Builder<>(ViewRequestActivity.this, images, new ImageLoader<Uri>() {
                                    @Override
                                    public void loadImage(ImageView imageView, Uri image) {
                                        Picasso.get().load(image).into(imageView);
                                    }
                                }).withStartPosition(3).show();
                            }
                        });
                    } else if (requestModel.getMedia().size() == 0) {
                        imageLayout.setVisibility(View.GONE);
                    }

                    phoneNumber.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(Intent.ACTION_DIAL);
                            String p = "tel:" + requestModel.getPhone();
                            i.setData(Uri.parse(p));
                            startActivity(i);
                        }
                    });
                    descriptionText.setText(requestModel.getDesc());
                    categoryName.setText(requestModel.getService_type().getCategory());
                    dateView.setText(placeHolder);
                }
            }

            @Override
            public void onFailure(Call<RequestModel> call, Throwable t) {
                Toast.makeText(ViewRequestActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            Uri uri = data.getData();

            uploadImage(uri);
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        }


    }

    private void uploadImage(Uri uri) {
        try {
            // get uri from Intent
            // get bitmap from uri
            final InputStream imageStream = getContentResolver().openInputStream(uri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            String encodedImage = "data:image/jpg;base64," + encodeImage(selectedImage);
            CloseRequestBody closeRequestBody = new CloseRequestBody("", encodedImage);
            closeRequest(id, closeRequestBody);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permission", "Granted");
                pickImage();
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
            } else {
                Toast.makeText(this, "Allow Permission To Get Image From Your Storage", Toast.LENGTH_SHORT).show();
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
        }

    }
}
