package com.bracks.futia.mylib.base.basemvp;

import android.os.Bundle;

import com.bracks.futia.mylib.utils.log.TLog;

/**
 * good programmer.
 *
 * @date : 2019-01-23 下午 03:53
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :代理目标对象：代理实现类，用来管理Presenter的生命周期，还有和view之间的关联
 */
public class PresenterProxyImpl<V extends BaseView, P extends BasePresenter<V>> implements PresenterProxy<V, P> {
    public static final String TAG = "PresenterProxyImpl";

    /**
     * 获取onSaveInstanceState中bundle的key
     */
    private static final String PRESENTER_KEY = "presenter_key";

    /**
     * Presenter工厂类
     */
    private PresenterFactory<V, P> mFactory;
    private P mPresenter;
    private Bundle mBundle;
    private boolean mIsAttchView;

    /**
     * 传入默认工厂方法
     *
     * @param presenterMvpFactory
     */
    public PresenterProxyImpl(PresenterFactory<V, P> presenterMvpFactory) {
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

    /**
     * 设置Presenter的工厂类,这个方法只能在创建Presenter之前调用,也就是调用getPresenter()之前，覆盖默认工厂方法。如果Presenter已经创建则不能再修改
     *
     * @param presenterFactory PresenterFactory类型
     */
    @Override
    public void setPresenterFactory(PresenterFactory<V, P> presenterFactory) {
        if (mPresenter != null) {
            throw new IllegalArgumentException("这个方法只能在getPresenter()之前调用，如果Presenter已经创建则不能再修改");
        }
        this.mFactory = presenterFactory;
    }

    /**
     * 获取Presenter的工厂类
     *
     * @return PresenterMvpFactory类型
     */
    @Override
    public PresenterFactory<V, P> getPresenterFactory() {
        return mFactory;
    }

    /**
     * 获取创建的Presenter
     *
     * @return 指定类型的Presenter
     * 如果之前创建过，而且是意外销毁则从Bundle中恢复
     */
    @Override
    public P getPresenter() {
        TLog.i(TAG, "Proxy getPresenter");
        if (mFactory != null) {
            if (mPresenter == null) {
                mPresenter = mFactory.createPresenter();
                mPresenter.onCreatePersenter(mBundle == null ? null : mBundle.getBundle(PRESENTER_KEY));
            }
        }
        TLog.i(TAG, "Proxy getPresenter = " + mPresenter);
        return mPresenter;
    }

    /**
     * 绑定Presenter和view前先创建Presenter
     *
     * @param mvpView
     */
    public void onCreate(V mvpView) {
        TLog.i(TAG, "Proxy onCreate");
        getPresenter();
        if (!mIsAttchView) {
            if (mPresenter != null) {
                mPresenter.onAttchView(mvpView);
                mIsAttchView = true;
            }
        }
    }

    /**
     * 销毁Presenter
     */
    public void onDestroy() {
        TLog.i(TAG, "Proxy onDestroy = ");
        if (mPresenter != null && mIsAttchView) {
            mPresenter.onDetachView();
            mIsAttchView = false;
            mPresenter.onDestroyPersenter();
            mPresenter = null;
        }
    }

    /**
     * 意外销毁的时候调用
     *
     * @return Bundle，存入回调给Presenter的Bundle和当前Presenter的id
     */
    public Bundle onSaveInstanceState() {
        TLog.i(TAG, "Proxy onSaveInstanceState = ");
        getPresenter();
        Bundle bundle = new Bundle();
        Bundle presenterBundle = new Bundle();
        //回调Presenter
        if (mPresenter != null) {
            mPresenter.onSaveInstanceState(presenterBundle);
            bundle.putBundle(PRESENTER_KEY, presenterBundle);
        }
        return bundle;
    }

    /**
     * 意外销毁恢复Presenter
     *
     * @param savedInstanceState 意外销毁时存储的Bundler
     */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        TLog.i(TAG, "Proxy onRestoreInstanceState Presenter = " + mPresenter);
        mBundle = savedInstanceState;

    }
}