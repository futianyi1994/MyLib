package com.bracks.mylib.rx;


import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;
import com.uber.autodispose.lifecycle.CorrespondingEventsFunction;

/**
 * good programmer.
 *
 * @date : 2019-07-07 下午 01:13
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class RxAutoDispose {
    private RxAutoDispose() {
        throw new IllegalStateException("Can't instance the RxAutoDispose");
    }

    public static <T> AutoDisposeConverter<T> bindLifecycleDestroy(LifecycleOwner lifecycleOwner) {
        return bindLifecycle(lifecycleOwner.getLifecycle(), Lifecycle.Event.ON_DESTROY);
    }

    public static <T> AutoDisposeConverter<T> bindLifecycle(LifecycleOwner lifecycleOwner) {
        return bindLifecycle(lifecycleOwner.getLifecycle());
    }

    public static <T> AutoDisposeConverter<T> bindLifecycle(Lifecycle lifecycle) {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycle));
    }

    public static <T> AutoDisposeConverter<T> bindLifecycle(LifecycleOwner lifecycleOwner, Lifecycle.Event untilEvent) {
        return bindLifecycle(lifecycleOwner.getLifecycle(), untilEvent);
    }

    public static <T> AutoDisposeConverter<T> bindLifecycle(Lifecycle lifecycle, Lifecycle.Event untilEvent) {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycle, untilEvent));
    }

    public static <T> AutoDisposeConverter<T> bindLifecycle(LifecycleOwner lifecycleOwner, CorrespondingEventsFunction<Lifecycle.Event> boundaryResolver) {
        return bindLifecycle(lifecycleOwner.getLifecycle(), boundaryResolver);
    }

    public static <T> AutoDisposeConverter<T> bindLifecycle(Lifecycle lifecycle, CorrespondingEventsFunction<Lifecycle.Event> boundaryResolver) {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycle, boundaryResolver));
    }
}
