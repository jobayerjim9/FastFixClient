package com.servicing.jobaer.fastfixclient.controller.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final String BASE_URL = "http://157.245.181.14:8030/";
    public static final String AUTH_TOKEN = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJ1c2VybmFtZSI6ImJpbGFsam1hbCIsImV4cCI6MTYxMTY4MDIyMywiZW1haWwiOiJiaWxhbGptYWxAZ21haWwuY29tIiwib3JpZ19pYXQiOjE1ODkyMTYyMjN9.pf_lo72kbPDCtVEXRdnXQTGV4Hv1yweUqm32x323nng";
    private static Retrofit retrofit = null;


//    public static String getAuthCredential() {
//        String base = USER
//        return "basic "+
//    }

    public static Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .readTimeout(160, TimeUnit.SECONDS)
                .writeTimeout(160, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        Request.Builder builder = originalRequest.newBuilder().header("Authorization",
                                AUTH_TOKEN);

                        Request newRequest = builder.build();
                        return chain.proceed(newRequest);
                    }
                }).addInterceptor(interceptor).build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
