package com.damocles.android.network;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.impl.cookie.CookieSpecBase;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

/**
 * HttpWrapper
 *
 * @author danielzhang
 */
final class HttpWrapper {

    private HttpWrapper() {

    }

    private static class HttpCfg {

        public static final int MAX_TOTAL_CONNECTIONS = 200;

        public static final int MAX_CONNECTIONS_PER_ROUTE = 20;

        public static final int WAIT_TIMEOUT = 10 * 1000;

        public static final int CONNECTION_TIMEOUT = 30 * 1000;

        public static final int SO_TIMEOUT = 30 * 1000;
    }

    public static HttpClient getHttpClient() {
        return HttpHelperHolder.INSTANCE;
    }

    private static class HttpHelperHolder {

        public static final HttpClient INSTANCE = buildHttpClient();

        private static HttpClient buildHttpClient() {
            HttpParams httpParams = new BasicHttpParams();
            HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
            // 最大连接数
            ConnManagerParams.setMaxTotalConnections(httpParams,
                    HttpCfg.MAX_TOTAL_CONNECTIONS);
            ConnPerRoute connPerRoute = new ConnPerRouteBean(
                    HttpCfg.MAX_CONNECTIONS_PER_ROUTE);
            // 超时
            ConnManagerParams.setMaxConnectionsPerRoute(httpParams,
                    connPerRoute);
            ConnManagerParams.setTimeout(httpParams, HttpCfg.WAIT_TIMEOUT);
            HttpConnectionParams.setConnectionTimeout(httpParams,
                    HttpCfg.CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, HttpCfg.SO_TIMEOUT);
            // 支持http和https两种协议
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            registry.register(new Scheme("https", SSLSocketFactory
                    .getSocketFactory(), 443));
            // 使用线程安全的连接管理来创建HttpClient
            ClientConnectionManager cm = new ThreadSafeClientConnManager(
                    httpParams, registry);
            HttpClient httpClient = new DefaultHttpClient(cm, httpParams);
            return httpClient;
        }
    }

    public static HttpURLConnection getHttpURLConnection(String spec)
            throws IOException {
        URL url = new URL(spec);
        return (HttpURLConnection) url.openConnection();
    }

    public static Header getCookieHeader(String key, String value) {
        List<Cookie> cookies = new ArrayList<Cookie>();
        Cookie cookie = new BasicClientCookie(key, value);
        cookies.add(cookie);
        CookieSpecBase cookieSpecBase = new BrowserCompatSpec();
        return cookieSpecBase.formatCookies(cookies).get(0);
    }

}
