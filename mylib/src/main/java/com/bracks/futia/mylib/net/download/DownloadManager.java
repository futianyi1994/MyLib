package com.bracks.futia.mylib.net.download;

import com.bracks.futia.mylib.Constants;
import com.bracks.futia.mylib.net.https.ProgressListener;
import com.bracks.futia.mylib.utils.json.JsonUtil;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * good programmer.
 *
 * @date : 2018-09-02 上午 11:33
 * @author: futia
 * @email : futianyi1994@126.com
 * @description : 下载工具类
 * @deprecated Use {@link com.bracks.futia.mylib.net.download.breakpoint.DownloadManager} instead.
 */
public class DownloadManager {

    private static Retrofit retrofit;


    public static <T> T init(Class<T> cls, ProgressListener.ProgressCallback callback) {
        return init(cls, Constants.API_URL, callback);
    }

    public static <T> T init(Class<T> cls, String host, ProgressListener.ProgressCallback callback) {
        if (retrofit == null) {
            synchronized (DownloadManager.class) {
                retrofit = new Retrofit
                        .Builder()
                        .baseUrl(host)
                        .addConverterFactory(GsonConverterFactory.create(JsonUtil.getGsonBuilder().create()))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(DownloadHelper.addDownloadListener(new OkHttpClient.Builder(), callback))
                        .build();
            }
        }
        return retrofit.create(cls);
    }
}
