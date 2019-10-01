package com.bracks.mylib.base.basemvp;

import android.os.Bundle;

/**
 * good programmer.
 *
 * @date : 2019-01-23 下午 03:46
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :代理接口
 */
public interface PresenterProxy<V extends BaseView, P extends BasePresenter<V>> {
    /**
     * 创建Presenter的工厂类,这个方法只能在创建Presenter之前调用,也就是调用createPresenter(V v)之前，覆盖默认工厂方法。
     * 如果Presenter已经创建则不能再修改
     *
     * @param presenterFactory PresenterFactory类型
     */
    void createPresenterFactory(PresenterFactory<P> presenterFactory);

    /**
     * 获取创建的Presenter工厂类
     *
     * @return 返回创建的Presenter工厂类
     */
    PresenterFactory<P> getPresenterFactory();

    /**
     * 创建Presenter
     * 如果之前创建过，而且是意外销毁则从Bundle中恢复
     *
     * @param v mvpView
     * @return Presenter
     */
    P createPresenter(V v);

    /**
     * 获取创建的Presenter
     *
     * @return 返回创建的Presenter
     */
    P getPresenter();

    /**
     * 销毁Presenter
     */
    void destroyPresenter();

    /**
     * 存储Presenter意外销毁时的Bundle，它的调用时机和Activity、Fragment、View中的onSaveInstanceState时机相同
     *
     * @return Presenter意外销毁时的Bundle
     */
    Bundle onSaveInstanceState();

    /**
     * 恢复意外销毁Presenter时的存储的Bundle
     *
     * @param savedInstanceState Presenter意外销毁时存储的Bundle
     * @return
     */
    void onRestoreInstanceState(Bundle savedInstanceState);
}