package com.bracks.wanandroid.contract;

import android.support.v4.app.FragmentActivity;

import com.bracks.mylib.base.basemvp.BasePresenterInter;
import com.bracks.mylib.base.basemvp.BaseView;
import com.bracks.wanandroid.model.bean.Collect;
import com.bracks.wanandroid.viewmodel.CollectViewModel;

import java.util.List;

/**
 * good programmer.
 *
 * @date : 2019-06-12 下午 06:29
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public interface CollectContract {

    interface View extends BaseView {
        void showDatas(List<Collect.DataBean.DatasBean> data);

        void loadMore(List<Collect.DataBean.DatasBean> data);
    }

    interface Presenter extends BasePresenterInter<View> {
        CollectViewModel fetch(FragmentActivity activity, int page);
    }
}
