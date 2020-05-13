package com.servicing.jobaer.fastfixclient.model;

import java.util.ArrayList;

public class AppData {
    private static ArrayList<String> imagesBase64;

    public static ArrayList<String> getImagesBase64() {
        return imagesBase64;
    }

    public static void setImagesBase64(ArrayList<String> imagesBase64) {
        AppData.imagesBase64 = imagesBase64;
    }
}
