package com.bracks.mylib.base.basemvp;

/**
 * good programmer.
 *
 * @date : 2019-01-23 下午 03:35
 * @author: futia
 * @email : futianyi1994@126.com
 * @description : Presenter工厂接口
 */
public interface PresenterFactory<V extends BaseView, P extends BasePresenter<V>> {
    /**
     * 创建Presenter的接口方法
     *
     * @return 需要创建的Presenter
     */
    P createPresenter();
}