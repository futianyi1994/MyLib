package com.bracks.wanandroid.model;

import com.bracks.mylib.base.basemvp.BaseModel;
import com.bracks.mylib.rx.RxSchedulersCompat;
import com.bracks.wanandroid.model.bean.PublicList;
import com.bracks.wanandroid.net.ApiService;

import io.reactivex.Observable;


/**
 * good programmer.
 *
 * @date : 2019-01-21 下午 04:09
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class PublicFragM implements BaseModel<PublicList> {

    @Override
    public Observable<PublicList> loadData() {
        return ApiService
                .getService()
                .getPublicList()
                .compose(RxSchedulersCompat.ioSchedulerObser());
    }
}
