package com.bracks.futia.mylib.base.model;

import android.text.TextUtils;

import com.bracks.futia.mylib.net.https.HttpCode;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * good programmer.
 *
 * @date : 2019-01-23 上午 11:51
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :实体基类：实现序列化
 */
public class Result<T> implements Serializable {
    @SerializedName("errorCode")
    private int code;
    @SerializedName("errorMsg")
    private String msg;
    @SerializedName("error")
    private boolean error = true;


    public boolean OK() {
        return code == HttpCode.CODE_SUCCESS || !error;
    }

    public boolean isExpired() {
        return code == HttpCode.CODE_EXPIRED;
    }

    public boolean isRedirect() {
        return code == HttpCode.CODE_REDIRECT;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        if (TextUtils.isEmpty(msg)) {
            return String.valueOf(code);
        } else {
            return msg;
        }
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
