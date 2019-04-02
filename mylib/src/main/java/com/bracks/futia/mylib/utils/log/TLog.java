package com.bracks.futia.mylib.utils.log;

import android.util.Log;

/**
 * Created on 2017-12-22  下午 01:34
 *
 * @author futia
 * @Description: log里面不能出现\r，否则打印不出log,\r是字符串结束标志
 */
public class TLog {
    public static final String LOG_TAG = "Tlog";
    public static final boolean DEBUG = true;

    public TLog() {
    }

    public static void d(String log) {
        if (DEBUG) {
            Log.d(LOG_TAG, log);
        }
    }

    public static void d(String tag, String log) {
        if (DEBUG) {
            Log.d(tag, log);
        }
    }

    public static void e(String log) {
        if (DEBUG) {
            Log.e(LOG_TAG, "" + log);
        }
    }

    public static void e(String tag, String log) {
        if (DEBUG) {
            Log.e(tag, log);
        }
    }

    public static void i(String log) {
        if (DEBUG) {
            Log.i(LOG_TAG, log);
        }
    }

    public static void i(String tag, String log) {
        if (DEBUG) {
            Log.i(tag, log);
        }
    }

    public static void v(String log) {
        if (DEBUG) {
            Log.v(LOG_TAG, log);
        }
    }

    public static void v(String tag, String log) {
        if (DEBUG) {
            Log.v(tag, log);
        }
    }

    public static void w(String log) {
        if (DEBUG) {
            Log.w(LOG_TAG, log);
        }
    }

    public static void w(String tag, String log) {
        if (DEBUG) {
            Log.w(tag, log);
        }
    }

    public static boolean isDebug() {
        return DEBUG;
    }
}
