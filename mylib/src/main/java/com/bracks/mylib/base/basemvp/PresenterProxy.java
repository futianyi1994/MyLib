package com.bracks.mylib.base.basemvp;

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
     * 设置创建Presenter的工厂
     *
     * @param presenterFactory PresenterFactory类型
     */
    void setPresenterFactory(PresenterFactory<V, P> presenterFactory);

    /**
     * 获取Presenter的工厂类
     *
     * @return 返回PresenterMvpFactory类型
     */
    PresenterFactory<V, P> getPresenterFactory();

    /**
     * 获取创建的Presenter
     *
     * @return 指定类型的Presenter
     */
    P getPresenter();

}