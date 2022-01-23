package com.bracks.utils.util;

import android.app.Activity;
import android.view.View;
import android.view.Window;

import androidx.fragment.app.Fragment;

/**
 * good programmer.
 *
 * @date : 2020-04-08 14:54
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class FindViewUtlis {
    private FindViewUtlis() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Look for a view with the given id.
     *
     * @param object
     * @param id
     * @param <V>
     * @return
     */
    public static <V extends View> V findViewById(Object object, int id) {
        if (object instanceof View) {
            return (V) ((View) object).findViewById(id);
        } else if (object instanceof Activity) {
            return (V) ((Activity) object).findViewById(id);
        } else if (object instanceof Window) {
            return (V) ((Window) object).findViewById(id);
        } else if (object instanceof Fragment) {
            return (V) ((Fragment) object).getView().findViewById(id);
        }
        return null;
    }
}
