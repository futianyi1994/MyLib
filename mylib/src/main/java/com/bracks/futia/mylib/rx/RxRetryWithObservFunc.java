package com.bracks.futia.mylib.rx;

import com.bracks.futia.mylib.Constants;
import com.bracks.futia.mylib.utils.log.TLog;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * good programmer.
 *
 * @date : 2018-12-06 上午 10:24
 * @author: futia
 * @email : futianyi1994@126.com
 * @description : 请求失败总共重试maxRetries次，重试间隔retryDelayMillis毫秒
 */
public class RxRetryWithObservFunc implements Function<Observable<Throwable>, ObservableSource<?>> {
    public static final String TAG = "RxRetryWithObservFunc";
    private final int maxRetries;
    private final long retryDelayMillis;
    private final TimeUnit unit;
    private int retryCount;

    public RxRetryWithObservFunc() {
        this(Constants.MAX_RETRIES, Constants.RETRY_DELAY_MILLIS);
    }

    public RxRetryWithObservFunc(int maxRetries, long retryDelayMillis) {
        this(maxRetries, retryDelayMillis, TimeUnit.MILLISECONDS);
    }

    public RxRetryWithObservFunc(int maxRetries, long retryDelayMillis, TimeUnit unit) {
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
        this.unit = unit;
    }

    @Override
    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) {
        return throwableObservable
                .flatMap((Function<Throwable, ObservableSource<?>>) throwable -> {
                    if (++retryCount <= maxRetries) {
                        //When this Observable calls onNext, the original Observable will be retried (i.e. re-subscribed).
                        TLog.e(TAG, "get error, it will try after " + retryDelayMillis + " millisecond, retry count " + retryCount);
                        //return Observable.just(retryCount).delay(retryDelayMillis, TimeUnit.MILLISECONDS);
                        return Observable.timer(retryDelayMillis, unit);
                    }
                    //Max retries hit. Just pass the error along.
                    return Observable.error(throwable);
                });
    }
}