package com.damocles.common.network;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * cookie管理（根据host保存cookie）
 * Created by zhanglong02 on 16/12/2.
 */
final class CookieJarImpl implements CookieJar {

    CookieJarImpl() {

    }

    private final ConcurrentHashMap<String, List<Cookie>> mCookieStore = new ConcurrentHashMap<String, List<Cookie>>();

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        mCookieStore.put(url.host(), cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = mCookieStore.get(url.host());
        return cookies != null ? cookies : new ArrayList<Cookie>();
    }

    public void clear() {
        mCookieStore.clear();
    }
}
