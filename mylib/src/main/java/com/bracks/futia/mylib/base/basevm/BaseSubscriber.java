package com.bracks.futia.mylib.base.basevm;

import com.blankj.utilcode.util.ToastUtils;
import com.bracks.futia.mylib.net.https.HttpCallback;

import io.reactivex.observers.DisposableObserver;

/**
 * good programmer.
 *
 * @date : 2019-02-15 上午 11:19
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class BaseSubscriber<T> extends DisposableObserver<T> {

    private BaseViewModel baseViewModel;
    private HttpCallback<T> requestCallback;

    public BaseSubscriber(BaseViewModel baseViewModel, HttpCallback<T> requestCallback) {
        this.baseViewModel = baseViewModel;
        this.requestCallback = requestCallback;
    }

    @Override
    public void onNext(T t) {
        if (requestCallback != null) {
            requestCallback.onSuccess(t);
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (baseViewModel == null) {
            ToastUtils.showLong(e.getMessage());
        } else {
            baseViewModel.showToast(e.getMessage());
        }
        requestCallback.onFailed(e);
    }

    @Override
    public void onComplete() {
        requestCallback.onComplete();
    }

}