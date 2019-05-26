package com.bracks.mylib.presenter;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.bracks.futia.mylib.base.basemvp.BasePresenter;
import com.bracks.futia.mylib.base.basevm.LViewModelProviders;
import com.bracks.mylib.viewiterf.LoginV;
import com.bracks.mylib.viewmodel.LoginViewModel;

/**
 * good programmer.
 *
 * @date : 2019-02-13 上午 10:46
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class LoginP extends BasePresenter<LoginV> {

    private LoginViewModel viewModel;

    /**
     * 执行数据
     */
    public LoginViewModel getViewModel(FragmentActivity activity) {
        if (getView() != null) {
            if (viewModel == null) {
                viewModel = LViewModelProviders.of(activity, LoginViewModel.class);
                viewModel
                        .getHistoryLiveData()
                        .observe(activity, dataBean -> {
                            if (!TextUtils.isEmpty(dataBean.getUsername())) {
                                getView().loginSrccess();
                            }
                        });
            }
        }
        return viewModel;
    }

    public void login(String userName, String psw) {
        if (getView() != null) {
            viewModel.login(userName, psw);
        }
    }
}
