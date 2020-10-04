package com.bracks.mylib.base.basemvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bracks.mylib.base.BaseFragment;
import com.bracks.mylib.base.interf.BaseView;

import butterknife.ButterKnife;

/**
 * good programmer.
 *
 * @date : 2019-01-25 上午 11:10
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :代理对象类（这里同时也是客户端）：代理Presenter的创建、销毁、绑定、解绑以及Presenter的状态保存,其实就是管理Presenter的生命周期
 */
public abstract class BaseProxyFrag<V extends BaseView, P extends BasePresenter<V>> extends BaseFragment implements PresenterProxy<V, P>, BaseView {

    private static final String PRESENTER_SAVE_KEY = "presenter_save_key";
    private PresenterProxyImpl<V, P> mProxy = new PresenterProxyImpl<>();
    private P presenter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
        initData(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
            mUnbinder = ButterKnife.bind(this, rootView);
            rootView.setOnTouchListener(this);
            createPresenterFactory(PresenterFactoryImpl.createFactory(getClass()));
            presenter = createPresenter((V) this);
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
    public void onDestroyView() {
        super.onDestroyView();
        destroyPresenter();
        rootView = null;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
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
