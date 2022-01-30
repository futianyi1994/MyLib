package com.bracks.player.play;

import android.animation.ValueAnimator;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bracks.utils.util.VolumeUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * good programmer.
 *
 * @date : 2021-11-02 16:35
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class EffectUtils {
    private static final long FADE_IN_DURATION = 2000;
    private static final ValueAnimator ANIM_FADE_IN = ValueAnimator.ofFloat(0, 1);
    private static final List<AnimatorUpdateListener> LISTENERS = new ArrayList<>();
    private static ValueAnimator.AnimatorUpdateListener animFadeInUpdateListener;
    private static float fadeInLastValue;

    static {
        ANIM_FADE_IN.setDuration(FADE_IN_DURATION);
        ANIM_FADE_IN.setInterpolator(new AccelerateInterpolator());
        if (animFadeInUpdateListener == null) {
            animFadeInUpdateListener = animation -> {
                float currentValue = Math.round((float) animation.getAnimatedValue() * 100) / 100.0f;
                if (fadeInLastValue != currentValue) {
                    for (Iterator<AnimatorUpdateListener> iterator = LISTENERS.iterator(); iterator.hasNext(); ) {
                        AnimatorUpdateListener animatorUpdateListener = iterator.next();
                        animatorUpdateListener.onAnimationUpdate(currentValue);
                    }
                    fadeInLastValue = currentValue;
                }
            };
            ANIM_FADE_IN.addUpdateListener(animFadeInUpdateListener);
        }
    }

    public static void fadeIn(@NonNull AnimatorUpdateListener listener) {
        endFadeAnim(listener);
        LISTENERS.add(listener);
        listener.onAnimationUpdate(VolumeUtils.MIN_VOLUME_C11);
        ANIM_FADE_IN.start();
    }

    public static void endFadeAnim(@Nullable AnimatorUpdateListener listener) {
        remove(listener);
        if (ANIM_FADE_IN.isRunning()) {
            ANIM_FADE_IN.end();
        }
    }

    public static void remove(@Nullable AnimatorUpdateListener listener) {
        if (listener != null) {
            LISTENERS.remove(listener);
        } else {
            LISTENERS.clear();
        }
        fadeInLastValue = 0;
    }

    public interface AnimatorUpdateListener {
        void onAnimationUpdate(float value);
    }
}
