package com.bracks.futia.mylib.rx;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;

/**
 * good programmer.
 *
 * @date : 2019-05-30 上午 11:16
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class RxBus {
    private static volatile RxBus instance;
    private final Relay<Object> mBus;
    private final Map<Class<?>, Object> mStickyEventMap;

    private RxBus() {
        this.mBus = PublishRelay.create().toSerialized();
        mStickyEventMap = new ConcurrentHashMap<>();
    }

    private static class Holder {
        private static final RxBus BUS = new RxBus();
    }

    public static RxBus getDefault() {
        if (instance == null) {
            synchronized (RxBus.class) {
                if (instance == null) {
                    instance = Holder.BUS;
                }
            }
        }
        return instance;
    }

    public void post(Object obj) {
        mBus.accept(obj);
    }

    public <T> Observable<T> toObservable(Class<T> tClass) {
        return mBus.ofType(tClass);
    }

    public Observable<Object> toObservable() {
        return mBus;
    }

    public boolean hasObservers() {
        return mBus.hasObservers();
    }

    public void reset() {
        instance = null;
    }

    /*************************************************粘性事件************************************************/

    public void postSticky(Object event) {
        synchronized (mStickyEventMap) {
            mStickyEventMap.put(event.getClass(), event);
        }
        post(event);
    }

    public <T> Observable<T> toObservableSticky(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            Observable<T> observable = toObservable(eventType);
            Object event = mStickyEventMap.get(eventType);
            if (event != null) {
                return observable
                        .mergeWith(Observable.create(subscriber -> subscriber.onNext(eventType.cast(event))));
            } else {
                return observable;
            }
        }
    }

    /**
     * 根据eventType获取Sticky事件
     *
     * @param eventType
     * @param <T>
     * @return
     */
    public <T> T getStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.get(eventType));
        }
    }

    /**
     * 移除指定eventType的Sticky事件
     *
     * @param eventType
     * @param <T>
     * @return
     */
    public <T> T removeStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.remove(eventType));
        }
    }

    /**
     * 移除所有的Sticky事件
     */
    public void removeAllStickyEvents() {
        synchronized (mStickyEventMap) {
            mStickyEventMap.clear();
        }
    }
}
