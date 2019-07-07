package com.bracks.wanandroid.presenter;


import com.bracks.mylib.base.basemvp.BasePresenter;
import com.bracks.mylib.rx.RxDefaultObserver;
import com.bracks.wanandroid.contract.HomeFragContract;
import com.bracks.wanandroid.model.HomeFragM;
import com.bracks.wanandroid.model.bean.Banner;
import com.bracks.wanandroid.model.bean.HomeList;
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
    public void fetch() {
        if (getView() != null) {
            getView().showLoading("加载中", true);
            new HomeFragM()
                    .loadData()
                    .as(bindLifecycle())
                    .subscribe(new RxDefaultObserver<Result<?>>() {
                        @Override
                        public void onSuccess(Result<?> response) {
                            getView().hideLoading();
                            if (response instanceof Banner) {
                                getView().showBanner(((Banner) response).getData());
                            } else if (response instanceof HomeList) {
                                getView().showDatas(((HomeList) response).getData().getDatas());
                            }
                        }
                    });
        }
    }
}
