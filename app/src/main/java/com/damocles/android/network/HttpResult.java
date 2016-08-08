package com.damocles.android.network;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.apache.http.HttpStatus;

public class HttpResult {

    private int statusCode;
    private String response;
    private Map<String, String> cookies;

    public HttpResult(int statusCode, Map<String, String> cookies, String response) {
        this.statusCode = statusCode;
        this.cookies = cookies;
        this.response = response;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponse() {
        return response;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public boolean isOk() {
        return this.statusCode == HttpStatus.SC_OK && this.response != null;
    }

    @Override
    public String toString() {
        return "{statusCode=" + statusCode + ";response=" + response + "cookies = [" + cookies + "] }";
    }

}
