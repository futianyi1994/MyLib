package com.bracks.wanandroid.contract;

import com.bracks.mylib.base.basemvp.BasePresenterInter;
import com.bracks.mylib.base.interf.BaseView;

import java.util.List;


/**
 * good programmer.
 *
 * @date : 2019-06-12 下午 05:00
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public interface MyFragContract {

    interface View extends BaseView {
        void showDatas(List<String> data);
    }

    interface Presenter extends BasePresenterInter<View> {
        void fetch();
    }
}
