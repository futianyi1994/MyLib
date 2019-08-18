package com.bracks.mylib.base.basevm;


import androidx.lifecycle.MutableLiveData;

import com.bracks.mylib.base.interf.BaseLifecycleObserver;

/**
 * good programmer.
 *
 * @date : 2019-02-14 下午 05:45
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public interface BaseViewModelInter extends BaseLifecycleObserver {

    void startLoading();

    void startLoading(String message, boolean isCancelable);

    void dismissLoading();

    void showToast(String message);

    void finish();

    void finishWithResultOk();

    MutableLiveData<BaseActionEvent> getActionLiveData();

}