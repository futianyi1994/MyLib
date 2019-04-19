package com.bracks.futia.mylib.net.https;

import com.bracks.futia.mylib.utils.json.JsonUtil;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * good programmer.
 *
 * @date : 2019-04-19 下午 04:22
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public abstract class OkHttpRequestBody extends RequestBody {

    public static RequestBody create(Object object) {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JsonUtil.toJson(object));
    }
}
