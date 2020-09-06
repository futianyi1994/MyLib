package com.bracks.mylib.base.basevm;

import com.bracks.mylib.base.model.Result;
import com.bracks.mylib.exception.ApiException;
import com.bracks.mylib.net.https.HttpCallback;
import com.bracks.mylib.rx.RxAutoDispose;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * good programmer.
 *
 * @date : 2019-02-15 上午 11:19
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :BaseRemoteDataSource的定位是将之当成一个接口实现者，即在RemoteDataSource中实际调用各个请求接口，并通过RxJava来控制loading弹出以及销毁的时机
 * 一般而言，BaseRemoteDataSource的实现类中声明的是具有相关逻辑的接口。例如，对于登录模块，可声明一个LoginDataSource
 */
public abstract class BaseRemoteDataSource implements BaseDataSource {

    protected CompositeDisposable compositeDisposable;

    protected BaseViewModel baseViewModel;

    public BaseRemoteDataSource(BaseViewModel baseViewModel) {
        this.compositeDisposable = new CompositeDisposable();
        this.baseViewModel = baseViewModel;
    }


    protected <T> void execute(Observable<? extends Result<T>> observable, HttpCallback<T> callback) {
        execute(observable, new BaseSubscriber<>(baseViewModel, callback), true, true);
    }

    protected <T> void executeWithoutShow(Observable<? extends Result<T>> observable, HttpCallback<T> callback) {
        execute(observable, new BaseSubscriber<>(baseViewModel, callback), false, true);
    }

    protected <T> void executeWithoutDismiss(Observable<? extends Result<T>> observable, HttpCallback<T> callback) {
        execute(observable, new BaseSubscriber<>(baseViewModel, callback), true, false);
    }

    protected <T> void execute(Observable<? extends Result<T>> observable, Observer<T> observer, boolean isShow, boolean isDismiss) {
        Observable<T> compose = observable
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(applySchedulers());
        addDisposable(
                isShow
                        ?
                        (Disposable) compose
                                .compose(isDismiss ? loadingTransformer() : loadingTransformerWithoutDismiss())
                                .as(RxAutoDispose.bindLifecycle(baseViewModel.lifecycleOwner))
                                .subscribeWith(observer)
                        :
                        (Disposable) compose
                                .as(RxAutoDispose.bindLifecycle(baseViewModel.lifecycleOwner))
                                .subscribeWith(observer));
    }

    private <T> ObservableTransformer<Result<T>, T> applySchedulers() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap((Function<Result<T>, ObservableSource<? extends T>>) result -> {
                    if (result.ok()) {
                        return Observable.create((ObservableOnSubscribe<T>) emitter -> {
                            try {
                                emitter.onNext(result.getData());
                                emitter.onComplete();
                            } catch (Exception e) {
                                emitter.onError(e);
                            }
                        });
                    } else {
                        throw new ApiException(result.getCode(), result.getMsg());
                    }
                });
    }

    private <T> ObservableTransformer<T, T> loadingTransformer() {
        return observable -> observable
                .subscribeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> startLoading())
                .doFinally(this::dismissLoading);
    }

    private <T> ObservableTransformer<T, T> loadingTransformerWithoutDismiss() {
        return observable -> observable
                .subscribeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> startLoading());
    }

    private void startLoading() {
        if (baseViewModel != null) {
            baseViewModel.startLoading();
        }
    }

    private void dismissLoading() {
        if (baseViewModel != null) {
            baseViewModel.dismissLoading();
        }
    }

    public void addDisposable(Disposable disposable) {
        if (compositeDisposable.isDisposed()) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    public void dispose() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }
}