package com.bracks.mylib.rx;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

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
    public static boolean isDestroy(FragmentActivity activity) {
        return activity == null || activity.isDestroyed() || activity.isFinishing();
    }

    public static boolean isDestroy(Fragment fragment) {
        return fragment.isDetached();
    }

    public static <T> ObservableTransformer<T, T> applyProgressBar(@NonNull final Context context,
                                                                   final boolean isShowLoading) {
        return applyProgressBar(context, isShowLoading, null);
    }

    public static <T> ObservableTransformer<T, T> applyProgressBar(@NonNull final Context context,
                                                                   final boolean isShowLoading,
                                                                   final DialogInterface.OnDismissListener listener) {
        return applyProgressBar(context, isShowLoading, listener, true);
    }

    public static <T> ObservableTransformer<T, T> applyProgressBar(@NonNull final Context context,
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
                                dialog = DialogUtils.createLoadingDialog(context, "加载中", dialogCancelable);
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
                        ;
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