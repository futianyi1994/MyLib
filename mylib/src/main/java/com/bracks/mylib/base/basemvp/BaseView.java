package com.bracks.mylib.base.basemvp;

/**
 * good programmer.
 *
 * @date : 2019-01-23 下午 02:59
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public interface BaseView {
    /**
     * 显示loading
     *
     * @param msg          dialog标题
     * @param isCancelable 能否点击取消dialog
     */
    void showLoading(String msg, boolean isCancelable);

    /**
     * 关闭loading
     */
    void hideLoading();

    /**
     * 显示吐司
     *
     * @param msg 吐司内容
     */
    void showToast(String msg);

}
