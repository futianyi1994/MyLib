package com.bracks.mylib.rx;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.Functions;

/**
 * good programmer.
 *
 * @date : 2019-03-27 下午 02:54
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :https://github.com/tbruyelle/RxPermissions
 */
public class RxPermission {
    public static final String TAG = "RxPermission";
    private final FragmentActivity activity;
    private final Fragment fragment;
    private final String[] permissions;
    private final String[] permissionNams;
    private final Consumer<? super Throwable> onError;
    private final Action onComplete;
    private final Consumer<? super Permission> requestEachCombinedOnNext;
    private final Observer<? super Permission> requestEachCombinedObserver;
    private final Consumer<? super Permission> requestEachOnNext;
    private final Observer<? super Permission> requestEachObserver;
    private final Consumer<? super Boolean> requestOnNext;
    private final Observer<? super Boolean> requestObserver;
    private RxPermissions rxPermissions;

    private RxPermission(Builder builder) {
        this.activity = builder.activity;
        this.fragment = builder.fragment;
        this.permissions = builder.permissions;
        this.permissionNams = builder.permissionNams;

        this.onError = builder.onError;
        this.onComplete = builder.onComplete;

        this.requestEachCombinedOnNext = builder.requestEachCombinedOnNext;
        this.requestEachCombinedObserver = builder.requestEachCombinedObserver;

        this.requestEachOnNext = builder.requestEachOnNext;
        this.requestEachObserver = builder.requestEachObserver;

        this.requestOnNext = builder.requestOnNext;
        this.requestObserver = builder.requestObserver;

        init();
    }

    private void init() {
        if (this.activity != null) {
            rxPermissions = new RxPermissions(this.activity);
        } else if (this.fragment != null) {
            rxPermissions = new RxPermissions(this.fragment);
        }
        if (this.requestEachCombinedObserver != null) {
            requestEachCombined(this.requestEachCombinedObserver);
        } else if (this.requestEachCombinedOnNext != null) {
            requestEachCombined(this.requestEachCombinedOnNext, this.onError, this.onComplete);
        } else if (this.requestEachObserver != null) {
            requestEach(this.requestEachObserver);
        } else if (this.requestEachOnNext != null) {
            requestEach(this.requestEachOnNext, this.onError, this.onComplete);
        } else if (this.requestObserver != null) {
            request(this.requestObserver);
        } else if (this.requestOnNext != null) {
            request(this.requestOnNext, this.onError, this.onComplete);
        }
    }

    public RxPermissions getRxPermissions() {
        return rxPermissions;
    }

    /*************************************************Request**************************************************/

    private void request(Consumer<? super Boolean> onNext, Consumer<? super Throwable> onError, Action onComplete) {
        Disposable disposable = rxPermissions
                .request(permissions)
                .subscribe(onNext, onError, onComplete);
    }

    private void request(Observer<? super Boolean> observer) {
        rxPermissions
                .request(permissions)
                .subscribe(observer);
    }

    /*************************************************RequestEach**************************************************/

    private void requestEach(Consumer<? super Permission> onNext, Consumer<? super Throwable> onError, Action onComplete) {
        Disposable disposable = rxPermissions
                .requestEach(permissions)
                .subscribe(onNext, onError, onComplete);
    }

    private void requestEach(Observer<? super Permission> observer) {
        rxPermissions
                .requestEach(permissions)
                .subscribe(observer);
    }

    /*************************************************RequestEachCombined**************************************************/

