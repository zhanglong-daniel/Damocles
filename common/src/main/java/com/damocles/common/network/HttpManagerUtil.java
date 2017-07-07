package com.damocles.common.network;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.damocles.common.log.Log;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by zhanglong02 on 16/12/2.
 */
final class HttpManagerUtil {

    private static final String TAG = "network";

    private static Request.Builder requestBuilder(String url) {
        return new Request.Builder()
                .url(url)
                .addHeader("User-Agent", HttpConfig.DEFAULT_USER_AGENT);
    }

    static Request buildGetRequest(String url) {
        Log.i(TAG, "GET URL: " + url);
        return requestBuilder(url).build();
    }

    static Request buildPostRequest(String url, String postJson) {
        Log.i(TAG, "POST URL: " + url);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON, postJson);
        return requestBuilder(url).post(requestBody).build();
    }

    static Request buildPostRequest(String url, Map<String, String> params) {
        Log.i(TAG, "POST URL: " + url);
        // 构造post body
        if (params == null) {
            params = new HashMap<String, String>();
        }
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            Log.i(TAG, "POST PARAMS: " + entry.getKey() + "=" + entry.getValue());
            formBodyBuilder.add(entry.getKey(), entry.getValue());
        }
        RequestBody requestBody = formBodyBuilder.build();

        return requestBuilder(url).post(requestBody).build();
    }

}
