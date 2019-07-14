package com.bracks.wanandroid.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.bracks.mylib.base.basevm.BaseViewModel;
import com.bracks.wanandroid.datasource.HistoryDataSource;
import com.bracks.wanandroid.model.bean.Chapter;
import com.bracks.wanandroid.repository.HistoryRepo;

import java.util.List;

/**
 * good programmer.
 *
 * @date : 2019-02-18 下午 05:36
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class HistoryViewModel extends BaseViewModel {
    private MutableLiveData<List<Chapter.DataBean.DatasBean>> liveData;
    private HistoryRepo historyRepo;

    @Override
    public void startLoading() {
    }

    public HistoryViewModel() {
        liveData = new MutableLiveData<>();
        historyRepo = new HistoryRepo(new HistoryDataSource(this));
    }

    public void quertHistory(int id, int page, String search) {
        historyRepo
                .getHistoryLiveData(id, page, search)
                .observe(lifecycleOwner, datasBeans -> liveData.setValue(datasBeans));
    }

    public MutableLiveData<List<Chapter.DataBean.DatasBean>> getHistoryLiveData() {
        return liveData;
    }
}
