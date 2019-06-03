package com.bracks.mylib.net.interceptor;


import android.support.annotation.NonNull;

import com.blankj.utilcode.util.NetworkUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * good programmer.
 *
 * @data : 2018-03-08 上午 09:32
 * @author: futia
 * @email : futianyi1994@126.com
 * @description : https://www.jianshu.com/p/e3d32c016c32
 * http://chuansong.me/n/1185791251527
 * https://www.jianshu.com/p/9c3b4ea108a7
 */
public class HttpCacheInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        int maxAge = 60 * 60;
        int maxStale = 60 * 60;
        CacheControl.Builder cacheBuilder = new CacheControl.Builder();
        //这个是控制缓存的最大生命时间
        cacheBuilder.maxAge(maxAge, TimeUnit.SECONDS);
        //这个是控制缓存的过时时间;max-stale和max-age同时设置的时候，缓存失效的时间按最长的算。缓存过期后就永远过期。
        cacheBuilder.maxStale(maxStale, TimeUnit.SECONDS);
        CacheControl cacheControl = cacheBuilder.build();
        Request request = chain.request();
        if (NetworkUtils.isConnected()) {
            request = request
                    .newBuilder()
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .build();
        } else {
            request = request
                    .newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response originalResponse = chain.proceed(request);
        if (NetworkUtils.isConnected()) {
            //设置缓存时间为，并移除了pragma消息头，移除它的原因是因为pragma也是控制缓存的一个消息头属性
            return originalResponse
                    .newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public ,max-age=" + 0)
                    .build();

        } else {
            // 注意：max-stale在响应头里设置无效(因为max-stale是请求头设置参数)，对于okhttp缓存的保存是在用户端自定义拦截器之前完成保存的，
            // 所以此时的response中header中设置的自定义的值不会对缓存的存储起什么作用
            return originalResponse
                    .newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached")
                    .build();
        }
    }
}
