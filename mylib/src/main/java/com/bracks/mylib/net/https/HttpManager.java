package com.bracks.mylib.net.https;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.Utils;
import com.bracks.mylib.Constants;
import com.bracks.mylib.net.interceptor.HttpCacheInterceptor;
import com.bracks.mylib.net.interceptor.LogInterceptor;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
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

    public final static int CONNECT_TIMEOUT = 5;
    public final static int READ_TIMEOUT = 10;
    public final static int WRITE_TIMEOUT = 10;

    private static final Map<String, Retrofit> RETROFIT_MAP = new HashMap<>();
    private static final Map<String, Retrofit.Builder> RETROFIT_BUILDER_MAP = new HashMap<>();
    private static final Map<String, OkHttpClient.Builder> OKHTTPCLIENT_BUILDER_MAP = new HashMap<>();
    private static final Map<String, ClearableCookieJar> COOKIE_MAP = new HashMap<>();

    public static void clear() {
        RETROFIT_MAP.clear();
        RETROFIT_BUILDER_MAP.clear();
        OKHTTPCLIENT_BUILDER_MAP.clear();
        COOKIE_MAP.clear();
    }

    public static List<Cookie> getCookies(String baseUrl, String url) {
        ClearableCookieJar cookieJar = COOKIE_MAP.get(baseUrl);
        return cookieJar != null ? cookieJar.loadForRequest(HttpUrl.parse(url)) : null;
    }

    public static OkHttpClient.Builder getOkHttpClientBuilder(String baseUrl) {
        OkHttpClient.Builder okHttpClientBuilder = OKHTTPCLIENT_BUILDER_MAP.get(baseUrl);
        if (okHttpClientBuilder == null) {
            synchronized (HttpManager.class) {
                LogInterceptor logInterceptor = new LogInterceptor();
                HttpCacheInterceptor httpCacheInterceptor = new HttpCacheInterceptor();
                //HttpLoggingInterceptor loggingInterceptor = HttpLogInterceptor.get();
                //PostAndGetFieldIntercepter postAndGetFieldIntercepter = new PostAndGetFieldIntercepter();

                //RequestParamInterceptor requestParamInterceptor = new RequestParamInterceptor();
                //HttpHeaderInterceptor headerInterceptor = new HttpHeaderInterceptor();
                //AppendUrlParamIntercepter appendUrlParamIntercepter = new AppendUrlParamIntercepter();
                //AppendBodyParamIntercepter appendBodyParamIntercepter = new AppendBodyParamIntercepter();
                //AppendHeaderParamIntercepter appendHeaderParamIntercepter = new AppendHeaderParamIntercepter();

                //ResponseParamInterceptor responseParamInterceptor = new ResponseParamInterceptor();
                //ReceivedCookiesInterceptor receivedCookiesInterceptor = new ReceivedCookiesInterceptor();

                File cacheFile = new File(Utils.getApp().getExternalCacheDir(), Constants.HTTP_CACHE);
                int cacheSize = 100 * 1024 * 1024;
                Cache cache = new Cache(cacheFile, cacheSize);
                ClearableCookieJar cookieJar = COOKIE_MAP.get(baseUrl);
                if (cookieJar == null) {
                    cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(Utils.getApp()));
                    COOKIE_MAP.put(baseUrl, cookieJar);
                }
                okHttpClientBuilder = new OkHttpClient
                        .Builder()
                        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        .cookieJar(cookieJar)
                        //.authenticator(new TokenAuth())
                        // https认证 如果要使用https且为自定义证书 可以去掉这两行注释，并自行配制证书。
                        //.sslSocketFactory(SslContextFactory.getSSLSocketFactoryForTwoWay(0,0))
                        //.hostnameVerifier(new SafeHostnameVerifier())
                        .cache(cache)
                        .addInterceptor(httpCacheInterceptor)
                        .addNetworkInterceptor(httpCacheInterceptor)
                        //.addInterceptor(postAndGetFieldIntercepter)
                        //.addInterceptor(requestParamInterceptor)
                        //.addInterceptor(headerInterceptor)
                        //.addInterceptor(appendUrlParamIntercepter)
                        //.addInterceptor(appendBodyParamIntercepter)
                        //.addInterceptor(appendHeaderParamIntercepter)
                        //.addInterceptor(responseParamInterceptor);
                        //.addInterceptor(receivedCookiesInterceptor)
                        //日志拦截器放最后（可以打印更多更准确信息）
                        .addInterceptor(logInterceptor)
                ;
            }
            OKHTTPCLIENT_BUILDER_MAP.put(baseUrl, okHttpClientBuilder);
            return okHttpClientBuilder;
        }
        return okHttpClientBuilder;
    }


    public static Retrofit.Builder getRetrofitBuilder(String baseUrl) {
        return getRetrofitBuilder(baseUrl, getOkHttpClientBuilder(baseUrl));
    }

    public static Retrofit.Builder getRetrofitBuilder(String baseUrl, OkHttpClient.Builder builder) {
        Retrofit.Builder retrofiitBuilder = RETROFIT_BUILDER_MAP.get(baseUrl);
        if (retrofiitBuilder == null) {
            synchronized (HttpManager.class) {
                //多域名时使用：me.jessyan:retrofit-url-manager:1.4.0
                /*OkHttpClient okHttpClient = RetrofitUrlManager
                        .getInstance()
                        .with(builder)
                        .build();
                retrofiitBuilder = new Retrofit.Builder()
                        .client(okHttpClient)
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create(JsonUtil.getGsonBuilder().create()))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create());*/

                //不使用多域名
                retrofiitBuilder = new Retrofit.Builder()
                        .client(builder.build())
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create(GsonUtils.getGson()))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
                RETROFIT_BUILDER_MAP.put(baseUrl, retrofiitBuilder);
            }
        }
        return retrofiitBuilder;
    }

    public static <T> T getApiService(Class<T> cls, String baseUrl) {
        return getApiService(cls, baseUrl, getOkHttpClientBuilder(baseUrl));
    }

    public static <T> T getApiService(Class<T> cls, String baseUrl, OkHttpClient.Builder builder) {
        Retrofit retrofit = RETROFIT_MAP.get(baseUrl);
        if (retrofit == null) {
            synchronized (HttpManager.class) {
                retrofit = getRetrofitBuilder(baseUrl, builder).build();
                RETROFIT_MAP.put(baseUrl, retrofit);
            }
        }
        return retrofit.create(cls);
    }

    public static <T> T getApiService(Class<T> cls, String baseUrl, Interceptor interceptor) {
        Retrofit retrofit = RETROFIT_MAP.get(baseUrl);
        if (retrofit == null) {
            synchronized (HttpManager.class) {
                retrofit = getRetrofitBuilder(baseUrl, getOkHttpClientBuilder(baseUrl).addInterceptor(interceptor)).build();
                RETROFIT_MAP.put(baseUrl, retrofit);
            }
        }
        return retrofit.create(cls);
    }

}
