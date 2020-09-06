package com.bracks.mylib.base.model;

import java.io.Serializable;

/**
 * good programmer.
 *
 * @date : 2019-05-14 下午 01:53
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public abstract class BaseResult<T> implements Serializable {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
