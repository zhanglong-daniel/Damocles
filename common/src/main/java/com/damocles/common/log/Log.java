package com.damocles.common.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

import com.damocles.common.constant.Constants;

import android.text.TextUtils;

/**
 * Created by zhanglong02 on 16/8/1.
 */
public final class Log {

    private static final String LOG_TAG = Constants.LOG_TAG;

    private static final boolean LOG_ENABLE = true;

    private static final int ENABLE_PRIORITY = android.util.Log.VERBOSE;

    private Log() {

    }

    public static int v(String msg) {
        return println(android.util.Log.VERBOSE, LOG_TAG, msg, 2);
    }

    public static int v(String tag, String msg) {
        return println(android.util.Log.VERBOSE, tag, msg, 2);
    }

    public static int v(String tag, String msg, Throwable tr) {
        return println(android.util.Log.VERBOSE, tag, msg, tr);

    }

    public static int d(String msg) {
        return println(android.util.Log.DEBUG, LOG_TAG, msg, 2);
    }

    public static int d(String tag, String msg) {
        return println(android.util.Log.DEBUG, tag, msg, 2);
    }

    public static int d(String tag, String msg, Throwable tr) {
        return println(android.util.Log.DEBUG, tag, msg, tr);
    }

    public static int i(String msg) {
        return println(android.util.Log.INFO, LOG_TAG, msg, 2);
    }

    public static int i(String tag, String msg) {
        return println(android.util.Log.INFO, tag, msg, 2);
    }

    public static int i(String tag, String msg, Throwable tr) {
        return println(android.util.Log.INFO, tag, msg, tr);
    }

    public static int w(String msg) {
        return println(android.util.Log.WARN, LOG_TAG, msg, 2);
    }

    public static int w(String tag, String msg) {
        return println(android.util.Log.WARN, tag, msg, 2);
    }

    public static int w(String tag, String msg, Throwable tr) {
        return println(android.util.Log.WARN, tag, msg, tr);
    }

    public static int e(String msg) {
        return println(android.util.Log.ERROR, LOG_TAG, msg, 2);
    }

    public static int e(String tag, String msg) {
        return println(android.util.Log.ERROR, tag, msg, 2);
    }

    public static int e(String tag, String msg, Throwable tr) {
        return println(android.util.Log.ERROR, tag, msg, tr);
    }

    private static int println(int priority, String tag, String msg, Throwable tr) {
        return println(priority, tag, msg + '\n' + getStackTraceString(tr), 3);
    }

    private static int println(int priority, String tag, String msg, int stackLevel) {
        if (!LOG_ENABLE || priority < ENABLE_PRIORITY) {
            return -1;
        }
        tag = TextUtils.equals(LOG_TAG, tag) ? tag : LOG_TAG + "_" + tag;
        StackTraceElement stackTrace = (new Throwable()).getStackTrace()[stackLevel];
        String fileName = stackTrace.getFileName();
        String methodName = stackTrace.getMethodName();
        int lineNumber = stackTrace.getLineNumber();
        if (fileName != null && fileName.contains(".java")) {
            fileName = fileName.replace(".java", "");
        }
        msg = String.format("[%s.%s(): %d] %s", fileName, methodName, lineNumber, msg);
        return android.util.Log.println(priority, tag, msg);
    }

    /**
     * Handy function to get a loggable stack trace from a Throwable
     *
     * @param tr An exception to log
     */
    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

}
