package com.servicing.jobaer.fastfixclient.model.requests;

import com.google.gson.annotations.SerializedName;

public class CloseRequestBody {

    @SerializedName("status")
    private String status;

    @SerializedName("invoice")
    private String invoice;

    public CloseRequestBody() {

    }

    public CloseRequestBody(String status, String invoice) {
        this.status = status;
        this.invoice = invoice;
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
}
