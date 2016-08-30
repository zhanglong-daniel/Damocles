package com.damocles.navi;

import android.os.Environment;

/**
 * Created by zhanglong02 on 16/8/24.
 */
final class NaviUtils {

    final static String LOG_TAG = "navi";

    static String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }
}
