package com.bracks.wanandroid.datasource;


import com.bracks.mylib.base.basevm.BaseRemoteDataSource;
import com.bracks.mylib.base.basevm.BaseViewModel;
import com.bracks.mylib.net.https.HttpCallback;
import com.bracks.wanandroid.model.bean.Collect;
import com.bracks.wanandroid.net.ApiService;

/**
 * good programmer.
 *
 * @date : 2019-02-15 下午 02:46
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class CollectDataSource extends BaseRemoteDataSource implements ICollectDataSource {
    public CollectDataSource(BaseViewModel baseViewModel) {
        super(baseViewModel);
    }

    @Override
    public void queryCollect(int page, HttpCallback<Collect.DataBean> callback) {
        execute(ApiService.getService().collectList(page), callback);
    }
}
