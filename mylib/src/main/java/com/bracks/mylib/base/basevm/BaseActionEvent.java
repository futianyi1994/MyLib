package com.bracks.mylib.base.basevm;


import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * good programmer.
 *
 * @date : 2019-02-14 下午 05:46
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class BaseActionEvent extends BaseEvent {

    public static final int SHOW_LOADING_DIALOG = 1;

    public static final int DISMISS_LOADING_DIALOG = 2;

    public static final int SHOW_TOAST = 3;

    public static final int FINISH = 4;

    public static final int FINISH_WITH_RESULT_OK = 5;


    @IntDef({SHOW_LOADING_DIALOG, DISMISS_LOADING_DIALOG, SHOW_TOAST, FINISH, FINISH_WITH_RESULT_OK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ActionEvent {
    }

    private String message;
    /**
     * 能否点击取消dialog
     */
    private boolean isCancelable = true;

    public BaseActionEvent(@ActionEvent int action) {
        super(action);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        isCancelable = true;
    }

    public void setMessage(String message, boolean cancelable) {
        this.message = message;
        isCancelable = cancelable;
    }

    public boolean isCancelable() {
        return isCancelable;
    }

}