package com.bracks.futia.mylib.net.interceptor;

import android.support.annotation.NonNull;

import com.bracks.futia.mylib.net.download.ProgressResponseBody;
import com.bracks.futia.mylib.net.https.ProgressListener;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * good programmer.
 *
 * @date : 2019-04-03 下午 06:27
 * @author: futia
 * @email : futianyi1994@126.com
 * @description : 下载文件拦截器
 */
public class DownloadInterceptor implements Interceptor {

    private ProgressListener listener;

    public DownloadInterceptor(ProgressListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        return originalResponse
                .newBuilder()
                .body(new ProgressResponseBody(originalResponse.body(), listener))
                .build();
    }
}