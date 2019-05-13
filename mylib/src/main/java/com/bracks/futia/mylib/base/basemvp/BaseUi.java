package com.bracks.futia.mylib.base.basemvp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.blankj.utilcode.util.KeyboardUtils;
import com.bracks.futia.mylib.R;
import com.bracks.futia.mylib.base.interf.BaseViewInterf;
import com.bracks.futia.mylib.internationalization.Language;
import com.bracks.futia.mylib.internationalization.MyContextWrapper;
import com.bracks.futia.mylib.rx.RxAppActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * good programmer.
 *
 * @date : 2019-01-21 下午 04:09
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public abstract class BaseUi<V extends BaseView, P extends BasePresenter<V>> extends RxAppActivity implements BaseViewInterf, BaseView {

    protected Unbinder mUnbinder;
    private P presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutId() != 0) {
            setBackgroundColor(R.color.common_item_gray_bg);
            setContentView(getLayoutId());
        }
        //通过注解绑定控件
        mUnbinder = ButterKnife.bind(this);
        initInstanceState(savedInstanceState);
        if (isTransparencyBar()) {
            com.blankj.utilcode.util.BarUtils.setStatusBarColor(this, Color.alpha(0));
        }
        if (isLightBarMode()) {
            com.bracks.futia.mylib.utils.bar.BarUtils.setLightStatusBar(this, true);
        }
        //创建Presenter
        if (presenter == null) {
            presenter = creatPresenter();
        }

        if (presenter == null) {
            throw new NullPointerException("presenter 不能为空!");
        }
        presenter.onAttchView((V) this);
        initData();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyboardUtils.hideSoftInput(this);
        mUnbinder.unbind();
        if (presenter != null) {
            presenter.onDetachView();
        }
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

    @Override
    public void initView() {
    }

    /**
     * 设置背景颜色
     *
     * @param color
     */
    protected void setBackgroundColor(@ColorRes int color) {
        this.getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, color));
    }

    /**
     * 设置背景资源
     */
    protected void setBackgroundResource(@DrawableRes int resid) {
        this.getWindow().getDecorView().setBackgroundResource(resid);
    }

    /**
     * 对savedInstanceState进行判断
     *
     * @param savedInstanceState
     */
    protected void initInstanceState(@Nullable Bundle savedInstanceState) {
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

    /**
     * 创建Presenter
     *
     * @return
     */
    protected abstract P creatPresenter();

    /**
     * 获取Presenter
     *
     * @return 返回子类创建的Presenter
     */
    public P getPresenter() {
        return presenter;
    }
}
