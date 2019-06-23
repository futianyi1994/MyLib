package com.bracks.wanandroid.presenter;

import android.support.v4.app.FragmentActivity;

import com.bracks.mylib.base.basemvp.BasePresenter;
import com.bracks.mylib.base.basevm.LViewModelProviders;
import com.bracks.wanandroid.contract.CollectContract;
import com.bracks.wanandroid.viewmodel.CollectViewModel;

/**
 * good programmer.
 *
 * @date : 2019-02-18 下午 05:33
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class CollectP extends BasePresenter<CollectContract.View> implements CollectContract.Presenter {
    private CollectViewModel viewModel;
    private int mPage;

    @Override
    public CollectViewModel fetch(FragmentActivity activity, int page) {
        mPage = page;
        if (getView() != null) {
            if (viewModel == null) {
                getView().showLoading("加载中", true);
                viewModel = LViewModelProviders.of(activity, CollectViewModel.class);
                viewModel
                        .getCollectLiveData()
                        .observe(activity, datasBeans -> {
                            if (mPage == 0) {
                                getView().showDatas(datasBeans);
                            } else {
                                getView().loadMore(datasBeans);
                            }
                        });
            }
            viewModel.quertCollect(mPage);
        }
        return viewModel;
    }
}
