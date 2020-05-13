package com.servicing.jobaer.fastfixclient.model;

import com.google.gson.annotations.SerializedName;

public class ImageModel {
    @SerializedName("pic")
    private String pic;

    public ImageModel() {
    }

    public ImageModel(String pic) {
        this.pic = pic;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
