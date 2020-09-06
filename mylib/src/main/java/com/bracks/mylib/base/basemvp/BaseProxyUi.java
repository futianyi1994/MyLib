package com.bracks.mylib.base.basemvp;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bracks.mylib.base.BaseActivity;

/**
 * good programmer.
 *
 * @date : 2019-01-21 下午 04:09
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :代理对象类（这里同时也是客户端）：代理Presenter的创建、销毁、绑定、解绑以及Presenter的状态保存,其实就是管理Presenter的生命周期
 */
public abstract class BaseProxyUi<V extends BaseView, P extends BasePresenter<V>> extends BaseActivity implements PresenterProxy<V, P>, BaseView {

    private static final String PRESENTER_SAVE_KEY = "presenter_save_key";
    private PresenterProxyImpl<V, P> mProxy = new PresenterProxyImpl<>();
    private P presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
        createPresenterFactory(PresenterFactoryImpl.createFactory(getClass()));
        presenter = createPresenter((V) this);
        presenter.setLifecycleOwner(this);
        getLifecycle().addObserver(presenter);
        initData(savedInstanceState);
        initView(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyPresenter();
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(PRESENTER_SAVE_KEY, onSaveInstanceState());
    }

    @Override
    public void createPresenterFactory(PresenterFactory<P> presenterFactory) {
        mProxy.createPresenterFactory(presenterFactory);
    }

    @Override
    public PresenterFactory<P> getPresenterFactory() {
        return mProxy.getPresenterFactory();
    }

    @Override
    public P createPresenter(V v) {
        return mProxy.createPresenter(v);
    }

    @Override
    public P getPresenter() {
        return mProxy.getPresenter();
    }

    @Override
    public void destroyPresenter() {
        mProxy.destroyPresenter();
    }

    @Override
    public Bundle onSaveInstanceState() {
        return mProxy.onSaveInstanceState();
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        mProxy.onRestoreInstanceState(savedInstanceState == null ? null : savedInstanceState.getBundle(PRESENTER_SAVE_KEY));
    }
}
