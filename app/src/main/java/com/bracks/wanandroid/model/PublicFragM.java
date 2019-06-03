package com.bracks.wanandroid.model;

import com.bracks.mylib.base.basemvp.BaseModel;
import com.bracks.mylib.net.https.HttpCallback;
import com.bracks.mylib.rx.RxDefaultObserver;
import com.bracks.mylib.rx.RxSchedulersCompat;
import com.bracks.wanandroid.model.bean.PublicList;
import com.bracks.wanandroid.net.ApiService;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.util.List;

import io.reactivex.schedulers.Schedulers;


/**
 * good programmer.
 *
 * @date : 2019-01-21 下午 04:09
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class PublicFragM implements BaseModel<List<PublicList.DataBean>> {

    @Override
    public <E> void loadData(LifecycleProvider<E> lifecycleProvider, HttpCallback<List<PublicList.DataBean>> listener) {
        ApiService
                .getService()
                .getPublicList()
                //.delay(1_000, TimeUnit.MILLISECONDS)
                .compose(RxSchedulersCompat.applyObservSchedulers(Schedulers.io()))
                .compose(lifecycleProvider.bindToLifecycle())
                .subscribe(new RxDefaultObserver<PublicList>() {
                    @Override
                    public void onSuccess(PublicList response) {
                        listener.onSuccess(response.getData());
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        listener.onComplete();
                    }
                });
    }
}
