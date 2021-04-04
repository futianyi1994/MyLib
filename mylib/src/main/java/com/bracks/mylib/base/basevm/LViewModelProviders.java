package com.bracks.mylib.base.basevm;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;


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
        T t = new ViewModelProvider(activity).get(modelClass);
        t.setLifecycleOwner(activity);
        return t;
    }

    public static <T extends BaseViewModel> T of(@NonNull Fragment fragment, Class<T> modelClass) {
        T t = new ViewModelProvider(fragment).get(modelClass);
        t.setLifecycleOwner(fragment);
        return t;
    }
}