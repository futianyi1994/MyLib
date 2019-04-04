package com.bracks.futia.mylib.net.upload;

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
 * @description :
 */
public class UploadManager {

    private static Retrofit retrofit;


    public static <T> T init(Class<T> cls, ProgressListener.ProgressCallback callback) {
        return init(cls, Constants.DOWN_UP_LOAD_HOST, callback);
    }

    public static <T> T init(Class<T> cls, String host, ProgressListener.ProgressCallback callback) {
        if (retrofit == null) {
            synchronized (UploadManager.class) {
                retrofit = new Retrofit
                        .Builder()
                        .baseUrl(host)
                        .addConverterFactory(GsonConverterFactory.create(JsonUtil.getGsonBuilder().create()))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(UploadHelper.addUploadListener(new OkHttpClient.Builder(), callback))
                        .build();
            }
        }
        return retrofit.create(cls);
    }
}
