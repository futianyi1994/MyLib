package com.bracks.futia.mylib.net.interceptor;


import java.io.IOException;
import java.util.List;

import io.reactivex.annotations.NonNull;
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
            // TODO: 2019-01-02 Fty:将COOKIE存入sp
            //SPUtils.put(Constants.COOKIE, sb.toString());
        }
        return originalResponse;
    }

}