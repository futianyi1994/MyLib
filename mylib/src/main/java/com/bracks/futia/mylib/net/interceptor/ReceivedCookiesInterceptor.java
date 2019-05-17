package com.bracks.futia.mylib.net.interceptor;


import android.support.annotation.NonNull;

import com.bracks.futia.mylib.Constants;
import com.bracks.futia.mylib.utils.save.SPUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * good programmer.
 *
 * @date : 2018-10-08 16:13
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class ReceivedCookiesInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            List<String> cookies = originalResponse.headers("Set-Cookie");
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < cookies.size(); j++) {
                if (sb.length() > 0) {
                    sb.append(";");
                }
                sb.append(cookies.get(j));
            }
            SPUtils.put(Constants.COOKIE, sb.toString());
        }
        return originalResponse;
    }
}