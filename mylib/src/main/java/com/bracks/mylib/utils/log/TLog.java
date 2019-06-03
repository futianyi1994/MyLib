package com.bracks.mylib.utils.log;

import android.util.Log;

import com.blankj.utilcode.util.AppUtils;

/**
 * Created on 2017-12-22  下午 01:34
 *
 * @author futia
 * @Description: log里面不能出现\r，否则打印不出log,\r是字符串结束标志
 */
public class TLog {
    private static final String LOG_TAG = "Tlog";
    private static boolean DEBUG = AppUtils.isAppDebug();

    public TLog() {
    }

    public static void d(String log) {
        d(LOG_TAG, log);
    }

    public static void d(String tag, String log) {
        if (DEBUG) {
            Log.d(tag, log);
        }
    }

    public static void d(String tag, String msg, Throwable throwable) {
        if (DEBUG) {
            Log.d(tag, msg, throwable);
        }
    }

    public static void e(String log) {
        e(LOG_TAG, log);
    }

    public static void e(String tag, String log) {
        if (DEBUG) {
            Log.e(tag, log);
        }
    }

    public static void e(String tag, String msg, Throwable throwable) {
        if (DEBUG) {
            Log.e(tag, msg, throwable);
        }
    }

    public static void i(String log) {
        i(LOG_TAG, log);
    }

    public static void i(String tag, String log) {
        if (DEBUG) {
            Log.i(tag, log);
        }
    }

    public static void i(String tag, String msg, Throwable throwable) {
        if (DEBUG) {
            Log.i(tag, msg, throwable);
        }
    }

    public static void v(String log) {
        v(LOG_TAG, log);
    }

    public static void v(String tag, String log) {
        if (DEBUG) {
            Log.v(tag, log);
        }
    }

    public static void v(String tag, String msg, Throwable throwable) {
        if (DEBUG) {
            Log.v(tag, msg, throwable);
        }
    }

    public static void w(String log) {
        w(LOG_TAG, log);
    }

    public static void w(String tag, String log) {
        if (DEBUG) {
            Log.w(tag, log);
        }
    }

    public static void w(String tag, Throwable throwable) {
        if (DEBUG) {
            Log.w(tag, throwable);
        }
    }

    public static void w(String tag, String msg, Throwable throwable) {
        if (DEBUG) {
            Log.w(tag, msg, throwable);
        }
    }

    public static boolean isDebug() {
        return DEBUG;
    }

    public static void setDebug(boolean debug) {
        DEBUG = debug;
    }
}
