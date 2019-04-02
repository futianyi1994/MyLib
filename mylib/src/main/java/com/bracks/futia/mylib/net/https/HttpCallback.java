package com.bracks.futia.mylib.net.https;

/**
 * good programmer.
 *
 * @date : 2018-09-01 下午 04:25
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public abstract class HttpCallback<T> {

    public abstract void onSuccess(T t);

    public void onFailed(Throwable e) {
    }

    public void onComplete() {
    }
}
