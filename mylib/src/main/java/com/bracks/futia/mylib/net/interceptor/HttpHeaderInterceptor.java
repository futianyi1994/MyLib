package com.bracks.futia.mylib.net.interceptor;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * good programmer.
 *
 * @date : 2018-09-01 下午 04:37
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class HttpHeaderInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        //配置请求头
        String accessToken = "token";
        String tokenType = "tokenType";
        Request request = chain.request().newBuilder()
                .header("app_key", "appId")
                .header("Authorization", tokenType + " " + accessToken)
                .header("Content-Type", "application/json")
                .addHeader("Connection", "close")
                .addHeader("Accept-Encoding", "identity")
                .build();
        return chain.proceed(request);
    }
}
