package com.bracks.futia.mylib.rx;


import io.reactivex.CompletableTransformer;
import io.reactivex.FlowableTransformer;
import io.reactivex.MaybeTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
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
        return upstream -> upstream
                .subscribeOn(scheduler)
                .observeOn(AndroidSchedulers.mainThread());
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
        return upstream -> upstream
                .subscribeOn(scheduler)
                .observeOn(AndroidSchedulers.mainThread());
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
        return upstream -> upstream
                .subscribeOn(scheduler)
                .observeOn(AndroidSchedulers.mainThread());
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
        return upstream -> upstream
                .subscribeOn(scheduler)
                .observeOn(AndroidSchedulers.mainThread());
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
        return upstream -> upstream
                .subscribeOn(scheduler)
                .observeOn(AndroidSchedulers.mainThread());
    }
}