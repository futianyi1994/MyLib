package com.bracks.mylib.base.interf;

import android.os.Bundle;
import androidx.annotation.Nullable;

/**
 * good programmer.
 *
 * @date : 2019-01-24 下午 02:42
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public interface BaseUiInterf {
    int getLayoutId();

    void initData(@Nullable Bundle savedInstanceState);

    void initView(@Nullable Bundle savedInstanceState);
}
