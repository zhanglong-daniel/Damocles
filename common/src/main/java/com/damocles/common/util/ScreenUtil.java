package com.damocles.common.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by zhanglong02 on 17/2/17.
 */

public class ScreenUtil {

    private static int screenWidth = 0;
    private static int screenHeight = 0;
    private static float density = 0.0f;

    public static int getScreenWidth(Context context) {
        if (screenWidth == 0) {
            screenWidth = getDisplayMetrics(context).widthPixels;
        }
        return screenWidth;
    }

    public static int getScreenHeight(Context context) {
        if (screenHeight == 0) {
            screenHeight = getDisplayMetrics(context).heightPixels;
        }
        return screenHeight;
    }

    public static float getDensity(Context context) {
        if (density == 0.0f) {
            density = getDisplayMetrics(context).density;
        }
        return density;
    }

    public static int dp2px(Context context, float dpValue) {
        return (int) (getDensity(context) * dpValue + 0.5f);
    }

    private static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }
}
