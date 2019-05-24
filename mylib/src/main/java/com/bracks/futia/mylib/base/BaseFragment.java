package com.bracks.futia.mylib.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.bracks.futia.mylib.base.interf.BaseFragInterf;
import com.bracks.futia.mylib.rx.RxAppFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * good programmer.
 *
 * @date : 2019-01-28 下午 03:05
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :fragment基类
 */
public abstract class BaseFragment extends RxAppFragment implements BaseFragInterf, View.OnTouchListener {

    protected Unbinder mUnbinder;
    /**
     * 布局
     */
    private View rootView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
            mUnbinder = ButterKnife.bind(this, rootView);
            rootView.setOnTouchListener(this);
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
        mUnbinder.unbind();
    }

    /**
     * 防止点击时间透传到上一个fragment,在onCreateView()中给rootView设置onTouchListener
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }
}
