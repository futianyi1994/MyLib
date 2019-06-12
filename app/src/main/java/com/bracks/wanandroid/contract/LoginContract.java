package com.bracks.wanandroid.contract;

import android.support.v4.app.FragmentActivity;

import com.bracks.mylib.base.basemvp.BasePresenterInter;
import com.bracks.mylib.base.basemvp.BaseView;
import com.bracks.wanandroid.viewmodel.LoginViewModel;

/**
 * good programmer.
 *
 * @date : 2019-06-12 下午 06:29
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public interface LoginContract {

    interface View extends BaseView {
        void loginSrccess();
    }

    interface Presenter extends BasePresenterInter<View> {
        LoginViewModel getViewModel(FragmentActivity activity);

        void login(String userName, String psw);
    }
}
