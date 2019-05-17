package com.bracks.futia.mylib.net.interceptor;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * good programmer.
 *
 * @date : 2019-01-10 下午 02:40
 * @author: futia
 * @email : futianyi1994@126.com
 * @description : 统一追加Header
 * 1.获取以前的Builder
 * 2.为以前的Builder添加参数
 * 3.生成新的Builder
 */
public class AppendHeaderParamIntercepter implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();

        Headers.Builder builder = request
                .headers()
                .newBuilder();

        //统一追加Header参数
        Headers newBuilder = builder
                .add("header1", "i am header 1")
                .add("token", "i am token")
                .build();

        Request newRequest = request
                .newBuilder()
                .headers(newBuilder)
                .build();

        return chain.proceed(newRequest);
    }
}
