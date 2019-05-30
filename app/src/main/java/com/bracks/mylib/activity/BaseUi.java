package com.bracks.mylib.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bracks.futia.mylib.base.basemvp.BasePresenter;
import com.bracks.futia.mylib.base.basemvp.BaseView;
import com.bracks.futia.mylib.base.basevm.BaseVmProxyUi;
import com.bracks.futia.mylib.utils.CommonUtils;
import com.bracks.futia.mylib.utils.bar.BarUtils;

/**
 * good programmer.
 *
 * @date : 2019-05-27 下午 03:01
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public abstract class BaseUi<V extends BaseView, P extends BasePresenter<V>> extends BaseVmProxyUi<V, P> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            BarUtils.hideNavBar(this);
        }
    }

    @Override
    public void onBackPressedSupport() {
        if (isTaskRoot()) {
            CommonUtils.exitBy2Click();
        } else {
            super.onBackPressedSupport();
        }
    }
}
