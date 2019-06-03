package com.bracks.mylib.base.basevm;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.bracks.mylib.rx.RxAppActivity;
import com.bracks.mylib.rx.RxAppFragment;

/**
 * good programmer.
 *
 * @date : 2019-02-14 下午 05:45
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class LViewModelProviders {

    public static <T extends BaseViewModel> T of(@NonNull FragmentActivity activity, Class<T> modelClass) {
        T t = ViewModelProviders.of(activity).get(modelClass);
        t.setLifecycleOwner(activity);
        t.setLifecycleProvider(activity instanceof RxAppActivity ? ((RxAppActivity) activity) : null);
        return t;
    }

    public static <T extends BaseViewModel> T of(@NonNull Fragment fragment, Class<T> modelClass) {
        T t = ViewModelProviders.of(fragment).get(modelClass);
        t.setLifecycleOwner(fragment);
        t.setLifecycleProvider(fragment instanceof RxAppFragment ? ((RxAppFragment) fragment) : null);
        return t;
    }
}