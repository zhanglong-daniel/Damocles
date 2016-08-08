package com.damocles.android.util;

import java.lang.reflect.Field;

import com.damocles.android.base.DamoclesApplication;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.SparseIntArray;

/**
 * Created by zhanglong02 on 16/2/23.
 */
public final class DeviceInfoUtils {

    enum Options {
        SCREEN_WIDTH, SCREEN_HEIGHT, STATUS_BAR_HEIGHT
    }

    private static final SparseIntArray dimensArray = new SparseIntArray();

    public static int getScreenWidth() {
        int width = dimensArray.get(Options.SCREEN_WIDTH.ordinal(), 0);
        if (width == 0) {
            width = getDisplayMetrics().widthPixels;
            dimensArray.put(Options.SCREEN_WIDTH.ordinal(), width);
        }
        return width;
    }

    public static int getScreenHeight() {
        int height = dimensArray.get(Options.SCREEN_HEIGHT.ordinal(), 0);
        if (height == 0) {
            height = getDisplayMetrics().heightPixels;
            dimensArray.put(Options.SCREEN_HEIGHT.ordinal(), height);
        }
        return height;
    }

    public static int getStatusBarHeight() {
        int statusBarHeight = dimensArray.get(Options.STATUS_BAR_HEIGHT.ordinal(), 0);
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object obj = c.newInstance();
                Field field = c.getField("status_bar_height");
                int id = Integer.parseInt(field.get(obj).toString());
                statusBarHeight = getDimensionPixelSize(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            dimensArray.put(Options.STATUS_BAR_HEIGHT.ordinal(), statusBarHeight);
        }
        return statusBarHeight;
    }

    public static int getStatusBarHeightOnWindowFocusChanged(Activity activity) {
        Rect outRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        return outRect.top;
    }

    public String getModel() {
        return android.os.Build.MODEL;
    }

    public String getDeviceId() {
        TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telephonyManager.getDeviceId();
        return TextUtils.isEmpty(deviceId) ? "0" : deviceId;
    }

    public String getAndroidId() {
        return Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private static int getDimensionPixelSize(int id) {
        return getResources().getDimensionPixelSize(id);
    }

    private static DisplayMetrics getDisplayMetrics() {
        return getResources().getDisplayMetrics();
    }

    private static Resources getResources() {
        return getContext().getResources();
    }

    private static Context getContext() {
        return DamoclesApplication.getInstance();
    }
}
