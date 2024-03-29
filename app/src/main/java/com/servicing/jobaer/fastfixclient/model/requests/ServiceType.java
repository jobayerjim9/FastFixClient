package com.servicing.jobaer.fastfixclient.model.requests;

import com.google.gson.annotations.SerializedName;

public class ServiceType {
    @SerializedName("id")
    private String id;
    @SerializedName("category")
    private String category;

    public ServiceType() {
    }

    public ServiceType(String id, String category) {
        this.id = id;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
