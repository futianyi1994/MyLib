package com.bracks.futia.mylib.net.https;

import com.bracks.futia.mylib.Constants;
import com.bracks.futia.mylib.net.interceptor.AppendBodyParamIntercepter;
import com.bracks.futia.mylib.net.interceptor.AppendHeaderParamIntercepter;
import com.bracks.futia.mylib.net.interceptor.AppendUrlParamIntercepter;
import com.bracks.futia.mylib.net.interceptor.HttpCacheInterceptor;
import com.bracks.futia.mylib.net.interceptor.HttpHeaderInterceptor;
import com.bracks.futia.mylib.net.interceptor.HttpLogInterceptor;
import com.bracks.futia.mylib.net.interceptor.PostAndGetFieldIntercepter;
import com.bracks.futia.mylib.net.interceptor.ReceivedCookiesInterceptor;
import com.bracks.futia.mylib.net.interceptor.RequestParamInterceptor;
import com.bracks.futia.mylib.net.interceptor.ResponseParamInterceptor;
import com.bracks.futia.mylib.utils.CommonUtils;
import com.bracks.futia.mylib.utils.json.JsonUtil;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.Cache;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * good programmer.
 *
 * @date : 2018-09-01 下午 04:25
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class HttpManager {

    private final static int CONNECT_TIMEOUT = 5;
    private final static int READ_TIMEOUT = 10;
    private final static int WRITE_TIMEOUT = 10;


    private static Retrofit retrofit;
    private static Retrofit.Builder retrofiitBuilder;
    private static OkHttpClient.Builder okHttpClientBuilder;

    private static ClearableCookieJar cookieJar;

    public static List<Cookie> getCookies(String url) {
        return cookieJar.loadForRequest(HttpUrl.parse(url));
    }

    public static OkHttpClient.Builder getOkHttpClientBuilder() {

        HttpLoggingInterceptor loggingInterceptor = HttpLogInterceptor.get();
        HttpCacheInterceptor httpCacheInterceptor = new HttpCacheInterceptor();
        HttpHeaderInterceptor headerInterceptor = new HttpHeaderInterceptor();
        RequestParamInterceptor requestParamInterceptor = new RequestParamInterceptor();
        ResponseParamInterceptor responseParamInterceptor = new ResponseParamInterceptor();
        PostAndGetFieldIntercepter postAndGetFieldIntercepter = new PostAndGetFieldIntercepter();
        ReceivedCookiesInterceptor receivedCookiesInterceptor = new ReceivedCookiesInterceptor();
        AppendUrlParamIntercepter appendUrlParamIntercepter = new AppendUrlParamIntercepter();
        AppendBodyParamIntercepter appendBodyParamIntercepter = new AppendBodyParamIntercepter();
        AppendHeaderParamIntercepter appendHeaderParamIntercepter = new AppendHeaderParamIntercepter();

        File cacheFile = new File(CommonUtils.getContext().getExternalCacheDir(), Constants.HTTP_CACHE);
        int cacheSize = 100 * 1024 * 1024;
        Cache cache = new Cache(cacheFile, cacheSize);

        cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(CommonUtils.getContext()));
        if (okHttpClientBuilder == null) {
            synchronized (HttpManager.class) {
                okHttpClientBuilder = new OkHttpClient
                        .Builder()
                        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        .cookieJar(cookieJar)
                        //.authenticator(new TokenAuth())
                        // https认证 如果要使用https且为自定义证书 可以去掉这两行注释，并自行配制证书。
                        //.sslSocketFactory(SslContextFactory.getSSLSocketFactoryForTwoWay())
                        //.hostnameVerifier(new SafeHostnameVerifier())
                        .cache(cache)
                        .addInterceptor(httpCacheInterceptor)
                        .addNetworkInterceptor(httpCacheInterceptor)
                        //.addInterceptor(headerInterceptor)
                        //.addInterceptor(requestParamInterceptor)
                        //.addInterceptor(responseParamInterceptor);
                        //.addInterceptor(postAndGetFieldIntercepter)
                        //.addInterceptor(receivedCookiesInterceptor)
                        //.addInterceptor(appendUrlParamIntercepter)
                        //.addInterceptor(appendBodyParamIntercepter)
                        //.addInterceptor(appendHeaderParamIntercepter)
                        //日志拦截器放最后（可以打印更多更准确信息）
                        .addInterceptor(loggingInterceptor)
                ;
            }
        }
        return okHttpClientBuilder;
    }


    public static Retrofit.Builder getRetrofitBuilder(String baseUrl) {
        return getRetrofitBuilder(baseUrl, getOkHttpClientBuilder());
    }

    public static Retrofit.Builder getRetrofitBuilder(String baseUrl, OkHttpClient.Builder builder) {
        if (retrofiitBuilder == null) {
            synchronized (HttpManager.class) {
                //多域名时使用：me.jessyan:retrofit-url-manager:1.4.0
                OkHttpClient okHttpClient = RetrofitUrlManager
                        .getInstance()
                        .with(builder)
                        .build();
                retrofiitBuilder = new Retrofit.Builder()
                        .client(okHttpClient)
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create(JsonUtil.getGsonBuilder().create()))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

                //不使用多域名
                /*retrofiitBuilder = new Retrofit.Builder()
                        .client(getOkHttpClientBuilder(true).build())
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create(JsonUtil.getGsonBuilder().create()))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create());*/
            }
        }
        return retrofiitBuilder;
    }

    public static <T> T getApiService(Class<T> cls, String baseUrl) {
        return getApiService(cls, baseUrl, getOkHttpClientBuilder());
    }

    public static <T> T getApiService(Class<T> cls, String baseUrl, OkHttpClient.Builder builder) {
        if (retrofit == null) {
            synchronized (HttpManager.class) {
                retrofit = getRetrofitBuilder(baseUrl, builder).build();
            }
        }
        return retrofit.create(cls);
    }

    public static <T> T getApiService(Class<T> cls, String baseUrl, Interceptor interceptor) {
        if (retrofit == null) {
            synchronized (HttpManager.class) {
                retrofit = getRetrofitBuilder(baseUrl, getOkHttpClientBuilder().addInterceptor(interceptor)).build();
            }
        }
        return retrofit.create(cls);
    }

}
