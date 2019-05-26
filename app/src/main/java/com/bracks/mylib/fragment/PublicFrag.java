package com.bracks.mylib.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bracks.futia.mylib.base.basemvp.BaseProxyFrag;
import com.bracks.futia.mylib.base.basemvp.CreatePresenter;
import com.bracks.futia.mylib.utils.widget.DialogUtils;
import com.bracks.mylib.R;
import com.bracks.mylib.adapter.PublicAdapter;
import com.bracks.mylib.model.bean.PublicList;
import com.bracks.mylib.presenter.PublicFragP;
import com.bracks.mylib.viewiterf.PublicFragV;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;

@CreatePresenter(PublicFragP.class)
public class PublicFrag extends BaseProxyFrag<PublicFragV, PublicFragP> implements PublicFragV {

    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recycleView;

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
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        collapsingToolbar.setTitle("公众号");
        collapsingToolbar.setExpandedTitleColor(Color.WHITE);
        collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
        refreshLayout.setEnableOverScrollDrag(true);
        getPresenter().fetch(this);
    }

    @Override
    public void showDatas(List<PublicList.DataBean> data) {
        PublicAdapter mAdapter = new PublicAdapter();
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter.setData(data);
        recycleView.setAdapter(mAdapter);
    }

    @Override
    public void showLoading(String msg, boolean isCancelable) {
        dialog = DialogUtils.createLoadingDialog(getActivity(), msg, isCancelable);
        dialog.show();
    }

    @Override
    public void hideLoading() {
        DialogUtils.dismissDialog(dialog);
    }

    @Override
    public void showToast(String msg) {

    }
}
