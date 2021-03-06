package com.bracks.wanandroid.activity;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SnackbarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bracks.mylib.base.basevm.BaseVmProxyUi;
import com.bracks.mylib.utils.BarUtils;
import com.bracks.mylib.utils.CommonUtils;

/**
 * good programmer.
 *
 * @date : 2019-05-27 下午 03:01
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public abstract class BaseUi extends BaseVmProxyUi {
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
            SnackbarUtils
                    .with(getWindow().getDecorView())
                    .setMessage("再按一次退出程序")
                    .setAction("知道了", v -> {
                        CommonUtils.clearExitFlag();
                        SnackbarUtils.dismiss();
                    })
                    .show()
            ;
            CommonUtils.exitBy2ClickNoTip();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void showToast(String msg) {
        ToastUtils.showLong(msg);
    }
}
