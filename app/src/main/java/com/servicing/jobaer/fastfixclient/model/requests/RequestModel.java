package com.servicing.jobaer.fastfixclient.model.requests;

import com.google.gson.annotations.SerializedName;
import com.servicing.jobaer.fastfixclient.model.ImageModel;

import java.util.ArrayList;

public class RequestModel {
    @SerializedName("id")
    private String id;
    @SerializedName("service_type")
    private ServiceType service_type;
    @SerializedName("media")
    private ArrayList<ImageModel> media;
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
    @SerializedName("invoice")
    private String invoice;
    @SerializedName("created")
    private String created;
    @SerializedName("updated")
    private String updated;
    @SerializedName("code")
    private String code;

    public RequestModel() {
    }

    public RequestModel(String id, ServiceType service_type, ArrayList<ImageModel> media, String name, String phone, String desc, double lat, double lon, String addres, String status, String invoice, String created, String updated, String code) {
        this.id = id;
        this.service_type = service_type;
        this.media = media;
        this.name = name;
        this.phone = phone;
        this.desc = desc;
        this.lat = lat;
        this.lon = lon;
        this.addres = addres;
        this.status = status;
        this.invoice = invoice;
        this.created = created;
        this.updated = updated;
        this.code = code;
    }

    public ArrayList<ImageModel> getMedia() {
        return media;
    }

    public void setMedia(ArrayList<ImageModel> media) {
        this.media = media;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ServiceType getService_type() {
        return service_type;
    }

    public void setService_type(ServiceType service_type) {
        this.service_type = service_type;
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

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }
}
