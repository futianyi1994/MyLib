package com.bracks.futia.mylib.net.interceptor;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;

import org.jetbrains.annotations.NotNull;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * good programmer.
 *
 * @date : 2018-09-01 下午 04:25
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class HttpLogInterceptor {
    /**
     * logging class
     */
    static class OkHttpLogger implements HttpLoggingInterceptor.Logger {
        @Override
        public void log(@NotNull String message) {
            LogUtils.iTag("oklog", message);
            //Logger.i("oklog: %s", message);
        }
    }

    public static HttpLoggingInterceptor get() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new OkHttpLogger());

        if (AppUtils.isAppDebug()) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }

        return interceptor;
    }
}

