package com.servicing.jobaer.fastfixclient.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    Uri uri;
    private ImageView image1,image2,image3,image4;
    private ArrayList<String> imagesBase64=new ArrayList<>(4);
    private EditText name,phoneNumber,descriptionText;
    final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

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
                if (ContextCompat.checkSelfPermission(CreateRequestActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(CreateRequestActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(CreateRequestActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    ActivityCompat.requestPermissions(CreateRequestActivity.this, permissions, 1);
                } else {
                    //pickImage(1);
                    selectImage(1);
                }
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(CreateRequestActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(CreateRequestActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(CreateRequestActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    ActivityCompat.requestPermissions(CreateRequestActivity.this, permissions, 1);
                } else {
                    // pickImage(2);
                    selectImage(2);

                }
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(CreateRequestActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(CreateRequestActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(CreateRequestActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    ActivityCompat.requestPermissions(CreateRequestActivity.this, permissions, 1);
                } else {
                    //pickImage(3);
                    selectImage(3);
                }
            }
        });
        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(CreateRequestActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(CreateRequestActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(CreateRequestActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    ActivityCompat.requestPermissions(CreateRequestActivity.this, permissions, 1);
                } else {
                    //pickImage(4);
                    selectImage(4);
                }
            }
        });
        getSpinnerItems();

    }

    private void validateData() {
        String nameText=name.getText().toString();
        String mobile=phoneNumber.getText().toString();
        String description=descriptionText.getText().toString();
        if (nameText.isEmpty())
        {
            Toast.makeText(this, getString(R.string.enter_name), Toast.LENGTH_SHORT).show();
        }
        else if (mobile.isEmpty()) {
            Toast.makeText(this,  getString(R.string.enter_mobile), Toast.LENGTH_SHORT).show();
        }
        else if (description.isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_desc), Toast.LENGTH_SHORT).show();
        }
        else if (selectedSpinner==-1) {
            Toast.makeText(this, getString(R.string.select_category), Toast.LENGTH_SHORT).show();
        }
        else {
            String spinerItem = servicesModels.get(selectedSpinner).getId();
            Intent intent = new Intent(CreateRequestActivity.this, CreateRequestMapActivity.class);
            intent.putExtra("name", nameText);
            intent.putExtra("mobile", mobile);
            intent.putExtra("description", description);
            intent.putExtra("service_type", spinerItem);
            AppData.setImagesBase64(imagesBase64);
            //intent.putStringArrayListExtra("images",imagesBase64);
            startActivityForResult(intent, 10);
            // finish();


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

    private void selectImage(int req) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(CreateRequestActivity.this);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, req);
                    }

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, req);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    public void pickImage(int requestCode) {

        Log.d("CodePicked",requestCode+"");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] {"image/*", "video/*"});
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        } else if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }


            Uri uri = data.getData();

            String fileType = getMimeType(uri);
            if (uri == null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                uri = getImageUri(CreateRequestActivity.this, imageBitmap);

            }
            Log.d("imageUri", uri + "");
            String[] splitedType = fileType.split("/", 2);
            //Log.d("SplitedType",splitedType[0]+ " "+splitedType[1]);
            Log.d("splittedType", splitedType + "");
            if (fileType.contains("image"))
            {
                try {
                    Cursor returnCursor =
                            getContentResolver().query(uri, null, null, null, null);
                    int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                    returnCursor.moveToFirst();
                    long sizeOfVideo = returnCursor.getLong(sizeIndex);
                    int sizeInMb = (int) sizeOfVideo / 1000000;
                    Log.d("imageSize", sizeInMb + "");
                    if (sizeInMb < 3) {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        if (requestCode == 1) {
                            if (splitedType.length == 1) {
                                image1.setImageBitmap(bitmap);
                                storeImage(uri, 0, "jpg");
                                image2.setVisibility(View.VISIBLE);
                            } else {
                                image1.setImageBitmap(bitmap);
                                storeImage(uri, 0, splitedType[1]);
                                image2.setVisibility(View.VISIBLE);
                            }

                        } else if (requestCode == 2) {
                            if (splitedType.length == 1) {
                                image2.setImageBitmap(bitmap);
                                storeImage(uri, 1, "jpg");
                                image3.setVisibility(View.VISIBLE);
                            } else {
                                image2.setImageBitmap(bitmap);
                                storeImage(uri, 1, splitedType[1]);
                                image3.setVisibility(View.VISIBLE);
                            }

                        } else if (requestCode == 3) {
                            if (splitedType.length == 1) {
                                image3.setImageBitmap(bitmap);
                                storeImage(uri, 2, "jpg");
                                image4.setVisibility(View.VISIBLE);
                            } else {
                                image3.setImageBitmap(bitmap);
                                storeImage(uri, 2, splitedType[1]);
                                image4.setVisibility(View.VISIBLE);
                            }

                        } else if (requestCode == 4) {
                            if (splitedType.length == 1) {
                                image4.setImageBitmap(bitmap);
                                storeImage(uri, 3, "jpg");
                            } else {
                                image4.setImageBitmap(bitmap);
                                storeImage(uri, 3, splitedType[1]);
                            }

                        }
                    } else {
                        Toast.makeText(this, "Upload Image That Lower Than 2 mb", Toast.LENGTH_SHORT).show();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (fileType.contains("video")){
                if (requestCode==1) {
                    image1.setImageDrawable(getDrawable(R.drawable.ic_videocam_black_24dp));
                    storeVideo(uri,0,splitedType[1]);
                }
                else if (requestCode==2) {
                    image2.setImageDrawable(getDrawable(R.drawable.ic_videocam_black_24dp));
                    storeVideo(uri,1,splitedType[1]);
                }
                else if (requestCode==3) {
                    image3.setImageDrawable(getDrawable(R.drawable.ic_videocam_black_24dp));
                    storeVideo(uri,2,splitedType[1]);
                }
                else if (requestCode==4) {
                    image4.setImageDrawable(getDrawable(R.drawable.ic_videocam_black_24dp));
                    storeVideo(uri,3,splitedType[1]);
                }
            }


        }


    }

    private void storeVideo(Uri uri, int i, String s) {
        Cursor returnCursor =
                getContentResolver().query(uri, null, null, null, null);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        long sizeOfVideo=returnCursor.getLong(sizeIndex);
        int sizeInMb=(int) sizeOfVideo/1000000;
        Log.d("videoSize",sizeInMb+"MB");
        if (sizeInMb<5) {

            String encodedVideo="data:image/"+s+";base64,"+encodeVideo(uri);
            imagesBase64.add(i,encodedVideo);
        }
        else {
            Toast.makeText(this,getString( R.string.max_3mb), Toast.LENGTH_SHORT).show();
        }

    }

    private String encodeVideo(Uri uri) {
        //Uri selectedVideoUri =uri;
       // String[] projection = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION};
        //Cursor cursor = getContentResolver().query(selectedVideoUri, projection, null, null, null);

        //cursor.moveToFirst();
        //String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
//        Log.d("File Name:",filePath);

//        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Video.Thumbnails.MINI_KIND);
        // Setting the thumbnail of the video in to the image view
       // msImage.setImageBitmap(thumb);
        InputStream inputStream = null;
// Converting the video in to the bytes
        try
        {
            inputStream = getContentResolver().openInputStream(uri);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int len = 0;
        try
        {
            while ((len = inputStream.read(buffer)) != -1)
            {
                byteBuffer.write(buffer, 0, len);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        System.out.println("converted!");

        String videoData="";
        //Converting bytes into base64
        videoData = Base64.encodeToString(byteBuffer.toByteArray(), Base64.DEFAULT);
        Log.d("VideoData**>  " , videoData);

       return videoData;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permission", "Granted");
                selectImage(1);

            } else {
                Toast.makeText(this, getString(R.string.storage_permision), Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(CreateRequestActivity.this, this.permissions, 1);
            }
        }

    }
    private void storeImage(Uri uri,int index,String type) {
        try {
            // get uri from Intent
            // get bitmap from uri

            final InputStream imageStream = getContentResolver().openInputStream(uri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            String encodedImage = null;
            if (type.equals("jpeg") || type.equals("jpg")) {
                encodedImage = "data:image/jpeg;base64," + encodeJpgImage(selectedImage);
            } else if (type.equals("png")) {
                encodedImage = "data:image/png;base64," + encodepngImage(selectedImage);
            }
            if (encodedImage != null) {
                try {
                    imagesBase64.add(index, encodedImage);
                } catch (ArrayIndexOutOfBoundsException e) {
                    Toast.makeText(this, "Remove The Previous Image Because It's Larger Than 2 MB", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String encodeJpgImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }
    private String encodepngImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }
    public String getMimeType(Uri uri) {
        String mimeType = null;
        try {


            if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                ContentResolver cr = getContentResolver();
                mimeType = cr.getType(uri);
            } else {
                String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                        .toString());
                mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                        fileExtension.toLowerCase());
            }
            return mimeType;
        } catch (Exception e) {
            return "image";
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
