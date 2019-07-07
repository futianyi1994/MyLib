package com.bracks.mylib.base.basevm;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;


/**
 * good programmer.
 *
 * @date : 2019-02-14 下午 05:45
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class BaseViewModel extends ViewModel implements BaseViewModelInter {

    private MutableLiveData<BaseActionEvent> actionLiveData;

    protected LifecycleOwner lifecycleOwner;


    public BaseViewModel() {
        actionLiveData = new MutableLiveData<>();
    }


    @Override
    public void startLoading() {
        startLoading(null, true);
    }

    @Override
    public void startLoading(String message, boolean isCancelable) {
        BaseActionEvent baseActionEvent = new BaseActionEvent(BaseActionEvent.SHOW_LOADING_DIALOG);
        baseActionEvent.setMessage(message, isCancelable);
        actionLiveData.setValue(baseActionEvent);
    }

    @Override
    public void dismissLoading() {
        actionLiveData.setValue(new BaseActionEvent(BaseActionEvent.DISMISS_LOADING_DIALOG));
    }

    @Override
    public void showToast(String message) {
        BaseActionEvent baseActionEvent = new BaseActionEvent(BaseActionEvent.SHOW_TOAST);
        baseActionEvent.setMessage(message);
        actionLiveData.setValue(baseActionEvent);
    }

    @Override
    public void finish() {
        actionLiveData.setValue(new BaseActionEvent(BaseActionEvent.FINISH));
    }

    @Override
    public void finishWithResultOk() {
        actionLiveData.setValue(new BaseActionEvent(BaseActionEvent.FINISH_WITH_RESULT_OK));
    }

    @Override
    public MutableLiveData<BaseActionEvent> getActionLiveData() {
        return actionLiveData;
    }

    @Override
    public void setLifecycleOwner(LifecycleOwner owner) {
        lifecycleOwner = owner;
    }

    @Override
    public void onCreate(@NotNull LifecycleOwner owner) {
    }

    @Override
    public void onStart(@NotNull LifecycleOwner owner) {
    }

    @Override
    public void onResume(@NotNull LifecycleOwner owner) {
    }

    @Override
    public void onPause(@NotNull LifecycleOwner owner) {
    }

    @Override
    public void onStop(@NotNull LifecycleOwner owner) {
    }

    @Override
    public void onDestroy(@NotNull LifecycleOwner owner) {
    }

    @Override
    public void onLifecycleChanged(@NotNull LifecycleOwner owner, @NotNull Lifecycle.Event event) {
    }

    public LifecycleOwner getLifecycleOwner() {
        return lifecycleOwner;
    }
}