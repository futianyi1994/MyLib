package com.bracks.mylib.net.retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * good programmer.
 *
 * @date : 2021-04-27 19:58
 * @author: futia
 * @email : futianyi1994@126.com
 * @description : 处理返回没有响应体body，只有响应头header，content-length为0的Response
 */
public class NullOnEmptyConverterFactory extends Converter.Factory {
    private NullOnEmptyConverterFactory() {
    }

    public static NullOnEmptyConverterFactory create() {
        return new NullOnEmptyConverterFactory();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);
        return (Converter<ResponseBody, Object>) body -> body.contentLength() == 0 ? null : delegate.convert(body);
    }
}