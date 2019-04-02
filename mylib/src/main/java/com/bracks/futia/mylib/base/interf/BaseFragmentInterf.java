package com.bracks.futia.mylib.base.interf;

import android.view.View;


/**
 * good programmer.
 *
 * @date : 2019-01-24 下午 02:43
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :基类fragment实现接口
 */
public interface BaseFragmentInterf {
    int getLayoutId();

    void initData();

    void initView(View view);
}
