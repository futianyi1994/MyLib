package com.bracks.futia.mylib.rx;


import org.reactivestreams.Publisher;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.CompletableTransformer;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.MaybeTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * good programmer.
 *
 * @date : 2018-12-28 下午 06:25
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class RxSchedulersCompat {

    public static <T> ObservableTransformer<T, T> ioSchedulerObser() {
        return applyObservSchedulers(Schedulers.io());
    }

    public static <T> FlowableTransformer<T, T> ioSchedulerFlow() {
        return applyFlowableSchedulers(Schedulers.io());
    }

    public static CompletableTransformer ioSchedulerComplet() {
        return applyCompletableSchedulers(Schedulers.io());
    }

    public static <T> SingleTransformer<T, T> ioSchedulerSingle() {
        return applySingleSchedulers(Schedulers.io());
    }

    public static <T> MaybeTransformer<T, T> ioSchedulerMaybe() {
        return applyMaybeSchedulers(Schedulers.io());
    }

    /**
     * @param scheduler Schedulers.computation()
     *                  Schedulers.io()
     *                  Schedulers.newThread()
     *                  Schedulers.trampoline()
     *                  Schedulers.from(ExecutorsManager.eventExecutor)
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> applyObservSchedulers(@NonNull final Scheduler scheduler) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .subscribeOn(scheduler)
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * @param scheduler Schedulers.computation()
     *                  Schedulers.io()
     *                  Schedulers.newThread()
     *                  Schedulers.trampoline()
     *                  Schedulers.from(ExecutorsManager.eventExecutor)
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> applyFlowableSchedulers(@NonNull final Scheduler scheduler) {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(@NonNull Flowable<T> upstream) {
                return upstream
                        .subscribeOn(scheduler)
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * @param scheduler Schedulers.computation()
     *                  Schedulers.io()
     *                  Schedulers.newThread()
     *                  Schedulers.trampoline()
     *                  Schedulers.from(ExecutorsManager.eventExecutor)
     * @return
     */
    public static CompletableTransformer applyCompletableSchedulers(@NonNull final Scheduler scheduler) {
        return new CompletableTransformer() {
            @Override
            public CompletableSource apply(Completable upstream) {
                return upstream
                        .subscribeOn(scheduler)
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * @param scheduler Schedulers.computation()
     *                  Schedulers.io()
     *                  Schedulers.newThread()
     *                  Schedulers.trampoline()
     *                  Schedulers.from(ExecutorsManager.eventExecutor)
     * @return
     */
    public static <T> SingleTransformer<T, T> applySingleSchedulers(@NonNull final Scheduler scheduler) {
        return new SingleTransformer<T, T>() {
            @Override
            public SingleSource<T> apply(Single<T> upstream) {
                return upstream
                        .subscribeOn(scheduler)
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * @param scheduler Schedulers.computation()
     *                  Schedulers.io()
     *                  Schedulers.newThread()
     *                  Schedulers.trampoline()
     *                  Schedulers.from(ExecutorsManager.eventExecutor)
     * @return
     */
    public static <T> MaybeTransformer<T, T> applyMaybeSchedulers(@NonNull final Scheduler scheduler) {
        return new MaybeTransformer<T, T>() {
            @Override
            public MaybeSource<T> apply(Maybe<T> upstream) {
                return upstream
                        .subscribeOn(scheduler)
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}