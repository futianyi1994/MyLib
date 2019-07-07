package com.bracks.mylib.rx;


import android.arch.lifecycle.LifecycleOwner;

import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

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

    public static <T> AutoDisposeConverter<T> bindLifecycle(LifecycleOwner lifecycleOwner) {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner));
    }
}
