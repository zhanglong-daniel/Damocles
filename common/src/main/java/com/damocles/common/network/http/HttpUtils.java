package com.damocles.common.network.http;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by zhanglong02 on 16/12/2.
 */
final class HttpUtils {

    private static Request.Builder requestBuilder(String url) {
        return new Request.Builder()
                .url(url)
                .addHeader("User-Agent", HttpConfig.DEFAULT_USER_AGENT);
    }

    static Request buildGetRequest(String url) {
        return requestBuilder(url).build();
    }

    static Request buildPostRequest(String url, Map<String, String> postParams) {
        // 构造post body
        if (postParams == null) {
            postParams = new HashMap<String, String>();
        }
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        Iterator<Map.Entry<String, String>> iterator = postParams.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            formBodyBuilder.add(entry.getKey(), entry.getValue());
        }
        RequestBody requestBody = formBodyBuilder.build();
        return requestBuilder(url).post(requestBody).build();
    }

    static Request buildPostRequest(String url, String postJson) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON, postJson);
        return requestBuilder(url).post(requestBody).build();
    }

    static Map<String, String> getCookies(Response response) {
        Map<String, String> cookieMap = new HashMap<String, String>();
        try {
            List<String> cookies = response.headers("Set-Cookie");
            for (int i = 0, len = cookies.size(); i < len; i++) {
                String cookie = cookies.get(i).split(";")[0].trim();
                cookieMap.put(cookie.split("=")[0], cookie.split("=")[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cookieMap;
    }

}
