package com.bracks.mylib.base.basemvp;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bracks.mylib.base.BaseFragment;

/**
 * good programmer.
 *
 * @date : 2019-01-25 上午 11:10
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public abstract class BaseFrag<V extends BaseView, P extends BasePresenter<V>> extends BaseFragment implements BaseView {

    private P presenter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //创建Presenter
        if (presenter == null) {
            presenter = creatPresenter();
            if (presenter == null) {
                presenter = (P) new BasePresenter<V>();
            }
        }
        presenter.onAttchView((V) this);
        presenter.setLifecycleOwner(this);
        getLifecycle().addObserver(presenter);
        initData(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDetachView();
        }
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
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
