package com.bracks.wanandroid.contract;

import com.bracks.mylib.base.basemvp.BasePresenterInter;
import com.bracks.mylib.base.interf.BaseView;
import com.bracks.wanandroid.model.bean.PublicList;

import java.util.List;


/**
 * good programmer.
 *
 * @date : 2019-06-12 下午 05:00
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public interface PublicFragContract {

    interface View extends BaseView {
        void showDatas(List<PublicList.DataBean> data);
    }

    interface Presenter extends BasePresenterInter<View> {
        void fetch();
    }
}
