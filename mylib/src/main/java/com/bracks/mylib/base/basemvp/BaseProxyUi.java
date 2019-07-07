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
 * @description : 代理对象：使用代理模式来代理Presenter的创建、销毁、绑定、解绑以及Presenter的状态保存,其实就是管理Presenter的生命周期
 */
public abstract class BaseProxyUi<V extends BaseView, P extends BasePresenter<V>> extends BaseActivity implements PresenterProxy, BaseView {

    private static final String PRESENTER_SAVE_KEY = "presenter_save_key";
    /**
     * 创建被代理对象,传入默认Presenter的工厂
     */
    private PresenterProxyImpl<V, P> mProxy = new PresenterProxyImpl<>(getClass());

    private P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mProxy.onRestoreInstanceState(savedInstanceState.getBundle(PRESENTER_SAVE_KEY));
        }
        mProxy.onCreate((V) this);
        presenter = mProxy.getPresenter();
        presenter.setLifecycleOwner(this);
        getLifecycle().addObserver(presenter);
        initData(savedInstanceState);
        initView(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProxy.onDestroy();
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
    }

    /**
     * 意外销毁的时候调用
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(PRESENTER_SAVE_KEY, mProxy.onSaveInstanceState());
    }

    /**
     * 可以在子类中重写自己的Presenter工厂覆盖默认的，如果你想自定义的话（注意需要在getPresenter()调用前设置）
     *
     * @param presenterFactory PresenterFactory类型
     */
    @Override
    public void setPresenterFactory(PresenterFactory presenterFactory) {
        mProxy.setPresenterFactory(presenterFactory);
    }

    /**
     * 得到Presenter工厂
     *
     * @return
     */
    @Override
    public PresenterFactory<V, P> getPresenterFactory() {
        return mProxy.getPresenterFactory();
    }

    /**
     * 获取Presenter
     *
     * @return 返回子类创建的Presenter
     */
    @Override
    public P getPresenter() {
        return presenter;
    }
}
