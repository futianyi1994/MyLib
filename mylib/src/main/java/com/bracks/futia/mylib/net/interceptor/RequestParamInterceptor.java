package com.bracks.futia.mylib.net.interceptor;

import android.support.annotation.NonNull;

import com.blankj.utilcode.util.AppUtils;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * good programmer.
 *
 * @date : 2019-01-02 上午 11:03
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class RequestParamInterceptor implements Interceptor {

    private static final String PLATFORM = "platform";
    private static final String DEVICE_ID = "device-id";
    private static final String VERSION = "version";
    private static final String COOKIE = "cookie";


    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        Request originalRequest = chain.request();

        String strUrl = originalRequest.url().toString();

        HttpUrl.Builder authorizedUrlBuilder = originalRequest
                .url()
                .newBuilder()
                .scheme(originalRequest.url().scheme())
                .host(originalRequest.url().host());

        String cookie = "";
        // TODO: 2019-01-02 Fty:赋值COOKIE
        //cookie = SPUtils.getString(Constants.COOKIE);
        String client_Id = "";
        // TODO: 2019-01-02 Fty:赋值DEVICE_ID
        //client_Id= UUIDTool.getUniqueId(CommonUtils.getContext());
        String originalUrl = authorizedUrlBuilder.build().url().toString();
        //在新的Url上拼接版本号等信息（如果需要）
        String newUrl;
        if (!originalUrl.contains("?")) {
            newUrl = originalUrl.concat("?version=" + AppUtils.getAppVersionName() + "&client_Id=" + client_Id);
        } else {
            newUrl = originalUrl.concat("&version=" + AppUtils.getAppVersionName() + "&client_Id=" + client_Id);
        }
        Request.Builder newRequestBuilder = originalRequest
                .newBuilder()
                .url(newUrl)
                .method(originalRequest.method(), originalRequest.body())
                .addHeader(COOKIE, cookie)
                .addHeader(PLATFORM, "android")
                .addHeader(DEVICE_ID, client_Id)
                .addHeader(VERSION, AppUtils.getAppVersionName());

        Request newRequest = newRequestBuilder.build();

        return chain.proceed(newRequest);
    }
}

