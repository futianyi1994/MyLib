package com.bracks.wanandroid.fragment;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.bracks.mylib.base.basemvp.BaseProxyFrag;
import com.bracks.mylib.base.basemvp.CreatePresenter;
import com.bracks.mylib.utils.bar.BarUtils;
import com.bracks.mylib.utils.widget.DialogUtils;
import com.bracks.wanandroid.R;
import com.bracks.wanandroid.contract.PublicFragContract;
import com.bracks.wanandroid.model.bean.PublicList;
import com.bracks.wanandroid.presenter.PublicFragP;

import java.util.List;

@CreatePresenter(PublicFragP.class)
public class MyFrag extends BaseProxyFrag<PublicFragContract.View, PublicFragP> implements PublicFragContract.View {


    private Dialog dialog;

    public static MyFrag newInstance() {
        Bundle args = new Bundle();
        MyFrag fragment = new MyFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    public void initView(View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void showDatas(List<PublicList.DataBean> data) {
    }

    @Override
    public void showLoading(String msg, boolean isCancelable) {
        dialog = DialogUtils.createLoadingDialog(getActivity(), msg, isCancelable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            DialogUtils.afterShow(() -> BarUtils.hideNavBar(dialog.getWindow().getDecorView()));
        } else {
            dialog.show();
        }
    }

    @Override
    public void hideLoading() {
        DialogUtils.dismissDialog(dialog);
    }

    @Override
    public void showToast(String msg) {
        ToastUtils.showLong(msg);
    }
}
