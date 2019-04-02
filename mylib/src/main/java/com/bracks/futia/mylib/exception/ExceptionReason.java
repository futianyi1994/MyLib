package com.bracks.futia.mylib.exception;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * good programmer.
 *
 * @date : 2019-02-11 上午 10:23
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :请求网络失败原因
 */
public class ExceptionReason {
    /**
     * 解析数据失败
     */
    public static final int PARSE_ERROR = 0;
    /**
     * 网络问题
     */
    public static final int BAD_NETWORK = 1;
    /**
     * 连接错误
     */
    public static final int CONNECT_ERROR = 2;
    /**
     * 连接超时
     */
    public static final int CONNECT_TIMEOUT = 3;
    /**
     * 未知错误
     */
    public static final int UNKNOWN_ERROR = 4;

    @IntDef({PARSE_ERROR, BAD_NETWORK, CONNECT_ERROR, CONNECT_TIMEOUT, UNKNOWN_ERROR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Reason {
    }
}
