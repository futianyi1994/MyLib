package com.bracks.futia.mylib.base.basemvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.blankj.utilcode.util.KeyboardUtils;
import com.bracks.futia.mylib.AppManager;
import com.bracks.futia.mylib.R;
import com.bracks.futia.mylib.base.interf.BaseViewInterf;
import com.bracks.futia.mylib.internationalization.Language;
import com.bracks.futia.mylib.internationalization.MyContextWrapper;
import com.bracks.futia.mylib.rx.RxAppActivity;
import com.bracks.futia.mylib.utils.statusbar.StatusBarUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * good programmer.
 *
 * @date : 2019-01-21 下午 04:09
 * @author: futia
 * @email : futianyi1994@126.com
 * @description : 代理对象：使用代理模式来代理Presenter的创建、销毁、绑定、解绑以及Presenter的状态保存,其实就是管理Presenter的生命周期
 */
public abstract class BaseProxyUi<V extends BaseView, P extends BasePresenter<V>> extends RxAppActivity implements BaseViewInterf, PresenterProxy, BaseView {

    protected Unbinder mUnbinder;

    private static final String PRESENTER_SAVE_KEY = "presenter_save_key";
    /**
     * 创建被代理对象,传入默认Presenter的工厂
     */
    private PresenterProxyImpl<V, P> mProxy = new PresenterProxyImpl<>(getClass());


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        if (isTransparencyBar()) {
            StatusBarUtils.transparencyBar(this);
        }
        if (isLightBarMode()) {
            StatusBarUtils.setLightStatusBar(this, true);
        }
        if (savedInstanceState != null) {
            mProxy.onRestoreInstanceState(savedInstanceState.getBundle(PRESENTER_SAVE_KEY));
        }
        if (getLayoutId() != 0) {
            setActivityBackground(R.color.common_item_gray_bg);
            setContentView(getLayoutId());
        }
        //通过注解绑定控件
        mUnbinder = ButterKnife.bind(this);

        mProxy.onCreate((V) this);
        initData();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyboardUtils.hideSoftInput(this);
        mUnbinder.unbind();
        mProxy.onDestroy();
    }

    /**
     * attachBaseContext的实现App实现国际化
     *
     * @param newBase
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        String language = Language.getInstance(newBase).language();
        super.attachBaseContext(MyContextWrapper.wrap(newBase, language));
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
        return mProxy.getPresenter();
    }

    @Override
    public void initView() {
    }

    /**
     * 设置背景
     */
    protected void setActivityBackground(@ColorRes int id) {
        //getDecorView 获得window最顶层的View
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(ContextCompat.getColor(this, id));
    }

    /**
     * 是否执行状态栏透明的操作，返回一个布尔值，默认是执行，
     * 可以在子类中重写不执行
     *
     * @return
     */
    protected boolean isTransparencyBar() {
        return true;
    }

    /**
     * 是否状态栏亮色的操作，返回一个布尔值，默认是不执行，
     * 可以在子类中重写不执行
     *
     * @return
     */
    protected boolean isLightBarMode() {
        return false;
    }
}
