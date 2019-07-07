package com.bracks.mylib.base.basemvp;


import io.reactivex.Observable;

/**
 * good programmer.
 *
 * @date : 2019-01-21 下午 04:09
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public interface BaseModel<M> {
    Observable<M> loadData();
}
