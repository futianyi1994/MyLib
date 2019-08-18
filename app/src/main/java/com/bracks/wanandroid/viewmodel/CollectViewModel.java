package com.bracks.wanandroid.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.bracks.mylib.base.basevm.BaseViewModel;
import com.bracks.wanandroid.datasource.CollectDataSource;
import com.bracks.wanandroid.model.bean.Collect;
import com.bracks.wanandroid.repository.CollectRepo;

import java.util.List;

/**
 * good programmer.
 *
 * @date : 2019-02-18 下午 05:36
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class CollectViewModel extends BaseViewModel {
    private MutableLiveData<List<Collect.DataBean.DatasBean>> liveData;
    private CollectRepo collectRepo;

    @Override
    public void startLoading() {
    }

    public CollectViewModel() {
        liveData = new MutableLiveData<>();
        collectRepo = new CollectRepo(new CollectDataSource(this));
    }

    public void quertCollect(int page) {
        collectRepo
                .getCollectLiveData(page)
                .observe(lifecycleOwner, datasBeans -> liveData.setValue(datasBeans));
    }

    public MutableLiveData<List<Collect.DataBean.DatasBean>> getCollectLiveData() {
        return liveData;
    }
}
