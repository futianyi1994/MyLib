package com.bracks.mylib.base.model;

/**
 * good programmer.
 *
 * @date : 2019-01-23 上午 11:51
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :实体基类
 */
public class BaseThrowable {
    private Throwable e;

    public BaseThrowable() {
    }

    public BaseThrowable(Throwable e) {
        this.e = e;
    }

    public Throwable getE() {
        return e;
    }

    public void setE(Throwable e) {
        this.e = e;
    }

    public boolean isOk() {
        return e == null;
    }

}
