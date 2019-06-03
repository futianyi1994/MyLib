package com.bracks.wanandroid.presenter;


import com.bracks.mylib.base.basemvp.BaseModel;
import com.bracks.mylib.base.basemvp.BasePresenter;
import com.bracks.mylib.net.https.HttpCallback;
import com.bracks.wanandroid.model.PublicFragM;
import com.bracks.wanandroid.model.bean.PublicList;
import com.bracks.wanandroid.viewiterf.PublicFragV;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.util.List;


/**
 * good programmer.
 *
 * @date : 2019-01-21 下午 04:08
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class PublicFragP extends BasePresenter<PublicFragV> {
    private BaseModel<List<PublicList.DataBean>> iCourseModel = new PublicFragM();

    /**
     * 执行数据
     */
    public <E> void fetch(LifecycleProvider<E> lifecycleProvider) {
        if (getView() != null) {
            getView().showLoading("加载中", true);
            iCourseModel.loadData(lifecycleProvider, new HttpCallback<List<PublicList.DataBean>>() {
                @Override
                public void onSuccess(List<PublicList.DataBean> beanList) {
                    getView().showDatas(beanList);
                }

                @Override
                public void onComplete() {
                    super.onComplete();
                    getView().hideLoading();
                }
            });
        }
    }
}
