package com.bracks.mylib.rx;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.bracks.mylib.utils.bar.BarUtils;
import com.bracks.mylib.utils.widget.DialogUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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

    public static boolean isShowLoading(String page) {
        return "1".equals(page);
    }

    public static boolean isShowLoading(int page) {
        return page == 1;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isDestroy(RxAppActivity activity) {
        return activity == null || activity.isDestroyed() || activity.isFinishing();
    }

    public static boolean isDestroy(RxAppFragment fragment) {
        return fragment.isDetached();
    }

    public static <T> ObservableTransformer<T, T> applyProgressBar(@NonNull final RxAppActivity activity,
                                                                   final boolean isShowLoading) {
        return applyProgressBar(activity, isShowLoading, null);
    }

    public static <T> ObservableTransformer<T, T> applyProgressBar(@NonNull final RxAppActivity activity,
                                                                   final boolean isShowLoading,
                                                                   final DialogInterface.OnDismissListener listener) {
        return applyProgressBar(activity, isShowLoading, listener, true);
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
                        .doOnSubscribe(disposable -> {
                            if (isShowLoading) {
                                dialog = DialogUtils.createLoadingDialog(activity, "加载中", dialogCancelable);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                    DialogUtils.afterShow(() -> BarUtils.hideNavBar(dialog.getWindow().getDecorView()));
                                } else {
                                    dialog.show();
                                }
                                if (listener != null) {
                                    dialog.setOnDismissListener(listener);
                                }
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .doFinally(() -> {
                            if (isShowLoading) {
                                DialogUtils.dismissDialog(dialog);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(activity.bindToLifecycle());
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
        return applyProgressBar(activity, isShowLoading, listener, true);
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
                        .doOnSubscribe(disposable -> {
                            if (isShowLoading) {
                                dialog = DialogUtils.createLoadingDialog(activity.getContext(), "加载中", dialogCancelable);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                    DialogUtils.afterShow(() -> BarUtils.hideNavBar(dialog.getWindow().getDecorView()));
                                } else {
                                    dialog.show();
                                }
                                if (listener != null) {
                                    dialog.setOnDismissListener(listener);
                                }
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .doFinally(() -> {
                            if (isShowLoading) {
                                DialogUtils.dismissDialog(dialog);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(activity.bindToLifecycle());
            }
        };
    }

    public static void cancelRequest(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            Disposable s = disposable;
            disposable = DisposableHelper.DISPOSED;
            s.dispose();
        }
    }
}