package com.bracks.wanandroid.net;

import com.bracks.mylib.net.https.HttpManager;
import com.bracks.mylib.Contants;

/**
 * good programmer.
 *
 * @date : 2019-01-23 下午 02:03
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class ApiService {
    private static DataApi service;

    private ApiService() {
    }

    public static DataApi getService() {
        if (service == null) {
            synchronized (HttpManager.class) {
                service = HttpManager.getApiService(DataApi.class, Contants.BASE_URL);
            }
        }
        return service;
    }
}
