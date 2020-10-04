package com.bracks.wanandroid.contract;

import com.bracks.mylib.base.basemvp.BaseModel;
import com.bracks.mylib.base.basemvp.BasePresenterInter;
import com.bracks.mylib.base.interf.BaseView;
import com.bracks.wanandroid.model.bean.Banner;
import com.bracks.wanandroid.model.bean.Chapter;

import java.util.List;

import io.reactivex.Observable;


/**
 * good programmer.
 *
 * @date : 2019-06-12 下午 05:00
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public interface HomeFragContract {

    interface View extends BaseView {
        void showBanner(List<Banner.DataBean> data);

        void onRefresh(List<Chapter.DataBean.DatasBean> data);

        void onLoadMore(List<Chapter.DataBean.DatasBean> data);

    }

    interface Presenter extends BasePresenterInter<View> {
        void refresh();

        void loadMore(int page);
    }

    interface Model<M> extends BaseModel<M> {
        Observable<M> refresh();

        Observable<Chapter> loadMore(int page);
    }
}
