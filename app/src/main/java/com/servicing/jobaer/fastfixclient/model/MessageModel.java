package com.servicing.jobaer.fastfixclient.model;

import com.google.gson.annotations.SerializedName;

public class MessageModel {
    @SerializedName("body")
    private String body;

    public MessageModel() {
    }

    public MessageModel(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
