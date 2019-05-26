package com.bracks.mylib.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.bracks.futia.mylib.base.basevm.BaseViewModel;
import com.bracks.mylib.datasource.HistoryDataSource;
import com.bracks.mylib.model.bean.History;
import com.bracks.mylib.repository.HistoryRepo;

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
    private MutableLiveData<List<History.DataBean.DatasBean>> liveData;
    private HistoryRepo weatherRepo;

    @Override
    public void startLoading() {
    }

    public HistoryViewModel() {
        liveData = new MutableLiveData<>();
        weatherRepo = new HistoryRepo(new HistoryDataSource(this));
    }

    public void quertHistory(int id, int page,String search) {
        weatherRepo
                .getHistoryLiveData(id, page,search)
                .observe(lifecycleOwner, datasBeans -> liveData.setValue(datasBeans));
    }

    public MutableLiveData<List<History.DataBean.DatasBean>> getHistoryLiveData() {
        return liveData;

    }
}
