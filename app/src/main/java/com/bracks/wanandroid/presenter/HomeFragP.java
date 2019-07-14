package com.bracks.wanandroid.presenter;


import com.bracks.mylib.base.basemvp.BasePresenter;
import com.bracks.mylib.rx.RxDefaultObserver;
import com.bracks.wanandroid.contract.HomeFragContract;
import com.bracks.wanandroid.model.HomeFragM;
import com.bracks.wanandroid.model.bean.Banner;
import com.bracks.wanandroid.model.bean.Chapter;
import com.bracks.wanandroid.model.bean.Result;


/**
 * good programmer.
 *
 * @date : 2019-01-21 下午 04:08
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class HomeFragP extends BasePresenter<HomeFragContract.View> implements HomeFragContract.Presenter {

    @Override
    public void refresh() {
        if (getView() != null) {
            getView().showLoading("加载中", true);
            new HomeFragM()
                    .refresh()
                    .as(bindLifecycle())
                    .subscribe(new RxDefaultObserver<Result<?>>() {
                        @Override
                        public void onSuccess(Result<?> response) {
                            getView().hideLoading();
                            if (response instanceof Banner) {
                                getView().showBanner(((Banner) response).getData());
                            } else if (response instanceof Chapter) {
                                getView().onRefresh(((Chapter) response).getData().getDatas());
                            }
                        }
                    });
        }
    }

    @Override
    public void loadMore(int page) {
        if (getView() != null) {
            new HomeFragM()
                    .loadMore(page)
                    .as(bindLifecycle())
                    .subscribe(new RxDefaultObserver<Chapter>() {
                        @Override
                        public void onSuccess(Chapter chapter) {
                            getView().onLoadMore((chapter).getData().getDatas());
                        }
                    });
        }
    }
}
