package com.damocles.android.network;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

/**
 * HttpRequestHelper
 *
 * @author danielzhang
 */
public class HttpRequestHelper {

    private final static String TAG = HttpRequestHelper.class.getSimpleName();

    public static HttpResult get(String url, String cookie)
            throws IOException {
        // httpGet
        HttpGet get = new HttpGet(url);
        Log.e(TAG, "get:\n" + url);
        // accept-encoding : gzip
        get.addHeader("Accept-Encoding", "gzip");
        // user-agent
        get.addHeader(HTTP.USER_AGENT, NetworkUtils.DEFAULT_USER_AGENT);
        // cookies
        if (!TextUtils.isEmpty(cookie)) {
            get.addHeader("Cookie", cookie);
        }
        // response
        HttpResponse response = HttpWrapper.getHttpClient().execute(get);
        return handleResult(response);
    }

    public static HttpResult post(String url, HttpEntityParams params,
                                  String cookie) throws ParseException, IOException {
        HttpEntity entity = null;
        if (params != null) {
            entity = new UrlEncodedFormEntity(
                    params.getHttpEntityParams(), HTTP.UTF_8);
            Log.e(TAG, "post:\n" + url + "?" + params.toString());
        } else {
            Log.e(TAG, "post:\n" + url);
        }
        return post(url, entity, cookie);
    }

    public static HttpResult post(String url, HttpEntity entity,
                                  String cookie) throws ParseException, IOException {
        // httpPost
        HttpPost post = new HttpPost(url);
        // accept-encoding : gzip
        post.addHeader("Accept-Encoding", "gzip");
        post.addHeader(HTTP.CONTENT_TYPE, "application/json; charset=UTF-8");
        // user-agent
        post.addHeader(HTTP.USER_AGENT, NetworkUtils.DEFAULT_USER_AGENT);
        // cookies
        post.addHeader("Cookie", cookie);
        // post entity
        if (entity != null) {
            post.setEntity(entity);
        }
        Log.e(TAG, "post:\n" + url);
        // response
        HttpResponse response = HttpWrapper.getHttpClient().execute(post);
        return handleResult(response);
    }

    public static ImageHttpResult getImage(String url, String userAgent)
            throws IOException {
        // httpGet
        HttpGet get = new HttpGet(url);
        Log.e(TAG, "get:\n" + url);
        // user-agent
        if (!TextUtils.isEmpty(userAgent)) {
            get.addHeader(HTTP.USER_AGENT, userAgent);
            Log.e(TAG, "User-Agent : " + userAgent);
        }
        // response
        HttpResponse response = HttpWrapper.getHttpClient().execute(get);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200 || !response.getFirstHeader(HTTP.CONTENT_TYPE).getValue().contains("image")) {
            return new ImageHttpResult(statusCode, null);
        }
        InputStream inputStream = null;
        try {

            inputStream = response.getEntity().getContent();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return new ImageHttpResult(statusCode, bitmap);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    private static HttpResult handleResult(HttpResponse response)
            throws ParseException, IOException {
        int statusCode = response.getStatusLine().getStatusCode();
        Map<String, String> cookies = getCookies(response);
        String content = convertResponseToString(response);
        Log.e(TAG, "handleResult: \n" + content);
        return new HttpResult(statusCode, cookies, content);
    }

    private static String convertResponseToString(HttpResponse response)
            throws ParseException, IOException {
        Header header = response.getFirstHeader(HTTP.CONTENT_ENCODING);
        if (header != null && header.getValue().equalsIgnoreCase("gzip")) { // gzip
            GzipDecompressingEntity entity = new GzipDecompressingEntity(
                    response.getEntity());
            return EntityUtils.toString(entity, HTTP.UTF_8);
        }
        Log.e(TAG, "not gzip !");
        return EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
    }

    private static Map<String, String> getCookies(HttpResponse response) {
        Map<String, String> cookieMap = new HashMap<String, String>();
        try {

            Header[] headers = response.getHeaders("Set-Cookie");
            for (int i = 0; i < headers.length; i++) {
                String cookie = headers[i].getValue().split(";")[0].trim();
                String[] cookies = cookie.split("=");
                cookieMap.put(cookies[0], cookies[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cookieMap;
    }

}
