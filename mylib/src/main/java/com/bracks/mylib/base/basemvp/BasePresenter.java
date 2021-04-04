package com.bracks.mylib.base.basemvp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.bracks.mylib.base.interf.BaseView;
import com.bracks.mylib.rx.RxAutoDispose;
import com.bracks.mylib.utils.TLog;
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
    private static final String TAG = "BasePresenter";

    protected LifecycleOwner lifecycleOwner;
    private WeakReference<V> mViewRef;

    public BasePresenter() {
    }

    @Override
    public void onCreatePersenter(@Nullable Bundle savedInstanceState) {
        TLog.i(TAG, "onCreatePersenter savedInstanceState = " + savedInstanceState);
    }

    @Override
    public void onDestroyPersenter() {
        TLog.i(TAG, "onDestroyPersenter");
    }

    @Override
    public Bundle onSaveInstanceState(@Nullable Bundle outState) {
        TLog.i(TAG, "onSaveInstanceState outState = " + outState);
        return outState;
    }

    @Override
    public void onAttchView(V v) {
        mViewRef = new WeakReference<>(v);
    }

    @Override
    public void onDetachView() {
        mViewRef.clear();
    }

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

    protected <T> AutoDisposeConverter<T> bindLifecycleDestroy() {
        if (null == lifecycleOwner) {
            throw new NullPointerException("lifecycleOwner == null");
        }
        return RxAutoDispose.bindLifecycleDestroy(lifecycleOwner);
    }
}
