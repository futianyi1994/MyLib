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
 * @description :
 */
public abstract class BaseUi<V extends BaseView, P extends BasePresenter<V>> extends BaseActivity implements BaseView {

    private P presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //创建Presenter
        if (presenter == null) {
            presenter = creatPresenter();
            presenter.setLifecycleOwner(this);
            getLifecycle().addObserver(presenter);
        }

        if (presenter == null) {
            throw new NullPointerException("presenter 不能为空!");
        }
        presenter.onAttchView((V) this);
        initData(savedInstanceState);
        initView(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDetachView();
        }
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
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
