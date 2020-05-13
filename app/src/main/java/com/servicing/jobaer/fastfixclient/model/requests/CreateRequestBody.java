package com.servicing.jobaer.fastfixclient.model.requests;

import com.google.gson.annotations.SerializedName;
import com.servicing.jobaer.fastfixclient.model.ImageModel;

import java.util.ArrayList;

public class CreateRequestBody {

    @SerializedName("name")
    private String name;
    @SerializedName("phone")
    private String phone;
    @SerializedName("desc")
    private String desc;
    @SerializedName("lat")
    private double lat;
    @SerializedName("lon")
    private double lon;
    @SerializedName("addres")
    private String addres;
    @SerializedName("status")
    private String status;
    @SerializedName("service_type")
    private String service_type;
    @SerializedName("media")
    private ArrayList<ImageModel> media;

    public CreateRequestBody(String name, String phone, String desc, double lat, double lon, String addres, String status, String service_type, ArrayList<ImageModel> media) {
        this.name = name;
        this.phone = phone;
        this.desc = desc;
        this.lat = lat;
        this.lon = lon;
        this.addres = addres;
        this.status = status;
        this.service_type = service_type;
        this.media = media;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getAddres() {
        return addres;
    }

    public void setAddres(String addres) {
        this.addres = addres;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public ArrayList<ImageModel> getMedia() {
        return media;
    }

    public void setMedia(ArrayList<ImageModel> media) {
        this.media = media;
    }
}
