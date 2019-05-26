package com.bracks.mylib.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.bracks.futia.mylib.base.basevm.BaseViewModel;
import com.bracks.mylib.datasource.LoginDataSource;
import com.bracks.mylib.model.bean.Login;
import com.bracks.mylib.repository.LoginRepo;

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
    private LoginRepo loginRepo;


    public LoginViewModel() {
        liveData = new MutableLiveData<>();
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
}
