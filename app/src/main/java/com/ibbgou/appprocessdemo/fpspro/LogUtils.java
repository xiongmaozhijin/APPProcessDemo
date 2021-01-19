package com.ibbgou.appprocessdemo.fpspro;

import android.util.Log;

import java.util.Locale;

public class LogUtils {
    public static final String TAG_HTTP = "TAG_HTTP";
    private static final boolean IS_SHOW_THREAD_NAME = true;
    private static final boolean saveLog = false;

    public static void d(String tag, String msg) {
        Log.d(predoTag(tag), msg);
        if (saveLog) {
        }
    }

    public static void v(String tag, String msg) {
        Log.v(predoTag(tag), msg);
    }

    public static void i(String tag, String msg) {
        Log.i(predoTag(tag), msg);
        if (saveLog) {
        }
    }

    public static void w(String tag, String msg) {
        Log.w(predoTag(tag), msg);
        if (saveLog) {
        }
    }

    public static void e(String tag, String msg) {
        Log.e(predoTag(tag), msg);
        if (saveLog) {
        }
    }

    private static String predoTag(String tag) {
        if (IS_SHOW_THREAD_NAME) {
            return String.format(Locale.getDefault(), "[%s]%s", Thread.currentThread().getName(), tag);
        }

        return tag;
    }

}
