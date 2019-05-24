package com.bracks.futia.mylib.base.interf;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;


/**
 * good programmer.
 *
 * @date : 2019-01-24 下午 02:43
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :基类fragment实现接口
 */
public interface BaseFragInterf {
    int getLayoutId();

    void initData(@Nullable Bundle savedInstanceState);

    void initView(View view, @Nullable Bundle savedInstanceState);
}
