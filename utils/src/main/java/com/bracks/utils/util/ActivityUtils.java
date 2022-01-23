package com.bracks.utils.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.IntentUtils;

/**
 * good programmer.
 *
 * @date : 2020-10-17 10:01
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class ActivityUtils {

    private ActivityUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Start the activity.
     *
     * @param context context
     * @param intent  The description of the activity to start
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean startActivity(@NonNull final Context context,
                                        @NonNull final Intent intent) {
        return startActivity(context, intent, null);
    }

    /**
     * Start the activity.
     *
     * @param context context
     * @param clz     The activity class.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean startActivity(@NonNull final Context context,
                                        @NonNull final Class<? extends Activity> clz) {
        Intent intent = new Intent(context, clz);
        intent.setComponent(new ComponentName(context.getPackageName(), clz.getName()));
        return startActivity(context, intent, null);
    }

    /**
     * Start the activity.
     *
     * @param intent  The description of the activity to start.
     * @param options Additional options for how the Activity should be started.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean startActivity(@NonNull final Context context,
                                        @NonNull final Intent intent,
                                        @Nullable final Bundle options) {
        if (!IntentUtils.isIntentAvailable(intent)) {
            Log.e("ActivityUtils", "intent is unavailable");
            return false;
        }
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (options != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            context.startActivity(intent, options);
        } else {
            context.startActivity(intent);
        }
        return true;
    }
}