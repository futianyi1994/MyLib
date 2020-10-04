package com.bracks.mylib.base.basemvp;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bracks.mylib.base.interf.BaseLifecycleObserver;
import com.bracks.mylib.base.interf.BaseView;


/**
 * good programmer.
 *
 * @date : 2019-06-12 下午 06:01
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public interface BasePresenterInter<V extends BaseView> extends BaseLifecycleObserver {
    /**
     * Presenter被创建时调用
     *
     * @param savedInstanceState 意外销毁后恢复保存的Bundle
     */
    void onCreatePersenter(@Nullable Bundle savedInstanceState);

    /**
     * Presenter被销毁时调用
     */
    void onDestroyPersenter();

    /**
     * Presenter意外销毁的时调用，它的调用时机和Activity、Fragment、View中的onSaveInstanceState时机相同
     *
     * @param outState 在return前可以在outState做一些数据保存工作
     * @return 意外销毁时保存的Bundle
     */
    Bundle onSaveInstanceState(@Nullable Bundle outState);

    /**
     * Presenter绑定View时调用
     *
     * @param v mvpView
     */
    void onAttchView(V v);

    /**
     * Presenter解绑View时调用
     */
    void onDetachView();

    /**
     * 获取VIew时调用
     *
     * @return mvpView
     */
    V getView();
}
