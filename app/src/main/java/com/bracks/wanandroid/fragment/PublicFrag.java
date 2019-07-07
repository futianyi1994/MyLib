package com.bracks.wanandroid.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.bracks.mylib.base.basemvp.BaseProxyFrag;
import com.bracks.mylib.base.basemvp.CreatePresenter;
import com.bracks.mylib.utils.bar.BarUtils;
import com.bracks.mylib.utils.widget.DialogUtils;
import com.bracks.wanandroid.R;
import com.bracks.wanandroid.adapter.PublicAdapter;
import com.bracks.wanandroid.contract.PublicFragContract;
import com.bracks.wanandroid.model.bean.PublicList;
import com.bracks.wanandroid.presenter.PublicFragP;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;

@CreatePresenter(PublicFragP.class)
public class PublicFrag extends BaseProxyFrag<PublicFragContract.View, PublicFragP> implements PublicFragContract.View {

    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Dialog dialog;

    public static PublicFrag newInstance() {
        Bundle args = new Bundle();
        PublicFrag fragment = new PublicFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_public;
    }

    @Override
    public void initView(View view, @Nullable Bundle savedInstanceState) {
        getPresenter().fetch();
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        collapsingToolbar.setTitle("公众号");
        collapsingToolbar.setExpandedTitleColor(Color.WHITE);
        collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
        refreshLayout.setEnableOverScrollDrag(true);
        refreshLayout.setOnRefreshListener(refreshLayout -> getPresenter().fetch());
    }

    @Override
    public void showDatas(List<PublicList.DataBean> data) {
        PublicAdapter adapter = new PublicAdapter();
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
}
