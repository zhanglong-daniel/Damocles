package com.damocles.android.network;

import org.apache.http.HttpStatus;

import android.graphics.Bitmap;

public class ImageHttpResult {

    private int statusCode;
    private Bitmap response;

    public ImageHttpResult(int statusCode, Bitmap response) {
        this.statusCode = statusCode;
        this.response = response;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Bitmap getResponse() {
        return response;
    }

    public boolean isOk() {
        return this.statusCode == HttpStatus.SC_OK && this.response != null;
    }

    @Override
    public String toString() {
        return "{statusCode=" + statusCode + ";response=" + response + "}";
    }

}
