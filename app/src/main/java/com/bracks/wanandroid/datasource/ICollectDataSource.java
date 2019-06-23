package com.bracks.wanandroid.datasource;


import com.bracks.mylib.base.basevm.BaseDataSource;
import com.bracks.mylib.net.https.HttpCallback;
import com.bracks.wanandroid.model.bean.Collect;

/**
 * good programmer.
 *
 * @date : 2019-02-15 下午 02:44
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public interface ICollectDataSource extends BaseDataSource {

    void queryCollect(int page, HttpCallback<Collect.DataBean> callback);

}
