package com.bracks.wanandroid.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.bracks.mylib.base.basevm.BaseViewModel;
import com.bracks.wanandroid.datasource.PubDataSource;
import com.bracks.wanandroid.model.bean.PublicList;
import com.bracks.wanandroid.repository.PubRepo;

import java.util.List;

/**
 * good programmer.
 *
 * @date : 2019-02-18 下午 05:36
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class PubViewModel extends BaseViewModel {
    private final MutableLiveData<List<PublicList.DataBean>> liveData;
    private final PubRepo pubRepo;


    public PubViewModel() {
        liveData = new MutableLiveData<>();
        pubRepo = new PubRepo(new PubDataSource(this));
    }

    public void getPublicList() {
        pubRepo
                .getPublicList()
                .observe(lifecycleOwner, datasBeans -> liveData.setValue(datasBeans));
    }

    public MutableLiveData<List<PublicList.DataBean>> getPubListLiveData() {
        return liveData;
    }

}
