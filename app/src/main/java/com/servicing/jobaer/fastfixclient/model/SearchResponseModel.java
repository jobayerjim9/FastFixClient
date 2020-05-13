package com.servicing.jobaer.fastfixclient.model;

import com.google.gson.annotations.SerializedName;

import com.servicing.jobaer.fastfixclient.model.requests.RequestModel;

import java.util.ArrayList;

public class SearchResponseModel {
    @SerializedName("status")
    private String status;

    @SerializedName("Message")
    private String Message;

    @SerializedName("data")
    private ArrayList<RequestModel> data;


    public SearchResponseModel() {
    }

    public SearchResponseModel(String status, String message, ArrayList<RequestModel> data) {
        this.status = status;
        Message = message;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public ArrayList<RequestModel> getData() {
        return data;
    }

    public void setData(ArrayList<RequestModel> data) {
        this.data = data;
    }
}
