package com.bracks.mylib.net.download;

import com.bracks.mylib.Constants;
import com.bracks.mylib.net.https.ProgressListener;
import com.bracks.mylib.utils.json.JsonUtil;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * good programmer.
 *
 * @date : 2018-09-02 上午 11:33
 * @author: futia
 * @email : futianyi1994@126.com
 * @description : 下载工具类
 * @deprecated Use {@link com.bracks.mylib.net.download.breakpoint.DownloadManager} instead.
 */
public class DownloadManager {

    private static Retrofit retrofit;


    public static <T> T init(Class<T> cls, ProgressListener.ProgressCallback callback) {
        return init(cls, Constants.API_URL, callback);
    }

    public static <T> T init(Class<T> cls, String host, ProgressListener.ProgressCallback callback) {
        return init(cls, host, new OkHttpClient.Builder(), callback);
    }

    public static <T> T init(Class<T> cls, String host, OkHttpClient.Builder builder, ProgressListener.ProgressCallback callback) {
        if (retrofit == null) {
            synchronized (DownloadManager.class) {
                retrofit = new Retrofit
                        .Builder()
                        .baseUrl(host)
                        .addConverterFactory(GsonConverterFactory.create(JsonUtil.getGsonBuilder().create()))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(DownloadHelper.addDownloadListener(builder, callback))
                        .build();
            }
        }
        return retrofit.create(cls);
    }
}
