package com.bracks.futia.mylib.base.basevm;

import android.app.Dialog;
import android.arch.lifecycle.ViewModel;

import com.bracks.futia.mylib.base.basemvp.BasePresenter;
import com.bracks.futia.mylib.base.basemvp.BaseUi;
import com.bracks.futia.mylib.base.basemvp.BaseView;
import com.bracks.futia.mylib.utils.widget.DialogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * good programmer.
 *
 * @date : 2019-01-21 下午 04:09
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public abstract class BaseVmUi<V extends BaseView, P extends BasePresenter<V>> extends BaseUi<V, P> {

    private Dialog dialog;


    @Override
    public void initView() {
        super.initView();
        initViewModelEvent();
    }

    @Override
    public void showLoading(String msg, boolean isCancelable) {
        dialog = DialogUtils.createLoadingDialog(this, msg, isCancelable);
        dialog.show();
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
            if (viewModel instanceof IViewModelAction) {
                IViewModelAction viewModelAction = (IViewModelAction) viewModel;
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
                                    case BaseActionEvent.FINISH:
                                        finish();
                                        break;
                                    case BaseActionEvent.FINISH_WITH_RESULT_OK:
                                        setResult(RESULT_OK);
                                        finish();
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
