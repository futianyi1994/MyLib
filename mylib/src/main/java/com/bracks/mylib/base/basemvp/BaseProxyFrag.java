package com.bracks.mylib.base.basemvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bracks.mylib.base.BaseFragment;

import butterknife.ButterKnife;

/**
 * good programmer.
 *
 * @date : 2019-01-25 上午 11:10
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :使用代理模式来代理Presenter的创建、销毁、绑定、解绑以及Presenter的状态保存,其实就是管理Presenter的生命周期
 */
public abstract class BaseProxyFrag<V extends BaseView, P extends BasePresenter<V>> extends BaseFragment implements PresenterProxy, BaseView {

    private static final String PRESENTER_SAVE_KEY = "presenter_save_key";

    /**
     * 创建被代理对象,传入默认Presenter的工厂
     */
    private PresenterProxyImpl<V, P> mProxy = new PresenterProxyImpl<>(getClass());

    private P presenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mProxy.onRestoreInstanceState(savedInstanceState.getBundle(PRESENTER_SAVE_KEY));
        }
        initData(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
            mUnbinder = ButterKnife.bind(this, rootView);
            rootView.setOnTouchListener(this);
            mProxy.onCreate((V) this);
            presenter = mProxy.getPresenter();
            presenter.setLifecycleOwner(this);
            getLifecycle().addObserver(presenter);
            initView(rootView, savedInstanceState);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mProxy.onDestroy();
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
    }

    /**
     * 意外销毁的时候调用
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
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
