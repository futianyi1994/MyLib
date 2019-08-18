package com.bracks.wanandroid.activity;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.bracks.mylib.base.basemvp.BasePresenter;
import com.bracks.mylib.base.basemvp.BaseView;
import com.bracks.mylib.base.basevm.BaseVmProxyUi;
import com.bracks.mylib.utils.CommonUtils;
import com.bracks.mylib.utils.bar.BarUtils;

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
    public void onBackPressed() {
        if (isTaskRoot()) {
            CommonUtils.exitBy2Click();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void showToast(String msg) {
        ToastUtils.showLong(msg);
    }
}
