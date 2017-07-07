package com.damocles.common.network.callback;

/**
 * Created by zhanglong02 on 17/6/20.
 */

public interface DownloadCallback {

    void onDownloadStart(String url, String filePath);

    void onDownloadProgress(String url, long downloadSize, long size);

    void onDownloadError(String url, String errMsg);

    void onDownloadComplete(String url, String filePath);
}
