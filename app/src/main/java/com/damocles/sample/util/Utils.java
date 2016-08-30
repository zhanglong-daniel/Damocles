package com.damocles.sample.util;

import com.damocles.R;
import com.damocles.android.util.DeviceInfoUtils;

import android.app.Activity;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

/**
 * Created by zhanglong02 on 16/3/3.
 */
public class Utils {

    public static Toolbar initToolbar(Activity activity, int id) {
        Toolbar toolbar = (Toolbar) activity.findViewById(id);
        int statusBarHeight = DeviceInfoUtils.getStatusBarHeight();
        int w = LinearLayout.LayoutParams.MATCH_PARENT;
        int h = activity.getResources().getDimensionPixelSize(R.dimen.tool_bar_height);
        if (Build.VERSION.SDK_INT > 19) {
            toolbar.setPadding(0, statusBarHeight, 0, 0);
            h += statusBarHeight;
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, h);
        toolbar.setLayoutParams(params);
        if (activity instanceof AppCompatActivity) {
            ((AppCompatActivity) activity).setSupportActionBar(toolbar);
        }
        return toolbar;
    }

}
