package com.damocles.common.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zhanglong02 on 17/2/15.
 */

public class ToastUtil {

    private static Toast sToast;

    public static void show(Context context, CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void show(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 自动覆盖之前的toast
     */
    public static void showCoverLast(Context context, CharSequence text) {
        if (sToast == null) {
            sToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(text);
            sToast.setDuration(Toast.LENGTH_SHORT);
        }
        sToast.show();
    }

    public static void showCoverLast(Context context, int resId) {
        if (sToast == null) {
            sToast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(resId);
            sToast.setDuration(Toast.LENGTH_SHORT);
        }
        sToast.show();
    }

}
