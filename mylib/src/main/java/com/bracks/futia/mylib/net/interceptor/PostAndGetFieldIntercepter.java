package com.bracks.futia.mylib.net.interceptor;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * good programmer.
 *
 * @date : 2019-01-10 上午 10:02
 * @author: futia
 * @email : futianyi1994@126.com
 * @description : Post和Get请求字段修改拦截器
 */
public class PostAndGetFieldIntercepter implements Interceptor {
    public static final String TAG = PostAndGetFieldIntercepter.class.getSimpleName();

    @Override
    public Response intercept(Chain chain) throws IOException {
        //得到原始的请求对象
        Request request = chain.request();
        //得到用户所使用的请求方式
        String method = request.method();

        if ("GET".equals(method)) {
            //原始的请求接口
            String oldUrl = request.url().toString();

            //拼接成一个新的URL
            String newUrl = oldUrl
                    //+ "&source=android"
                    ;

            //重新构建请求体
            request = new Request
                    .Builder()
                    .url(newUrl)
                    .build();

        } else if ("POST".equals(method)) {
            //得到原始的url
            String oldUrl = request.url().toString();
            //得到原有的请求参数
            FormBody oldBody = (FormBody) request.body();

            //新的构建项
            FormBody.Builder builder = new FormBody.Builder();
            for (int i = 0; i < oldBody.size(); ++i) {
                //取出相关请求参数(原有的)
                String name = oldBody.name(i);
                String value = oldBody.value(i);
                //将原始的参数拼装到新的构建体当中
                builder.add(name, value);
            }

            //在原来的基础上添加公共的参数
            //builder.add("common", "common");

            /****************************************************************/
            // TODO: 2019-01-10 Fty: 以下根据需求自定义
            //只有获取Token的接口需要每次请求更改时间戳和signMsg
            if (oldUrl.contains("token/getToken")) {
                Long currentTime = System.currentTimeMillis();
                //修改需要重新构建项覆盖原来的
                builder = new FormBody.Builder();
                //添加新的参数
                builder.add("currentTime", String.valueOf(currentTime));
            }
            /****************************************************************/

            request = new Request
                    .Builder()
                    .url(oldUrl)
                    .post(builder.build())
                    .build();

        }
        //重新发送请求
        return chain.proceed(request);
    }
}
