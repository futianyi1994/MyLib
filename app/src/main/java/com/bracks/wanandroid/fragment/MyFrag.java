package com.bracks.wanandroid.fragment;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bracks.mylib.base.basemvp.BaseProxyFrag;
import com.bracks.mylib.base.basemvp.CreatePresenter;
import com.bracks.mylib.utils.bar.BarUtils;
import com.bracks.mylib.utils.widget.DialogUtils;
import com.bracks.wanandroid.R;
import com.bracks.wanandroid.adapter.MyAdapter;
import com.bracks.wanandroid.contract.MyFragContract;
import com.bracks.wanandroid.presenter.MyP;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@CreatePresenter(MyP.class)
public class MyFrag extends BaseProxyFrag<MyFragContract.View, MyP> implements MyFragContract.View {


    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;


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
        getPresenter().fetch();
        refreshLayout.setEnableOverScrollDrag(true);
        refreshLayout.setOnRefreshListener(refreshLayout -> getPresenter().fetch());
    }

    @Override
    public void showDatas(List<String> data) {
        MyAdapter adapter = new MyAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setData(data);
        recyclerView.setAdapter(adapter);
        refreshLayout.finishRefresh();
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

    @OnClick({R.id.tvUserName, R.id.image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvUserName:
                break;
            case R.id.image:
                break;
            default:
                break;
        }
    }
}
