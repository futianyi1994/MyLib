package com.bracks.utils.util;

import android.content.Context;

/**
 * good programmer.
 *
 * @date : 2020/2/29 16:33
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class ACacheHelper {

    private static ACache cache;

    public static void init(Context context, String name) {
        if (cache == null) {
            synchronized (ACacheHelper.class) {
                cache = ACache.get(context.getApplicationContext(), name);
            }
        }
    }

    public static ACache instance() {
        return cache;
    }
}
