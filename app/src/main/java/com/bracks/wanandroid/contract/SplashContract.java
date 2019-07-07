package com.bracks.wanandroid.contract;

import com.bracks.mylib.base.basemvp.BasePresenterInter;
import com.bracks.mylib.base.basemvp.BaseView;


/**
 * good programmer.
 *
 * @date : 2019-06-12 下午 05:00
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public interface SplashContract {

    interface View extends BaseView {
        /**
         * after some time jump to main page
         */
        void jumpToMain();
    }

    interface Presenter extends BasePresenterInter<View> {

        void jumpToMain();
    }
}
