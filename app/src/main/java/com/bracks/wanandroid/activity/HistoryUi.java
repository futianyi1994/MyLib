package com.bracks.wanandroid.activity;

import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import com.bracks.mylib.base.basemvp.CreatePresenter;
import com.bracks.wanandroid.R;
import com.bracks.wanandroid.adapter.HistoryAdapter;
import com.bracks.wanandroid.model.bean.History;
import com.bracks.wanandroid.presenter.HistoryP;
import com.bracks.wanandroid.viewiterf.HistoryV;
import com.bracks.wanandroid.viewmodel.HistoryViewModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

import butterknife.BindView;

import static com.bracks.mylib.utils.CommonUtils.getContext;

/**
 * good programmer.
 *
 * @date : 2019-02-18 下午 05:28
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
@CreatePresenter(HistoryP.class)
public class HistoryUi extends BaseUi<HistoryV, HistoryP> implements HistoryV {
    public static final String EXTRA_ID = "id";
    public static final String EXTRA_PAGE = "page";

    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private HistoryViewModel viewModel;
    private HistoryAdapter mAdapter;
    private int id;
    private int page;
    private String search = "";


    @Override
    protected ViewModel initViewModel() {
        return viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_history;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getIntExtra(EXTRA_ID, 0);
            page = intent.getIntExtra(EXTRA_PAGE, 0);
        }
        viewModel = getPresenter().fetch(this, id, page, search);
    }


    @Override
    public void showDatas(List<History.DataBean.DatasBean> data) {
        if (mAdapter == null) {
            mAdapter = new HistoryAdapter(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(mAdapter);
            refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    page++;
                    getPresenter().fetch(HistoryUi.this, id, page, search);
                }

                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    page = 1;
                    getPresenter().fetch(HistoryUi.this, id, page, search);
                }
            });
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    search = s;
                    getPresenter().fetch(HistoryUi.this, id, page, search);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search = s;
                    getPresenter().fetch(HistoryUi.this, id, page, search);
                    return false;
                }
            });
        }
        mAdapter.clear();
        mAdapter.addAll(data);
        refreshLayout.finishRefresh();
    }

    @Override
    public void loadMore(List<History.DataBean.DatasBean> data) {
        mAdapter.addAll(data);
        refreshLayout.finishLoadMore();
    }
}
