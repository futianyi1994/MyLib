package com.bracks.mylib.base.basevm;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


/**
 * good programmer.
 *
 * @date : 2019-02-14 下午 05:45
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class BaseViewModel extends ViewModel implements BaseViewModelInter {

    protected LifecycleOwner lifecycleOwner;
    private final MutableLiveData<BaseActionEvent> actionLiveData;


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
    public void onCreate(@NonNull LifecycleOwner owner) {
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
    }

    @Override
    public void onLifecycleChanged(@NonNull LifecycleOwner owner, @NonNull Lifecycle.Event event) {
    }

    public LifecycleOwner getLifecycleOwner() {
        return lifecycleOwner;
    }

    @Override
    public void setLifecycleOwner(LifecycleOwner owner) {
        lifecycleOwner = owner;
    }
}