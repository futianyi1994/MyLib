package com.bracks.mylib.base.basemvp;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bracks.mylib.base.interf.BaseView;
import com.bracks.mylib.utils.TLog;

/**
 * good programmer.
 *
 * @date : 2019-01-23 下午 03:53
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :代理对象类代表的的真正目标对象：真正用来管理Presenter的生命周期，还有和view之间的关联
 */
public class PresenterProxyImpl<V extends BaseView, P extends BasePresenter<V>> implements PresenterProxy<V, P> {
    public static final String TAG = "PresenterProxyImpl";

    private static final String PRESENTER_KEY = "presenter_key";

    private PresenterFactory<P> mFactory;
    private P mPresenter;
    private Bundle mBundle;
    private boolean mIsAttchView;

    public PresenterProxyImpl() {
    }

    /**
     * 传入默认工厂方法
     *
     * @param presenterMvpFactory
     */
    public PresenterProxyImpl(PresenterFactory<P> presenterMvpFactory) {
        this.mFactory = presenterMvpFactory;
    }

    /**
     * 传入默认工厂方法：PresenterFactoryImpl.createFactory(getClass())
     *
     * @param viewClazz
     */
    public PresenterProxyImpl(Class<?> viewClazz) {
        this.mFactory = PresenterFactoryImpl.createFactory(viewClazz);
    }

    @Override
    public void createPresenterFactory(PresenterFactory<P> presenterFactory) {
        if (mPresenter != null) {
            throw new IllegalArgumentException("这个方法只能在getPresenter()之前调用，如果Presenter已经创建则不能再修改");
        }
        this.mFactory = presenterFactory;
    }

    @Override
    public PresenterFactory<P> getPresenterFactory() {
        return mFactory;
    }

    @Override
    public P createPresenter(V mvpView) {
        if (mFactory != null) {
            if (mPresenter == null) {
                mPresenter = mFactory.createPresenter();
                TLog.i(TAG, "Proxy createPresenter = " + mPresenter);
                mPresenter.onCreatePersenter(mBundle == null ? null : mBundle.getBundle(PRESENTER_KEY));
            }
        }
        if (!mIsAttchView) {
            if (mPresenter != null) {
                mPresenter.onAttchView(mvpView);
                mIsAttchView = true;
            }
        }
        return mPresenter;
    }

    @Override
    public P getPresenter() {
        return mPresenter;
    }

    @Override
    public void destroyPresenter() {
        if (mPresenter != null && mIsAttchView) {
            mPresenter.onDetachView();
            mIsAttchView = false;
            mPresenter.onDestroyPersenter();
            mPresenter = null;
        }
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle bundle = new Bundle();
        if (mPresenter != null) {
            bundle.putBundle(PRESENTER_KEY, mPresenter.onSaveInstanceState(new Bundle()));
        }
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        mBundle = savedInstanceState;
    }
}