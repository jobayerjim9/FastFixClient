package com.servicing.jobaer.fastfixclient.controller.retrofit;

import com.servicing.jobaer.fastfixclient.model.MessageModel;
import com.servicing.jobaer.fastfixclient.model.RequestGenaratedModel;
import com.servicing.jobaer.fastfixclient.model.ResponseModel;
import com.servicing.jobaer.fastfixclient.model.SearchResponseModel;

import com.servicing.jobaer.fastfixclient.model.ServicesModel;
import com.servicing.jobaer.fastfixclient.model.requests.CloseRequestBody;
import com.servicing.jobaer.fastfixclient.model.requests.CreateRequestBody;
import com.servicing.jobaer.fastfixclient.model.requests.RequestModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("orders_open")
    Call<ArrayList<RequestModel>> getOpenRequests();

    @GET("orders")
    Call<ArrayList<RequestModel>> getAllRequests();

    @GET("detail/{id}")
    Call<RequestModel> getRequestDetail(@Path("id") String id);

    @PATCH("status/{id}")
    Call<ResponseModel> closeOrder(@Path("id") String id, @Body CloseRequestBody body);

    @GET("order")
    Call<SearchResponseModel> getSearchResult(@Query("q") String q);

    @GET("services")
    Call<ArrayList<ServicesModel>> getServiceList();

    @POST("create/")
    Call<RequestGenaratedModel> createRequest(@Body CreateRequestBody createRequestBody);

    @GET("message/")
    Call<ArrayList<MessageModel>> getMessage();

}
