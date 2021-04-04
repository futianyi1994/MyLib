package com.bracks.mylib.net.interceptor;

import android.util.Log;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * good programmer.
 *
 * @date : 2019-04-23 下午 03:38
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class LogInterceptor implements Interceptor {
    private final int priority;

    public LogInterceptor() {
        this.priority = AppUtils.isAppDebug() ? Log.INFO : 0;
    }

    public LogInterceptor(int priority) {
        this.priority = priority;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        Request request = chain.request();
        if (this.priority == 0) {
            return chain.proceed(request);
        }
        Request copy = request.newBuilder().build();
        Buffer buffer = new Buffer();
        RequestBody body = copy.body();
        if (body != null) {
            body.writeTo(buffer);
        }
        print(
                String.format(
                        Locale.getDefault(),
                        "Request: %s Method: @%s%n%sBody: %s",
                        request.url(),
                        request.method(),
                        request.headers(),
                        buffer.readUtf8())
        );

        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            LogUtils.eTag("oklog", e);
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
        String content = response.body().string();
        print(
                String.format(
                        Locale.getDefault()
                        , "Response: %s in %dms%n%sResult：%s"
                        , response.request().url()
                        , tookMs
                        , response.headers()
                        , content)
        );
        return response
                .newBuilder()
                .body(okhttp3.ResponseBody.create(response.body().contentType(), content))
                .build();
    }

    private void print(String contents) {
        switch (this.priority) {
            case Log.VERBOSE:
                LogUtils.vTag("oklog", contents);
                break;
            case Log.DEBUG:
                LogUtils.dTag("oklog", contents);
                break;
            case Log.INFO:
                LogUtils.iTag("oklog", contents);
                break;
            case Log.WARN:
                LogUtils.wTag("oklog", contents);
                break;
            case Log.ERROR:
                LogUtils.eTag("oklog", contents);
                break;
            case Log.ASSERT:
                LogUtils.aTag("oklog", contents);
                break;
            default:
                break;
        }
    }
}