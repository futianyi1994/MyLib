package com.bracks.futia.mylib.base.interf;

import android.app.ProgressDialog;

/**
 * good programmer.
 *
 * @date : 2019-01-24 下午 02:43
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public interface BaseDialogInterf {

    void hideWaitDialog();

    ProgressDialog showWaitDialog();

    ProgressDialog showWaitDialog(int resid);

    ProgressDialog showWaitDialog(String text);
}
