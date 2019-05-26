package com.bracks.mylib.datasource;


import com.bracks.futia.mylib.base.basevm.BaseRemoteDataSource;
import com.bracks.futia.mylib.base.basevm.BaseViewModel;
import com.bracks.futia.mylib.net.https.HttpCallback;
import com.bracks.mylib.model.bean.Login;
import com.bracks.mylib.net.ApiService;

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