    /**
     * 获取Observable
     *
     * @param isDispose 是否经过flatMap处理
     * @return
     */
    public Observable<Permission> getRequestEachCombinedObservable(boolean isDispose) {
        return rxPermissions
                .requestEachCombined(permissions)
                .flatMap((Function<Permission, ObservableSource<Permission>>) permission ->
                        Observable
                                .create(emitter -> {
                                    try {
                                        if (isDispose) {
                                            if (permission.granted) {
                                                //全部授权，回调一次
                                            } else if (permission.shouldShowRequestPermissionRationale) {
                                                //至少有一个权限拒绝（没有勾选不再询问），回调一次
                                            } else {
                                                //至少有一个权限拒绝（勾选不在询问），回调一次
                                                if (permissions.length != permissionNams.length) {
                                                    throw new Exception("请设置权限数量和权限名对应！");
                                                }
                                                PermissionUtils.launchAppDetailsSettings();
                                                for (int i = 0; i < permissions.length; i++) {
                                                    if (!PermissionUtils.isGranted(permissions[i])) {
                                                        ToastUtils.showLong("需要%s权限", permissionNams[i]);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        emitter.onNext(permission);
                                        emitter.onComplete();
                                    } catch (Exception e) {
                                        emitter.onError(e);
                                    }
                                }));
    }

    private void requestEachCombined(Consumer<? super Permission> onNext, Consumer<? super Throwable> onError, Action onComplete) {
        Disposable disposable = getRequestEachCombinedObservable(true)
                .subscribe(onNext, onError, onComplete);
    }

    private void requestEachCombined(Observer<? super Permission> observer) {
        getRequestEachCombinedObservable(true)
                .subscribe(observer);
    }

    public static class Builder {
        private FragmentActivity activity;
        private Fragment fragment;
        private String[] permissions;
        private String[] permissionNams;

        private Consumer<? super Throwable> onError;
        private Action onComplete;

        private Consumer<? super Permission> requestEachCombinedOnNext;
        private Observer<? super Permission> requestEachCombinedObserver;

        private Consumer<? super Permission> requestEachOnNext;
        private Observer<? super Permission> requestEachObserver;

        private Consumer<? super Boolean> requestOnNext;
        private Observer<? super Boolean> requestObserver;

        public Builder(FragmentActivity activity) {
            this.activity = activity;
        }

        public Builder(Fragment fragment) {
            this.fragment = fragment;
        }

        public Builder setPermissions(String... permissions) {
            this.permissions = permissions;
            return this;
        }

        public Builder setPermissionNams(String... permissionNams) {
            this.permissionNams = permissionNams;
            return this;
        }

        /*************************************************Request**************************************************/

        public Builder request() {
            request(Functions.emptyConsumer(), Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION);
            return this;
        }

        public Builder request(Consumer<? super Boolean> requestOnNext) {
            request(requestOnNext, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION);
            return this;
        }

        public Builder request(Consumer<? super Boolean> requestOnNext, Consumer<? super Throwable> onError) {
            request(requestOnNext, onError, Functions.EMPTY_ACTION);
            return this;
        }

        public Builder request(Consumer<? super Boolean> requestOnNext, Consumer<? super Throwable> onError, Action onComplete) {
            this.requestOnNext = requestOnNext;
            this.onError = onError;
            this.onComplete = onComplete;
            return this;
        }

        public Builder request(Observer<? super Boolean> requestObserver) {
            this.requestObserver = requestObserver;
            return this;
        }

        /*************************************************RequestEach**************************************************/

        public Builder requestEach() {
            requestEach(Functions.emptyConsumer(), Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION);
            return this;
        }

        public Builder requestEach(Consumer<? super Permission> requestEachOnNext) {
            requestEach(requestEachOnNext, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION);
            return this;
        }

        public Builder requestEach(Consumer<? super Permission> requestEachOnNext, Consumer<? super Throwable> onError) {
            requestEach(requestEachOnNext, onError, Functions.EMPTY_ACTION);
            return this;
        }

        public Builder requestEach(Consumer<? super Permission> requestEachOnNext, Consumer<? super Throwable> onError, Action onComplete) {
            this.requestEachOnNext = requestEachOnNext;
            this.onError = onError;
            this.onComplete = onComplete;
            return this;
        }

        public Builder requestEach(Observer<? super Permission> requestEachObserver) {
            this.requestEachObserver = requestEachObserver;
            return this;
        }

        /*************************************************RequestEachCombined**************************************************/

        public Builder requestEachCombined() {
            requestEachCombined(Functions.emptyConsumer(), Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION);
            return this;
        }

        public Builder requestEachCombined(Consumer<? super Permission> requestEachCombinedOnNext) {
            this.requestEachCombinedOnNext = requestEachCombinedOnNext;
            requestEachCombined(requestEachCombinedOnNext, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION);
            return this;
        }

        public Builder requestEachCombined(Consumer<? super Permission> requestEachCombinedOnNext, Consumer<? super Throwable> onError) {
            this.requestEachCombinedOnNext = requestEachCombinedOnNext;
            requestEachCombined(requestEachCombinedOnNext, onError, Functions.EMPTY_ACTION);
            return this;
        }

        public Builder requestEachCombined(Consumer<? super Permission> requestEachCombinedOnNext, Consumer<? super Throwable> onError, Action onComplete) {
            this.requestEachCombinedOnNext = requestEachCombinedOnNext;
            this.onError = onError;
            this.onComplete = onComplete;
            return this;
        }

        public Builder requestEachCombined(Observer<? super Permission> requestEachCombinedObserver) {
            this.requestEachCombinedObserver = requestEachCombinedObserver;
            return this;
        }

        public RxPermission build() {
            return new RxPermission(this);
        }
    }
}
