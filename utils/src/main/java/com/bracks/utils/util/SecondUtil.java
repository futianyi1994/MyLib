package com.bracks.utils.util;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

/**
 * good programmer.
 *
 * @date : 2020-05-18 20:56
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class SecondUtil {
    /**
     * 显示时分秒
     */
    public static final int TYPE_HOURS = 1;
    /**
     * 显示分秒
     */
    public static final int TYPE_MINUTES = 2;
    /**
     * 显示秒
     */
    public static final int TYPE_SECONDS = 3;

    public static String format(long second) {
        if (second <= 0) {
            return String.format(Locale.getDefault(), "%02d:%02d", 0, 0);
        }
        long seconds = second % 60;
        long minutes = (second / 60) % 60;
        long hours = second / 3600;
        if (minutes == 0) {
            return String.format(Locale.getDefault(), "%02d:%02d", 0, seconds);
        } else if (hours == 0) {
            return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        } else {
            return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds);
        }
    }

    public static String format(long second, @TIME_TYPE int type) {
        long seconds = second % 60;
        long minutes = (second / 60) % 60;
        long hours = second / 3600;
        switch (type) {
            case TYPE_HOURS:
                return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds);
            case TYPE_SECONDS:
                return String.format(Locale.getDefault(), "%02d:%02d", 0, seconds);
            case TYPE_MINUTES:
            default:
                return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }
    }

    /**
     * 根据hash获取的歌曲时长单位是s
     *
     * @param second 默认单位为s
     * @return
     */
    public static int getTimeType(long second) {
        if (second <= 0) {
            return TYPE_MINUTES;
        }

        long seconds = second % 60;
        long minutes = (second / 60) % 60;
        long hours = second / 3600;
        if (minutes == 0) {
            return TYPE_SECONDS;
        } else if (hours == 0) {
            return TYPE_MINUTES;
        } else {
            return TYPE_HOURS;
        }
    }

    @IntDef({TYPE_HOURS, TYPE_MINUTES, TYPE_SECONDS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TIME_TYPE {
    }
}
