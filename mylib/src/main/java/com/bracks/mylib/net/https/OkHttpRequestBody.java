package com.bracks.mylib.net.https;

import com.bracks.mylib.utils.json.JsonUtil;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.ByteString;

/**
 * good programmer.
 *
 * @date : 2019-04-19 下午 04:22
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public abstract class OkHttpRequestBody extends RequestBody {

    public static final String CONTENT_TYPE = "Content-Type:";
    public static final String JSON = "application/json;charset=utf-8";
    public static final String FORM = "application/x-www-form-urlencoded";
    public static final String MULTIPART = "multipart/form-data";

    /***
     *
     * @param object JsonObject
     * @return RequestBody
     *
     * @deprecated Use createJson instead
     */
    @Deprecated
    public static RequestBody create(Object object) {
        return createJson(object);
    }

    public static RequestBody createJson(Object object) {
        return create(JSON, JsonUtil.toJson(object));
    }

    public static RequestBody createMultipart(String content) {
        return create(MULTIPART, content);
    }


    public static RequestBody create(String string, String content) {
        return RequestBody.create(MediaType.parse(string), content);
    }

    public static RequestBody create(String string, ByteString content) {
        return RequestBody.create(MediaType.parse(string), content);
    }

    public static RequestBody create(String string, byte[] content) {
        return RequestBody.create(MediaType.parse(string), content);
    }

    public static RequestBody create(String string, byte[] content, int offset, int byteCount) {
        return RequestBody.create(MediaType.parse(string), content, offset, byteCount);
    }

    public static RequestBody create(String string, File file) {
        return RequestBody.create(MediaType.parse(string), file);
    }
}
