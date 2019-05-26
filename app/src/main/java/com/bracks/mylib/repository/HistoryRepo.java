package com.bracks.mylib.repository;

import android.arch.lifecycle.MutableLiveData;

import com.bracks.futia.mylib.base.basevm.BaseRepo;
import com.bracks.futia.mylib.net.https.HttpCallback;
import com.bracks.mylib.datasource.IHistoryDataSource;
import com.bracks.mylib.model.bean.History;

import java.util.List;


/**
 * good programmer.
 *
 * @date : 2019-02-15 下午 02:43
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class HistoryRepo extends BaseRepo<IHistoryDataSource> {

    public HistoryRepo(IHistoryDataSource remoteDataSource) {
        super(remoteDataSource);
    }

    public MutableLiveData<List<History.DataBean.DatasBean>> getHistoryLiveData(int id, int page, String search) {
        MutableLiveData<List<History.DataBean.DatasBean>> liveData = new MutableLiveData<>();
        remoteDataSource.queryHistory(id, page, search, new HttpCallback<History.DataBean>() {
            @Override
            public void onSuccess(History.DataBean dataBean) {
                liveData.setValue(dataBean.getDatas());
            }
        });
        return liveData;
    }

}
