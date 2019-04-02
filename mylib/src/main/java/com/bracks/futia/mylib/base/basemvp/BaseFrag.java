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
 * @description :
 */
public abstract class BaseFrag<V extends BaseView, P extends BasePresenter<V>> extends RxAppFragment implements BaseFragmentInterf, View.OnTouchListener, BaseView {

    protected Unbinder mUnbinder;
    private P presenter;
    /**
     * 布局
     */
    private View rootView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //创建Presenter
        if (presenter == null) {
            presenter = creatPresenter();
        }
        if (presenter == null) {
            throw new NullPointerException("presenter 不能为空!");
        }
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
            mUnbinder = ButterKnife.bind(this, rootView);
            rootView.setOnTouchListener(this);
            initInstanceState(savedInstanceState);
            presenter.onAttchView((V) this);
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
        if (presenter != null) {
            presenter.onDetachView();
        }
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

    /**
     * 对savedInstanceState进行判断
     *
     * @param savedInstanceState
     */
    protected void initInstanceState(Bundle savedInstanceState) {
    }

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
