package com.bracks.futia.mylib.net.interceptor;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * good programmer.
 *
 * @date : 2019-01-10 下午 03:03
 * @author: futia
 * @email : futianyi1994@126.com
 * @description : 将所有的Get -> POST, 将Get后面的Query Params -> Body (基本用不到)
 * 1.获取以前的Builder
 * 2.为以前的Builder添加参数
 * 3.生成新的Builder
 */
public class AppendBodyParamIntercepter implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();

        //拿到所有Query的Key
        Set<String> queryKeyName = request
                .url()
                .queryParameterNames();
        //将query -> body
        StringBuilder bodyString = new StringBuilder();
        for (String s : queryKeyName) {
            bodyString
                    .append(s)
                    .append("=")
                    //查询url后面key的value
                    .append(request.url().queryParameterValues(s))
                    .append(",");
        }
        //构建新body。 MediaType根据实际情况更换
        RequestBody newBody = RequestBody.create(MediaType.parse("application/json"), bodyString.toString().substring(0, bodyString.toString().length() - 1));

        Request newRequest = request
                .newBuilder()
                .post(newBody)
                .build();

        return chain.proceed(newRequest);
    }
}
