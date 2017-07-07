package com.damocles.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

/**
 * Created by zhanglong02 on 16/8/1.
 */
public final class CommonUtils {

    /**
     * 权限检测
     *
     * @param context
     * @param permName
     *
     * @return
     */
    public static boolean checkPermission(Context context, String permName) {
        return ContextCompat.checkSelfPermission(context, permName) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 权限申请
     *
     * @param activity
     * @param permission
     * @param requestCode
     */
    public static void requestPermission(Activity activity, String permission, final int requestCode) {
        ActivityCompat.requestPermissions(activity, new String[] {permission}, requestCode);
    }

    /**
     * app安装检测
     *
     * @param context
     * @param packageName
     *
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public static File getExternalCacheDir(Context context) {
        File path = context.getExternalCacheDir();
        if (Build.VERSION.SDK_INT >= 21) {
            if (Environment.getExternalStorageState(path).equals(Environment.MEDIA_MOUNTED)) {
                return path;
            }
        } else {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                return path;
            }
        }
        return context.getCacheDir();
    }

    /**
     * 拷贝assets中的文件到SD卡
     *
     * @param source
     * @param dest
     * @param isCover
     */
    private void copyFromAssetsToSdcard(Context context, String source, String dest, boolean isCover) {
        File file = new File(dest);
        // 文件已存在且不需要覆盖，直接结束
        if (file.exists() && !isCover) {
            return;
        }
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = context.getResources().getAssets().open(source);
            String path = dest;
            fos = new FileOutputStream(path);
            byte[] buffer = new byte[1024];
            int size = 0;
            while ((size = is.read(buffer, 0, 1024)) >= 0) {
                fos.write(buffer, 0, size);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getProcessName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = activityManager.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        int pid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    public static String getFileNameFromUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        url = url.split("\\?")[0];
        Pattern pattern = Pattern.compile("^.*/(.*)$");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * 获取可用存储空间大小（单位：byte）
     *
     * @param path
     *
     * @return
     */
    public static long getAvailableMemorySize(String path) {
        try {
            StatFs statFs = new StatFs(path);
            long blockSize;
            long availableBlocks;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = statFs.getBlockSizeLong();
                availableBlocks = statFs.getAvailableBlocksLong();
            } else {
                blockSize = statFs.getBlockSize();
                availableBlocks = statFs.getAvailableBlocks();
            }
            return blockSize * availableBlocks;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }
}
