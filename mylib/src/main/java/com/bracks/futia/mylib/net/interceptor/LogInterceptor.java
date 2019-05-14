package com.bracks.futia.mylib.net.interceptor;

import android.support.annotation.NonNull;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
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
    private HttpLoggingInterceptor.Level level;

    public LogInterceptor() {
        if (AppUtils.isAppDebug()) {
            this.level = HttpLoggingInterceptor.Level.BODY;
        } else {
            this.level = HttpLoggingInterceptor.Level.NONE;
        }
    }

    public LogInterceptor(HttpLoggingInterceptor.Level level) {
        this.level = level;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        Request request = chain.request();
        if (this.level == HttpLoggingInterceptor.Level.NONE) {
            return chain.proceed(request);
        }
        Request copy = request.newBuilder().build();
        Buffer buffer = new Buffer();
        RequestBody body = copy.body();
        if (body != null) {
            body.writeTo(buffer);
        }
        LogUtils.iTag("oklog", String.format(
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
        LogUtils.iTag("oklog", String.format(
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
}