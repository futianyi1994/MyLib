package com.bracks.mylib.net.interceptor;

import android.support.annotation.NonNull;

import com.bracks.mylib.base.model.Result;
import com.bracks.mylib.utils.json.JsonUtil;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * good programmer.
 *
 * @data : 2018-02-27 下午 02:03
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class ResponseParamInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();

        // try the request
        Response originalResponse = chain.proceed(request);

        ResponseBody responseBody = originalResponse.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE);
        Buffer buffer = source.buffer();
        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(UTF8);
        }
        String bodyString = buffer.clone().readString(charset);
        Result result = JsonUtil.fromJson(bodyString, Result.class);
        if (!result.OK()) {
            //cookie失效重新登录
            if (result.isExpired() || result.isRedirect()) {
                //此判断排除个别请求不跳转
                if (!request.url().toString().contains("app/v3/user/auto-info")) {
                    // TODO: 2019-01-02 Fty: 跳转到登陆页面
                    //LoginUtils.toLoginUIWithFlags(CommonUtils.getContext(), Intent.FLAG_ACTIVITY_NEW_TASK);
                }
            }
        }
        return originalResponse;
    }
}
