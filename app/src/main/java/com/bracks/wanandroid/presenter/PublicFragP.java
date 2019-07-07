package com.bracks.wanandroid.presenter;


import com.bracks.mylib.base.basemvp.BasePresenter;
import com.bracks.mylib.rx.RxDefaultObserver;
import com.bracks.wanandroid.contract.PublicFragContract;
import com.bracks.wanandroid.model.PublicFragM;
import com.bracks.wanandroid.model.bean.PublicList;


/**
 * good programmer.
 *
 * @date : 2019-01-21 下午 04:08
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class PublicFragP extends BasePresenter<PublicFragContract.View> implements PublicFragContract.Presenter {

    @Override
    public void fetch() {
        if (getView() != null) {
            getView().showLoading("加载中", true);
            new PublicFragM()
                    .loadData()
                    .as(bindLifecycle())
                    .subscribe(new RxDefaultObserver<PublicList>() {
                        @Override
                        public void onSuccess(PublicList response) {
                            getView().showDatas(response.getData());
                        }

                        @Override
                        public void onComplete() {
                            super.onComplete();
                            getView().hideLoading();
                        }
                    });
        }
    }
}
