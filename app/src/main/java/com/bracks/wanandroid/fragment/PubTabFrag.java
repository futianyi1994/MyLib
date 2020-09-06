package com.bracks.wanandroid.fragment;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bracks.mylib.base.basemvp.BasePresenter;
import com.bracks.mylib.base.basemvp.BaseView;
import com.bracks.mylib.base.basemvp.CreatePresenter;
import com.bracks.mylib.base.basevm.BaseVmProxyFrag;
import com.bracks.mylib.base.basevm.LViewModelProviders;
import com.bracks.mylib.rx.RxAutoDispose;
import com.bracks.mylib.rx.RxBus;
import com.bracks.utils.widget.recycleView.SpaceItemDecoration;
import com.bracks.wanandroid.R;
import com.bracks.wanandroid.adapter.ChapterAdapter;
import com.bracks.wanandroid.model.bean.Chapter;
import com.bracks.wanandroid.model.evenbus.QueryEvent;
import com.bracks.wanandroid.model.evenbus.ScrollEvent;
import com.bracks.wanandroid.viewmodel.HistoryViewModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

import butterknife.BindView;

/**
 * good programmer.
 *
 * @date : 2019-07-27 上午 10:50
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
@CreatePresenter(BasePresenter.class)
public class PubTabFrag extends BaseVmProxyFrag<BaseView, BasePresenter<BaseView>> {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    private HistoryViewModel viewModel;
    private int id;
    private int page = 1;
    private ChapterAdapter adapter;


    public static PubTabFrag newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt("id", id);
        PubTabFrag fragment = new PubTabFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected ViewModel initViewModel() {
        viewModel = LViewModelProviders.of(this, HistoryViewModel.class);
        viewModel
                .getHistoryLiveData()
                .observe(this, datasBeans -> {
                    if (page == 1) {
                        showDatas(datasBeans);
                    } else {
                        loadMore(datasBeans);
                    }
                });
        viewModel.queryHistory(id, page, PubFrag.search);
        RxBus
                .getDefault()
                .toObservable(QueryEvent.class)
                .as(RxAutoDispose.bindLifecycle(this, Lifecycle.Event.ON_DESTROY))
                .subscribe(queryEvent -> viewModel.queryHistory(id, page = 1, PubFrag.search));
        return viewModel;
    }

    @Override
    public void showToast(String msg) {
        ToastUtils.showLong(msg);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_pubtab;
    }

    @Override
    public void initView(View view, @Nullable Bundle savedInstanceState) {
        id = getArguments().getInt("id");
        recyclerView.addItemDecoration(new SpaceItemDecoration(ConvertUtils.dp2px(10)));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RxBus
                        .getDefault()
                        .post(new ScrollEvent(dy));
            }
        });
        adapter = new ChapterAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                viewModel.queryHistory(id, page, PubFrag.search);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                viewModel.queryHistory(id, page, PubFrag.search);
            }
        });
    }

    public void showDatas(List<Chapter.DataBean.DatasBean> data) {
        adapter.setData(data);
        refreshLayout.finishRefresh();
    }

    public void loadMore(List<Chapter.DataBean.DatasBean> data) {
        adapter.addAll(data);
        refreshLayout.finishLoadMore();
    }
}
