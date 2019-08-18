package com.bracks.wanandroid.repository;

import androidx.lifecycle.MutableLiveData;

import com.bracks.mylib.base.basevm.BaseRepo;
import com.bracks.mylib.net.https.HttpCallback;
import com.bracks.wanandroid.datasource.ICollectDataSource;
import com.bracks.wanandroid.model.bean.Collect;

import java.util.List;


/**
 * good programmer.
 *
 * @date : 2019-02-15 下午 02:43
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class CollectRepo extends BaseRepo<ICollectDataSource> {

    public CollectRepo(ICollectDataSource remoteDataSource) {
        super(remoteDataSource);
    }

    public MutableLiveData<List<Collect.DataBean.DatasBean>> getCollectLiveData(int page) {
        MutableLiveData<List<Collect.DataBean.DatasBean>> liveData = new MutableLiveData<>();
        remoteDataSource.queryCollect(page, new HttpCallback<Collect.DataBean>() {
            @Override
            public void onSuccess(Collect.DataBean dataBean) {
                if (dataBean.getDatas().size() > 0) {
                    dataBean.getDatas().get(0).setOver(dataBean.isOver());
                }
                liveData.setValue(dataBean.getDatas());
            }
        });
        return liveData;
    }

}
