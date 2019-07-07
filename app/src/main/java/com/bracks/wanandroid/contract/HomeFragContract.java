package com.bracks.wanandroid.contract;

import com.bracks.mylib.base.basemvp.BaseModel;
import com.bracks.mylib.base.basemvp.BasePresenterInter;
import com.bracks.mylib.base.basemvp.BaseView;
import com.bracks.wanandroid.model.bean.Banner;
import com.bracks.wanandroid.model.bean.HomeList;

import java.util.List;


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
        void showDatas(List<HomeList.DataBean.DatasBean> data);

        void showBanner(List<Banner.DataBean> data);

    }

    interface Presenter extends BasePresenterInter<View> {
        void fetch();
    }

    interface Model<M> extends BaseModel<M> {
    }
}
