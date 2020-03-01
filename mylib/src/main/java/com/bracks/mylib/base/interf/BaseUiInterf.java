package com.bracks.mylib.base.interf;

import android.os.Bundle;
import android.support.annotation.NonNull;

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

    void initData(@NonNull Bundle savedInstanceState);

    void initView(@NonNull Bundle savedInstanceState);
}
