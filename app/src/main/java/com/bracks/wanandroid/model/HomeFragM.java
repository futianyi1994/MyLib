package com.bracks.wanandroid.model;

import com.bracks.mylib.rx.RxSchedulersCompat;
import com.bracks.wanandroid.contract.HomeFragContract;
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
public class HomeFragM implements HomeFragContract.Model<Result<?>> {
    private int page;

    public HomeFragM() {
    }

    public HomeFragM(int page) {
        this.page = page;
    }


    @Override
    public Observable<Result<?>> loadData() {
        return Observable
                .mergeArrayDelayError(
                        ApiService.getService().banner(),
                        ApiService.getService().homeList(page)
                )
                .compose(RxSchedulersCompat.ioSchedulerObser())
                ;
    }
}
