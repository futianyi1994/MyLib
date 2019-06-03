package com.bracks.wanandroid.datasource;


import com.bracks.mylib.base.basevm.BaseDataSource;
import com.bracks.mylib.net.https.HttpCallback;
import com.bracks.wanandroid.model.bean.Login;

/**
 * good programmer.
 *
 * @date : 2019-02-15 下午 02:44
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public interface ILoginDataSource extends BaseDataSource {

    void login(String userName, String psw, HttpCallback<Login.DataBean> callback);

}
