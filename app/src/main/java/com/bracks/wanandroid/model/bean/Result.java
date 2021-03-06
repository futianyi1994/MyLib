package com.bracks.wanandroid.model.bean;


/**
 * good programmer.
 *
 * @date : 2019-04-14 下午 09:25
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class Result<T> extends com.bracks.mylib.base.model.Result<T> {

    @Override
    public boolean ok() {
        return code == 0;
    }

    @Override
    public boolean isRedirect() {
        return code == -1001;
    }
}
