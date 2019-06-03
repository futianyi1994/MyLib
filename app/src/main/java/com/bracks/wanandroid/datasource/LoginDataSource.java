package com.bracks.wanandroid.datasource;


import com.bracks.mylib.base.basevm.BaseRemoteDataSource;
import com.bracks.mylib.base.basevm.BaseViewModel;
import com.bracks.mylib.net.https.HttpCallback;
import com.bracks.wanandroid.model.bean.Login;
import com.bracks.wanandroid.net.ApiService;

/**
 * good programmer.
 *
 * @date : 2019-02-15 下午 02:46
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class LoginDataSource extends BaseRemoteDataSource implements ILoginDataSource {
    public LoginDataSource(BaseViewModel baseViewModel) {
        super(baseViewModel);
    }

    @Override
    public void login(String userName, String psw, HttpCallback<Login.DataBean> callback) {
        execute(ApiService.getService().login(userName, psw), callback);
    }
}
