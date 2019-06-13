package com.bracks.wanandroid.activity;

import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bracks.wanandroid.R;

/**
 * good programmer.
 *
 * @date : 2019-06-13 下午 04:03
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class CollectUi extends BaseUi {
    @Override
    protected ViewModel initViewModel() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_collect;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }
}
