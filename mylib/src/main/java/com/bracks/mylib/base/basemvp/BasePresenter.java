package com.bracks.mylib.base.basemvp;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bracks.mylib.rx.RxAutoDispose;
import com.uber.autodispose.AutoDisposeConverter;

import java.lang.ref.WeakReference;

/**
 * good programmer.
 *
 * @date : 2019-01-21 下午 03:44
 * @author: futia
 * @email : futianyi1994@126.com
 * @description : 所有Presenter的基类，并不强制实现这些方法，有需要在重写
 */
public class BasePresenter<V extends BaseView> implements BasePresenterInter<V> {

    private LifecycleOwner lifecycleOwner;

    /**
     * V层view
     */
    private WeakReference<V> mViewRef;

    public BasePresenter() {
    }

    /**
     * Presenter被创建后调用
     *
     * @param savedState 被意外销毁后重建后的Bundle
     */
    @Override
    public void onCreatePersenter(@Nullable Bundle savedState) {
    }


    /**
     * Presenter被销毁时调用
     */
    @Override
    public void onDestroyPersenter() {
    }

    /**
     * 在Presenter意外销毁的时候被调用，它的调用时机和Activity、Fragment、View中的onSaveInstanceState
     * 时机相同
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    /**
     * 进行绑定
     *
     * @param view
     */
    @Override
    public void onAttchView(V view) {
        mViewRef = new WeakReference<>(view);
    }

    /**
     * 进行解绑
     */
    @Override
    public void onDetachView() {
        mViewRef.clear();
    }

    /**
     * 获取V层
     *
     * @return
     */
    @Override
    public V getView() {
        return mViewRef == null ? null : mViewRef.get();
    }

    @Override
    public void setLifecycleOwner(LifecycleOwner owner) {
        lifecycleOwner = owner;
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
    }

    @Override
    public void onLifecycleChanged(@NonNull LifecycleOwner owner, @NonNull Lifecycle.Event event) {
    }

    protected <T> AutoDisposeConverter<T> bindLifecycle() {
        if (null == lifecycleOwner) {
            throw new NullPointerException("lifecycleOwner == null");
        }
        return RxAutoDispose.bindLifecycle(lifecycleOwner);
    }
}
