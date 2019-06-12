package com.bracks.mylib.base.basemvp;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.trello.rxlifecycle2.LifecycleProvider;

/**
 * good programmer.
 *
 * @date : 2019-06-12 下午 06:01
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public interface BasePresenterInter<V extends BaseView> {

    <E> LifecycleProvider<E> getLifecycleProvider();

    <E> void setLifecycleProvider(LifecycleProvider<E> lifecycleProvider);

    /**
     * Presenter被创建后调用
     *
     * @param savedState 被意外销毁后重建后的Bundle
     */
    void onCreatePersenter(@Nullable Bundle savedState);

    /**
     * Presenter被销毁时调用
     */
    void onDestroyPersenter();

    /**
     * 在Presenter意外销毁的时候被调用，它的调用时机和Activity、Fragment、View中的onSaveInstanceState
     * 时机相同
     *
     * @param outState
     */
    void onSaveInstanceState(Bundle outState);

    /**
     * 进行绑定
     *
     * @param view
     */
    void onAttchView(V view);

    /**
     * 进行解绑
     */
    void onDetachView();

    /**
     * 获取V层
     *
     * @return
     */
    V getView();
}
