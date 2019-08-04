package com.bracks.wanandroid.datasource;


import com.bracks.mylib.base.basevm.BaseRemoteDataSource;
import com.bracks.mylib.base.basevm.BaseViewModel;
import com.bracks.mylib.net.https.HttpCallback;
import com.bracks.wanandroid.model.bean.PublicList;
import com.bracks.wanandroid.net.ApiService;

import java.util.List;

/**
 * good programmer.
 *
 * @date : 2019-02-15 下午 02:46
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class PubDataSource extends BaseRemoteDataSource implements IPubDataSource {
    public PubDataSource(BaseViewModel baseViewModel) {
        super(baseViewModel);
    }

    @Override
    public void publicList(HttpCallback<List<PublicList.DataBean>> callback) {
        execute(ApiService.getService().getPublicList(), callback);
    }
}
