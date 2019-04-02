package com.bracks.futia.mylib.base.basemvp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.bracks.futia.mylib.base.interf.BaseFragmentInterf;
import com.bracks.futia.mylib.rx.RxAppFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * good programmer.
 *
 * @date : 2019-01-25 上午 11:10
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :使用代理模式来代理Presenter的创建、销毁、绑定、解绑以及Presenter的状态保存,其实就是管理Presenter的生命周期
 */
public abstract class BaseProxyFrag<V extends BaseView, P extends BasePresenter<V>> extends RxAppFragment implements BaseFragmentInterf, PresenterProxy, BaseView, View.OnTouchListener {

    protected Unbinder mUnbinder;

    private static final String PRESENTER_SAVE_KEY = "presenter_save_key";

    /**
     * 创建被代理对象,传入默认Presenter的工厂
     */
    private PresenterProxyImpl<V, P> mProxy = new PresenterProxyImpl<>(getClass());
    /**
     * 布局
     */
    private View rootView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mProxy.onRestoreInstanceState(savedInstanceState.getBundle(PRESENTER_SAVE_KEY));
        }
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
            mUnbinder = ButterKnife.bind(this, rootView);
            rootView.setOnTouchListener(this);
            mProxy.onCreate((V) this);
            initView(rootView);
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
        mUnbinder.unbind();
        mProxy.onDestroy();
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
        return mProxy.getPresenter();
    }

    /**
     * 防止点击时间透传到上一个fragment,在onCreateView()中给rootView设置onTouchListener
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @Override
    public void initData() {
    }
}
