package com.bracks.wanandroid.presenter;

import android.support.v4.app.FragmentActivity;

import com.bracks.mylib.base.basemvp.BasePresenter;
import com.bracks.mylib.base.basevm.LViewModelProviders;
import com.bracks.wanandroid.contract.HistoryContract;
import com.bracks.wanandroid.viewmodel.HistoryViewModel;

/**
 * good programmer.
 *
 * @date : 2019-02-18 下午 05:33
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class HistoryP extends BasePresenter<HistoryContract.View> implements HistoryContract.Presenter {
    private HistoryViewModel viewModel;
    private int mPage;

    @Override
    public HistoryViewModel fetch(FragmentActivity activity, int id, int page, String search) {
        mPage = page;
        if (getView() != null) {
            if (viewModel == null) {
                getView().showLoading("加载中", true);
                viewModel = LViewModelProviders.of(activity, HistoryViewModel.class);
                viewModel
                        .getHistoryLiveData()
                        .observe(activity, datasBeans -> {
                            if (mPage == 1) {
                                getView().showDatas(datasBeans);
                            } else {
                                getView().loadMore(datasBeans);
                            }
                        });
            }
            viewModel.quertHistory(id, mPage, search);
        }
        return viewModel;
    }
}
