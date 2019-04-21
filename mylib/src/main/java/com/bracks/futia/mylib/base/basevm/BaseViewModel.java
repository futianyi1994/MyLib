package com.bracks.futia.mylib.base.basevm;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.bracks.futia.mylib.exception.ApiException;
import com.bracks.futia.mylib.rx.RxAppActivity;
import com.bracks.futia.mylib.rx.RxAppFragment;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;


/**
 * good programmer.
 *
 * @date : 2019-02-14 下午 05:45
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class BaseViewModel extends ViewModel implements IViewModelAction {

    private MutableLiveData<BaseActionEvent> actionLiveData;

    protected LifecycleOwner lifecycleOwner;

    /**
     * LifecycleProvide接口
     */
    protected LifecycleProvider<ActivityEvent> lifecycleActProvider;
    protected LifecycleProvider<FragmentEvent> lifecycleFragProvider;

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

    void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

    <E> void setLifecycleProvider(LifecycleProvider<E> lifecycleProvider) {
        if (lifecycleProvider instanceof RxAppActivity) {
            lifecycleActProvider = ((RxAppActivity) lifecycleProvider);
        } else if (lifecycleProvider instanceof RxAppFragment) {
            lifecycleFragProvider = ((RxAppFragment) lifecycleProvider);
        } else {
            throw new ApiException(0, "Base Activity not implements LifecycleProvider");
        }
    }
}