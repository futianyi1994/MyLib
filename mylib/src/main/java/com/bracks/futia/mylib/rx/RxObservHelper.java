package com.bracks.futia.mylib.rx;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.bracks.futia.mylib.utils.widget.DialogUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.schedulers.Schedulers;

/**
 * good programmer.
 *
 * @date : 2018-09-02 下午 04:49
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class RxObservHelper {

    public static boolean isShowLoading(String page){
        if ("1".equals(page)){
            return true;
        }else {
            return false;
        }
    }
    public static boolean isShowLoading(int page){
        if (page == 1){
            return true;
        }else {
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isDestroy(RxAppActivity activity){
        return activity == null || activity.isDestroyed() || activity.isFinishing();
    }
    public static boolean isDestroy(RxAppFragment fragment){
        return fragment.isDetached();
    }

    public static <T> ObservableTransformer<T, T> applyProgressBar(@NonNull final RxAppActivity activity,
                                                                   final boolean isShowLoading) {
        return applyProgressBar(activity, isShowLoading, null);
    }

    public static <T> ObservableTransformer<T, T> applyProgressBar(@NonNull final RxAppActivity activity,
                                                                   final boolean isShowLoading,
                                                                   final DialogInterface.OnDismissListener listener) {
        return applyProgressBar(activity, isShowLoading, listener,true);
    }

    public static <T> ObservableTransformer<T, T> applyProgressBar(@NonNull final RxAppActivity activity,
                                                                   final boolean isShowLoading,
                                                                   final DialogInterface.OnDismissListener listener,
                                                                   final boolean dialogCancelable) {
        return new ObservableTransformer<T, T>() {

            Dialog dialog;

            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (isShowLoading) {
                                    dialog = DialogUtils.createLoadingDialog(activity, "加载中", dialogCancelable);
                                    dialog.show();
                                    if (listener != null){
                                        dialog.setOnDismissListener(listener);
                                    }
                                }
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .doOnTerminate(new Action() {
                            @Override
                            public void run() throws Exception {
                                if (isShowLoading) {
                                    DialogUtils.dismissDialog(dialog);
                                }
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(activity.<T>bindToLifecycle());
            }
        };
    }

    public static <T> ObservableTransformer<T, T> applyProgressBar(@NonNull final RxAppFragment activity,
                                                                   final boolean isShowLoading) {
        return applyProgressBar(activity, isShowLoading, null);
    }
    public static <T> ObservableTransformer<T, T> applyProgressBar(@NonNull final RxAppFragment activity,
                                                                   final boolean isShowLoading,
                                                                   final DialogInterface.OnDismissListener listener) {
        return applyProgressBar(activity, isShowLoading, listener,true);
    }

    public static <T> ObservableTransformer<T, T> applyProgressBar(@NonNull final RxAppFragment activity,
                                                                   final boolean isShowLoading,
                                                                   final DialogInterface.OnDismissListener listener,
                                                                   final boolean dialogCancelable) {
        return new ObservableTransformer<T, T>() {

            Dialog dialog;

            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (isShowLoading) {
                                    dialog = DialogUtils.createLoadingDialog(activity.getContext(), "加载中", dialogCancelable);
                                    dialog.show();
                                    if (listener != null){
                                        dialog.setOnDismissListener(listener);
                                    }
                                }
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .doOnTerminate(new Action() {
                            @Override
                            public void run() throws Exception {
                                if (isShowLoading) {
                                    DialogUtils.dismissDialog(dialog);
                                }
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(activity.<T>bindToLifecycle());
            }
        };
    }

    public static void cancelRequest(Disposable disposable){
        if (disposable != null && !disposable.isDisposed()) {
            Disposable s = disposable;
            disposable = DisposableHelper.DISPOSED;
            s.dispose();
        }
    }
}