package com.bracks.mylib.base.basevm;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.bracks.mylib.base.BaseFragment;
import com.bracks.mylib.base.interf.BaseView;
import com.bracks.mylib.utils.BarUtils;
import com.bracks.mylib.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * good programmer.
 *
 * @date : 2019-02-14 下午 06:23
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public abstract class BaseVmFrag extends BaseFragment implements BaseView {

    protected Dialog dialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData(savedInstanceState);
        initViewModelEvent();
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
    }

    @Override
    public void showLoading(String msg, boolean isCancelable) {
        dialog = DialogUtils.createLoadingDialog(getContext(), msg, isCancelable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            DialogUtils.afterShow(() -> BarUtils.hideNavBar(dialog.getWindow().getDecorView()));
        } else {
            dialog.show();
        }
    }

    @Override
    public void hideLoading() {
        DialogUtils.dismissDialog(dialog);
    }

    protected abstract ViewModel initViewModel();

    protected List<ViewModel> initViewModelList() {
        return null;
    }

    private void initViewModelEvent() {
        List<ViewModel> viewModelList = initViewModelList();
        if (viewModelList != null && viewModelList.size() > 0) {
            observeEvent(viewModelList);
        } else {
            ViewModel viewModel = initViewModel();
            if (viewModel != null) {
                List<ViewModel> modelList = new ArrayList<>();
                modelList.add(viewModel);
                observeEvent(modelList);
            }
        }
    }

    private void observeEvent(List<ViewModel> viewModelList) {
        for (ViewModel viewModel : viewModelList) {
            if (viewModel instanceof BaseViewModelInter) {
                BaseViewModelInter viewModelAction = (BaseViewModelInter) viewModel;
                getLifecycle().addObserver(viewModelAction);
                viewModelAction
                        .getActionLiveData()
                        .observe(this, baseActionEvent -> {
                            if (baseActionEvent != null) {
                                switch (baseActionEvent.getAction()) {
                                    case BaseActionEvent.SHOW_LOADING_DIALOG:
                                        if (baseActionEvent.getMessage() != null) {
                                            showLoading(baseActionEvent.getMessage(), baseActionEvent.isCancelable());
                                        } else {
                                            showLoading("加载中", baseActionEvent.isCancelable());
                                        }
                                        break;
                                    case BaseActionEvent.DISMISS_LOADING_DIALOG:
                                        hideLoading();
                                        break;
                                    case BaseActionEvent.SHOW_TOAST:
                                        showToast(baseActionEvent.getMessage());
                                        break;
                                    default:
                                        break;
                                }
                            }
                        });
            }
        }
    }
}