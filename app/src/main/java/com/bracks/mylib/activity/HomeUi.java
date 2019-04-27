package com.bracks.mylib.activity;

import android.arch.lifecycle.ViewModel;
import android.os.Build;
import android.widget.Button;

import androidx.annotation.RequiresApi;

import com.bracks.futia.mylib.base.basemvp.BasePresenter;
import com.bracks.futia.mylib.base.basemvp.CreatePresenter;
import com.bracks.futia.mylib.base.basevm.BaseVmProxyUi;
import com.bracks.futia.mylib.utils.bar.BarUtils;
import com.bracks.futia.mylib.widget.CustomAlertDialog;
import com.bracks.mylib.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * good programmer.
 *
 * @date : 2019-04-27 下午 03:25
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
@CreatePresenter(BasePresenter.class)
public class HomeUi extends BaseVmProxyUi {
    @BindView(R.id.btnShowDialog)
    Button btnShowDialog;

    @Override
    protected ViewModel initViewModel() {
        return null;
    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    protected boolean isTransparencyBar() {
        return false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void initData() {
        BarUtils.hideNavBar(getWindow());
    }

    @OnClick(R.id.btnShowDialog)
    public void onViewClicked() {
        new CustomAlertDialog
                .Builder(this)
                .setMessage("nihao")
                .setDefaultPromptView2()
                .setAfterShowListener(dialog -> BarUtils.hideNavBar(dialog.getWindow().getDecorView()))
                .build();
    }
}
