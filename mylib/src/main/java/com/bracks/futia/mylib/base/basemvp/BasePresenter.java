package com.bracks.futia.mylib.base.basemvp;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bracks.futia.mylib.utils.log.TLog;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.lang.ref.WeakReference;

/**
 * good programmer.
 *
 * @date : 2019-01-21 下午 03:44
 * @author: futia
 * @email : futianyi1994@126.com
 * @description : 所有Presenter的基类，并不强制实现这些方法，有需要在重写
 */
public class BasePresenter<V extends BaseView> {
    public static final String TAG = "BasePresenter";

    /**
     * LifecycleProvide接口
     */
    private LifecycleProvider lifecycleProvider;

    /**
     * V层view
     */
    private WeakReference<V> mViewRef;

    public BasePresenter() {
    }

    public <E> BasePresenter(LifecycleProvider<E> lifecycleProvider) {
        this.lifecycleProvider = lifecycleProvider;
    }

    public <E> LifecycleProvider<E> getLifecycleProvider() {
        return lifecycleProvider;
    }

    public <E> void setLifecycleProvider(LifecycleProvider<E> lifecycleProvider) {
        this.lifecycleProvider = lifecycleProvider;
    }

    /**
     * Presenter被创建后调用
     *
     * @param savedState 被意外销毁后重建后的Bundle
     */
    public void onCreatePersenter(@Nullable Bundle savedState) {
        TLog.i(TAG, "P onCreatePersenter = ");
    }


    /**
     * Presenter被销毁时调用
     */
    public void onDestroyPersenter() {
        TLog.i(TAG, "P onDestroyPersenter = ");
    }

    /**
     * 在Presenter意外销毁的时候被调用，它的调用时机和Activity、Fragment、View中的onSaveInstanceState
     * 时机相同
     *
     * @param outState
     */
    public void onSaveInstanceState(Bundle outState) {
        TLog.i(TAG, "P onSaveInstanceState = ");
    }

    /**
     * 进行绑定
     *
     * @param view
     */
    public void onAttchView(V view) {
        mViewRef = new WeakReference<>(view);
    }

    /**
     * 进行解绑
     */
    public void onDetachView() {
        mViewRef.clear();
    }

    /**
     * 获取V层
     *
     * @return
     */
    public V getView() {
        return mViewRef == null ? null : mViewRef.get();
    }
}
