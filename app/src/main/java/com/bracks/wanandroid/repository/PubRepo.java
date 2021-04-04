package com.bracks.wanandroid.repository;

import androidx.lifecycle.MutableLiveData;

import com.bracks.mylib.base.basevm.BaseRepo;
import com.bracks.mylib.net.https.HttpCallback;
import com.bracks.wanandroid.datasource.IPubDataSource;
import com.bracks.wanandroid.model.bean.PublicList;

import java.util.List;


/**
 * good programmer.
 *
 * @date : 2019-02-15 下午 02:43
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class PubRepo extends BaseRepo<IPubDataSource> {

    public PubRepo(IPubDataSource remoteDataSource) {
        super(remoteDataSource);
    }

    public MutableLiveData<List<PublicList.DataBean>> getPublicList() {
        MutableLiveData<List<PublicList.DataBean>> liveData = new MutableLiveData<>();
        remoteDataSource.publicList(new HttpCallback<List<PublicList.DataBean>>() {
            @Override
            public void onSuccess(List<PublicList.DataBean> dataBean) {
                liveData.setValue(dataBean);
            }
        });
        return liveData;
    }
}
