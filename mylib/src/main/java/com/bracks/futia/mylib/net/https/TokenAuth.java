package com.bracks.futia.mylib.net.https;

/**
 * Created by Alan on 2017/6/27.
 */

import com.bracks.futia.mylib.utils.log.TLog;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * @author futia
 * @date 20/01/2017
 * 只有返回HTTP的状态码为401时，才会使用Authenticator接口
 */

public class TokenAuth implements Authenticator {
    private boolean isLoginSyn;

    @Override
    public Request authenticate(Route route, Response response) throws IOException {

        Request oriRequest = response.request();

        String oriUrl = oriRequest.url().toString();
        TLog.i("url = " + oriUrl);

        //重新调用登陆接口获取token是否成功
        if (isLoginSyn) {
            TLog.i("Authenticating for response: " + response);
            String token = "";
            HttpUrl.Builder authorizedUrlBuilder = oriRequest.url()
                    .newBuilder()
                    .scheme(oriRequest.url().scheme())
                    .host(oriRequest.url().host());

            Request.Builder newRequestBuilder = oriRequest.newBuilder()
                    .url(authorizedUrlBuilder.build())
                    .removeHeader("yui2-token")
                    .addHeader("yui2-token", token)
                    .method(oriRequest.method(), oriRequest.body());

            return newRequestBuilder.build();

        } else {
            throw new IOException("登陆已过期,请重新登陆");
        }
    }
}
