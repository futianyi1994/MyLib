package com.bracks.futia.mylib.base.basemvp;


import com.bracks.futia.mylib.net.https.HttpCallback;
import com.trello.rxlifecycle2.LifecycleProvider;

/**
 * good programmer.
 *
 * @date : 2019-01-21 下午 04:09
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public interface BaseModel<M> {
    <E> void loadData(LifecycleProvider<E> lifecycleProvider, HttpCallback<M> listener);
}
