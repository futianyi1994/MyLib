package com.bracks.mylib.rx;

import com.bracks.mylib.Constants;
import com.bracks.mylib.utils.TLog;

import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/**
 * good programmer.
 *
 * @date : 2018-12-06 上午 10:24
 * @author: futia
 * @email : futianyi1994@126.com
 * @description : 请求失败总共重试maxRetries次，重试间隔retryDelayMillis毫秒
 */
public class RxRetryWithFlowFunc implements Function<Flowable<Throwable>, Publisher<?>> {
    public static final String TAG = "RxRetryWithFlowFunc";
    private final int maxRetries;
    private final long retryDelayMillis;
    private final TimeUnit unit;
    private int retryCount;

    public RxRetryWithFlowFunc() {
        this(Constants.MAX_RETRIES, Constants.RETRY_DELAY_MILLIS);
    }

    public RxRetryWithFlowFunc(int maxRetries) {
        this(maxRetries, Constants.RETRY_DELAY_MILLIS, TimeUnit.MILLISECONDS);
    }

    public RxRetryWithFlowFunc(int maxRetries, long retryDelayMillis) {
        this(maxRetries, retryDelayMillis, TimeUnit.MILLISECONDS);
    }

    public RxRetryWithFlowFunc(int maxRetries, long retryDelayMillis, TimeUnit unit) {
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
        this.unit = unit;
    }

    @Override
    public Publisher<?> apply(Flowable<Throwable> throwableFlowable) {
        return throwableFlowable
                .flatMap((Function<Throwable, Publisher<?>>) throwable -> {
                    if (++retryCount <= maxRetries) {
                        //When this Observable calls onNext, the original Observable will be retried (i.e. re-subscribed).
                        TLog.e(TAG, throwable.getMessage() + ": it will try after " + retryDelayMillis + " millisecond, retry count " + retryCount);
                        //return Flowable.just(retryCount).delay(retryDelayMillis, TimeUnit.MILLISECONDS);
                        return Flowable.timer(retryDelayMillis, unit);
                    }
                    //Max retries hit. Just pass the error along.
                    return Flowable.error(throwable);
                });
    }
}