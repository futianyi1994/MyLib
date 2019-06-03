package com.bracks.wanandroid.repository;

import android.arch.lifecycle.MutableLiveData;

import com.bracks.mylib.base.basevm.BaseRepo;
import com.bracks.mylib.net.https.HttpCallback;
import com.bracks.wanandroid.datasource.ILoginDataSource;
import com.bracks.wanandroid.model.bean.Login;


/**
 * good programmer.
 *
 * @date : 2019-02-15 下午 02:43
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class LoginRepo extends BaseRepo<ILoginDataSource> {

    public LoginRepo(ILoginDataSource remoteDataSource) {
        super(remoteDataSource);
    }

    public MutableLiveData<Login.DataBean> getHistoryLiveData(String userName, String psw) {
        MutableLiveData<Login.DataBean> liveData = new MutableLiveData<>();
        remoteDataSource.login(userName, psw, new HttpCallback<Login.DataBean>() {
            @Override
            public void onSuccess(Login.DataBean dataBean) {
                liveData.setValue(dataBean);
            }
        });
        return liveData;
    }

}
