package com.bracks.mylib.net.interceptor;


import androidx.annotation.NonNull;

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
    private int maxAge;
    private int maxStale;
    private TimeUnit maxAgeTimeUnit;
    private TimeUnit maxStaleTimeUnit;
    private CacheControl cacheControl;

    /**
     * 设置缓存忽略网络状况
     */
    private boolean ignoreNetwork;

    public HttpCacheInterceptor() {
        this(true);
    }

    public HttpCacheInterceptor(boolean isCache) {
        this(isCache ? CacheControl.FORCE_CACHE : CacheControl.FORCE_NETWORK);
    }

    public HttpCacheInterceptor(int maxAge, int maxStale) {
        this(maxAge, maxStale, TimeUnit.SECONDS);
    }

    public HttpCacheInterceptor(int maxAge, int maxStale, TimeUnit timeUnit) {
        this(maxAge, timeUnit, maxStale, timeUnit);
    }

    public HttpCacheInterceptor(CacheControl cacheControl) {
        this.cacheControl = cacheControl;
    }

    public HttpCacheInterceptor(int maxAge, TimeUnit maxAgeTimeUnit, int maxStale, TimeUnit maxStaleTimeUnit) {
        this.maxAge = maxAge;
        this.maxStale = maxStale;
        this.maxAgeTimeUnit = maxAgeTimeUnit;
        this.maxStaleTimeUnit = maxStaleTimeUnit;
    }

    public HttpCacheInterceptor setIgnoreNetwork(boolean ignoreNetwork) {
        this.ignoreNetwork = ignoreNetwork;
        return this;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        CacheControl cacheControl = this.cacheControl != null
                ? this.cacheControl
                :
                new CacheControl
                        .Builder()
                        //这个是控制缓存的最大生命时间
                        .maxAge(this.maxAge, this.maxAgeTimeUnit)
                        /*
                         * 这个是控制缓存的过时时间：max-stale和max-age同时设置的时候，缓存失效的时间maxStale按最长的算，缓存可用为maxAge+maxStale,
                         * 但是在超过maxAge但不超过maxAge+maxStale时请求会有Warning: 110 HttpURLConnection"Response is stale"警告，超过maxAge+maxStale时则请求失败，
                         * 缓存过期后就永远过期。
                         */
                        .maxStale(this.maxStale, this.maxStaleTimeUnit)
                        .build();

        Request request = chain.request();
        if (this.ignoreNetwork || !NetworkUtils.isConnected()) {
            request = request
                    .newBuilder()
                    .cacheControl(cacheControl)
                    .build();
        }

        Response response = chain.proceed(request);
        //max-stale在响应头里设置无效不会对缓存的存储起什么作用(因为max-stale是请求头设置参数)，对于okhttp缓存的保存是在用户端自定义拦截器之前完成保存的
        return response
                .newBuilder()
                //移除了pragma消息头，移除它的原因是因为pragma也是控制缓存的一个消息头属性
                .removeHeader("Pragma")
                //头部Cache-Control设为only-if-cached时使用缓存
                //.header("Cache-Control", "public, only-if-cached")
                //头部Cache-Control设为max-age=0时则不会使用缓存而请求服务器
                //.header("Cache-Control", "public ,max-age=" + 0)
                //.header("Cache-Control", request.cacheControl().toString())
                .header("Cache-Control", cacheControl.toString())
                .build();
    }
}
