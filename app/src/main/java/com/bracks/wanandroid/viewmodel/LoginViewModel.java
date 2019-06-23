package com.bracks.wanandroid.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.bracks.mylib.base.basevm.BaseViewModel;
import com.bracks.wanandroid.datasource.LoginDataSource;
import com.bracks.wanandroid.model.bean.Login;
import com.bracks.wanandroid.repository.LoginRepo;

/**
 * good programmer.
 *
 * @date : 2019-02-18 下午 05:36
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class LoginViewModel extends BaseViewModel {
    private MutableLiveData<Login.DataBean> liveData;
    private MutableLiveData<Login.DataBean> regitsterLiveData;
    private LoginRepo loginRepo;


    public LoginViewModel() {
        liveData = new MutableLiveData<>();
        regitsterLiveData = new MutableLiveData<>();
        loginRepo = new LoginRepo(new LoginDataSource(this));
    }

    public void login(String userName, String psw) {
        loginRepo
                .getHistoryLiveData(userName, psw)
                .observe(lifecycleOwner, datasBeans -> liveData.setValue(datasBeans));
    }

    public MutableLiveData<Login.DataBean> getHistoryLiveData() {
        return liveData;
    }

    public void register(String userName, String psw, String repassword) {
        loginRepo
                .getRegisterLiveData(userName, psw, repassword)
                .observe(lifecycleOwner, dataBean -> regitsterLiveData.setValue(dataBean));
    }

    public MutableLiveData<Login.DataBean> getRegitsterLiveData() {
        return regitsterLiveData;
    }
}
