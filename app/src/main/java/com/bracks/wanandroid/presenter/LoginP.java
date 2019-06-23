package com.bracks.wanandroid.presenter;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.bracks.mylib.base.basemvp.BasePresenter;
import com.bracks.mylib.base.basevm.LViewModelProviders;
import com.bracks.wanandroid.contract.LoginContract;
import com.bracks.wanandroid.viewmodel.LoginViewModel;

/**
 * good programmer.
 *
 * @date : 2019-02-13 上午 10:46
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class LoginP extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {

    private LoginViewModel viewModel;

    @Override
    public LoginViewModel getViewModel(FragmentActivity activity) {
        if (getView() != null) {
            if (viewModel == null) {
                viewModel = LViewModelProviders.of(activity, LoginViewModel.class);
                viewModel
                        .getHistoryLiveData()
                        .observe(activity, dataBean -> {
                            if (!TextUtils.isEmpty(dataBean.getUsername())) {
                                getView().loginSrccess(dataBean.getUsername());
                            }
                        });
                viewModel
                        .getRegitsterLiveData()
                        .observe(activity, dataBean -> {
                            if (!TextUtils.isEmpty(dataBean.getUsername())) {
                                getView().registerSrccess();
                            }
                        });
            }
        }
        return viewModel;
    }

    @Override
    public void login(String userName, String psw) {
        if (getView() != null) {
            viewModel.login(userName, psw);
        }
    }

    @Override
    public void register(String userName, String psw, String repassword) {
        if (getView() != null) {
            viewModel.register(userName, psw, repassword);
        }
    }
}
