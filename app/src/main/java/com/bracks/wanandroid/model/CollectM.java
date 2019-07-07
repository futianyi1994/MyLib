package com.bracks.wanandroid.model;

import com.bracks.mylib.base.basemvp.BaseModel;
import com.bracks.mylib.rx.RxSchedulersCompat;
import com.bracks.wanandroid.model.bean.Result;
import com.bracks.wanandroid.net.ApiService;

import io.reactivex.Observable;

/**
 * good programmer.
 *
 * @date : 2019-06-29 下午 12:05
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class CollectM implements BaseModel<Result> {
    private int id;
    private int originId;

    public CollectM(int id) {
        this.id = id;
    }

    public CollectM(int id, int originId) {
        this.id = id;
        this.originId = originId;
    }


    public Observable<Result<String>> cancelMyCollect() {
        return ApiService
                .getService()
                .cancelMyCollect(id, originId)
                .compose(RxSchedulersCompat.ioSchedulerObser())
                ;
    }

    public Observable<Result<String>> cancelCollect() {
        return ApiService
                .getService()
                .cancelCollect(id)
                .compose(RxSchedulersCompat.ioSchedulerObser())
                ;
    }

    @Override
    public Observable<Result> loadData() {
        return ApiService
                .getService()
                .collect(id)
                .compose(RxSchedulersCompat.ioSchedulerObser());
    }
}
