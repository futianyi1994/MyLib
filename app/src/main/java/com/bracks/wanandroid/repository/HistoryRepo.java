package com.bracks.wanandroid.repository;

import android.arch.lifecycle.MutableLiveData;

import com.bracks.mylib.base.basevm.BaseRepo;
import com.bracks.mylib.net.https.HttpCallback;
import com.bracks.wanandroid.datasource.IHistoryDataSource;
import com.bracks.wanandroid.model.bean.Chapter;

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

    public MutableLiveData<List<Chapter.DataBean.DatasBean>> getHistoryLiveData(int id, int page, String search) {
        MutableLiveData<List<Chapter.DataBean.DatasBean>> liveData = new MutableLiveData<>();
        remoteDataSource.queryHistory(id, page, search, new HttpCallback<Chapter.DataBean>() {
            @Override
            public void onSuccess(Chapter.DataBean dataBean) {
                liveData.setValue(dataBean.getDatas());
            }
        });
        return liveData;
    }

}
