package com.bracks.mylib.repository;

import android.arch.lifecycle.MutableLiveData;

import com.bracks.futia.mylib.base.basevm.BaseRepo;
import com.bracks.futia.mylib.net.https.HttpCallback;
import com.bracks.mylib.datasource.ILoginDataSource;
import com.bracks.mylib.model.bean.Login;


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
