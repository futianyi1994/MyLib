package com.bracks.wanandroid.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.Utils;
import com.bracks.mylib.base.basemvp.BasePresenter;
import com.bracks.mylib.base.basemvp.CreatePresenter;
import com.bracks.mylib.base.basevm.LViewModelProviders;
import com.bracks.utils.widget.recycleView.SpaceItemDecoration;
import com.bracks.wanandroid.R;
import com.bracks.wanandroid.adapter.CollectAdapter;
import com.bracks.wanandroid.model.bean.Collect;
import com.bracks.wanandroid.viewmodel.CollectViewModel;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;


/**
 * good programmer.
 *
 * @date : 2019-06-13 下午 04:03
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
@CreatePresenter(BasePresenter.class)
public class CollectUi extends BaseUi {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private int page;
    private CollectAdapter adapter;
    private CollectViewModel viewModel;

    @Override
    public int getLayoutId() {
        return R.layout.activity_collect;
    }

    @Override
    protected boolean isTransparencyBar() {
        BarUtils.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimaryDark));
        return false;
    }

    @Override
    protected ViewModel initViewModel() {
        showLoading("加载中", true);
        viewModel = LViewModelProviders.of(this, CollectViewModel.class);
        viewModel
                .getCollectLiveData()
                .observe(this, datasBeans -> {
                    if (page == 0) {
                        showDatas(datasBeans);
                    } else {
                        loadMore(datasBeans);
                    }
                });
        viewModel.quertCollect(page);
        return viewModel;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        toolbar.setTitle("收藏");
        setSupportActionBar(toolbar);
        //设置是否显示返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
        recyclerView.addItemDecoration(new SpaceItemDecoration(ConvertUtils.dp2px(10)));
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            page = 0;
            viewModel.quertCollect(page);
        });
    }

    public void showDatas(List<Collect.DataBean.DatasBean> data) {
        if (adapter == null) {
            adapter = new CollectAdapter(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(Utils.getApp()));
            recyclerView.setAdapter(adapter);
        }
        adapter.clear();
        adapter.addAll(data);
        refreshLayout.finishRefresh();

        if (data.get(0).isOver()) {
            refreshLayout.setEnableOverScrollDrag(true);
            refreshLayout.setEnableLoadMore(false);
        } else {
            refreshLayout.setOnLoadMoreListener(refreshLayout -> {
                page++;
                viewModel.quertCollect(page);
            });
        }
    }

    public void loadMore(List<Collect.DataBean.DatasBean> data) {
        adapter.addAll(data);
        refreshLayout.finishLoadMore();
    }
}
