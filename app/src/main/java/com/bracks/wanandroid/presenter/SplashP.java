package com.bracks.wanandroid.presenter;

import com.bracks.mylib.base.basemvp.BasePresenter;
import com.bracks.wanandroid.contract.SplashContract;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * good programmer.
 *
 * @date : 2019-06-12 下午 06:02
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class SplashP extends BasePresenter<SplashContract.View> implements SplashContract.Presenter {
    @Override
    public void jumpToMain() {
        if (getView() != null) {
            Disposable disposable = Observable
                    .timer(2000, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(bindLifecycle())
                    .subscribe(aLong -> getView().jumpToMain());
        }
    }
}
