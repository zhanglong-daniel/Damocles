package com.damocles.common.util;

import com.damocles.common.constant.Constants;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by zhanglong02 on 16/8/1.
 */
public final class Utils {

    /**
     * 权限检测
     *
     * @param context
     * @param permName
     *
     * @return
     */
    public static boolean checkPermission(Context context, String permName) {
        boolean result = false;
        try {
            PackageManager packageManager = context.getPackageManager();
            result = packageManager.checkPermission(permName, context.getPackageName())
                    == PackageManager.PERMISSION_GRANTED;
        } catch (Throwable e) {
            Log.e(Constants.LOG_TAG, "checkPermission error", e);
        }
        return result;
    }

}
