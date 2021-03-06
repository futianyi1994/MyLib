package com.bracks.wanandroid.fragment;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bracks.mylib.base.basemvp.BaseProxyFrag;
import com.bracks.mylib.base.basemvp.CreatePresenter;
import com.bracks.mylib.rx.RxBus;
import com.bracks.mylib.utils.BarUtils;
import com.bracks.mylib.utils.DialogUtils;
import com.bracks.utils.widget.recycleView.SpaceItemDecoration;
import com.bracks.wanandroid.R;
import com.bracks.wanandroid.adapter.ChapterAdapter;
import com.bracks.wanandroid.contract.HomeFragContract;
import com.bracks.wanandroid.model.bean.Banner;
import com.bracks.wanandroid.model.bean.Chapter;
import com.bracks.wanandroid.model.evenbus.ScrollEvent;
import com.bracks.wanandroid.presenter.HomeFragP;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@CreatePresenter(HomeFragP.class)
public class HomeFrag extends BaseProxyFrag<HomeFragContract.View, HomeFragP> implements HomeFragContract.View {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private Dialog dialog;
    private ChapterAdapter adapter;

    private int page;

    public static HomeFrag newInstance() {
        Bundle args = new Bundle();
        HomeFrag fragment = new HomeFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView(View view, @Nullable Bundle savedInstanceState) {
        adapter = new ChapterAdapter(getActivity());
        refreshLayout.setEnableOverScrollDrag(true);
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getPresenter().loadMore(page);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 0;
                getPresenter().refresh();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RxBus
                        .getDefault()
                        .post(new ScrollEvent(dy));
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(ConvertUtils.dp2px(10), 1, SpaceItemDecoration.LINEARLAYOUT));
        getPresenter().refresh();
    }

    @Override
    public void showBanner(List<Banner.DataBean> data) {
        //获取数据时已经处理首先获取banner数据所以这里不需要做判断
        adapter.setBannerData(data);
    }

    @Override
    public void onRefresh(List<Chapter.DataBean.DatasBean> data) {
        adapter.setData(data);
        recyclerView.setAdapter(adapter);
        refreshLayout.finishRefresh();
    }

    @Override
    public void onLoadMore(List<Chapter.DataBean.DatasBean> data) {
        adapter.addAll(data);
        refreshLayout.finishLoadMore();
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

    @OnClick({R.id.fab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fab:
                recyclerView.smoothScrollToPosition(0);
                break;
            default:
                break;
        }
    }
}
