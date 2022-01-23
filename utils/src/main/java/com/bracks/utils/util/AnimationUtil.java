package com.bracks.utils.util;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;


/**
 * good programmer.
 *
 * @date : 2020/6/19 16:18
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class AnimationUtil {

    /**
     * 开始加载
     */
    public static void startLoading(ImageView loading, @DrawableRes int resId) {
        Drawable drawable = loading.getDrawable();
        if (drawable instanceof AnimationDrawable) {
            AnimationDrawable ad = (AnimationDrawable) drawable;
            if (!ad.isRunning()) {
                ad.start();
            }
        } else {
            loading.setImageResource(resId);
            AnimationDrawable ad = (AnimationDrawable) loading.getDrawable();
            ad.start();
        }
    }

    /**
     * 停止加载
     */
    public static void stopLoading(ImageView loading) {
        Drawable drawable = loading.getDrawable();
        if (drawable instanceof AnimationDrawable) {
            AnimationDrawable ad = (AnimationDrawable) drawable;
            if (ad.isRunning()) {
                ad.stop();
            }
        }
    }

    public static ObjectAnimator rotation(Object target, long duration, int repeatCount, TimeInterpolator value, float... values) {
        @SuppressLint("ObjectAnimatorBinding") ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(target, "rotation", values);
        objectAnimator.setInterpolator(value);
        objectAnimator.setDuration(duration);
        objectAnimator.setRepeatCount(repeatCount);
        objectAnimator.start();
        return objectAnimator;
    }
}
