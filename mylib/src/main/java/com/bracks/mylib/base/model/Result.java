package com.bracks.mylib.base.model;

import android.text.TextUtils;

import com.bracks.mylib.net.https.HttpCode;
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
public class Result<T> extends BaseResult<T> implements Serializable {
    @SerializedName("errorCode")
    protected int code;
    @SerializedName("errorMsg")
    protected String msg;
    @SerializedName("error")
    protected boolean error = true;


    public boolean ok() {
        return code == HttpCode.OK || !error;
    }

    public boolean isExpired() {
        return code == HttpCode.UNAUTHORIZED;
    }

    public boolean isRedirect() {
        return code == HttpCode.REDIRECT_FOREVER;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return TextUtils.isEmpty(msg) ? String.valueOf(code) : msg;
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
}
