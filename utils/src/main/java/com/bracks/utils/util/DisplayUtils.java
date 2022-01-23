package com.bracks.utils.util;

import android.app.Activity;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.util.Log;
import android.view.Display;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.ActivityUtils;

import java.util.Locale;

/**
 * good programmer.
 *
 * @date : 2020-12-23 14:56
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class DisplayUtils {
    private static final String TAG = "DisplayUtils";

    /**
     * @param context
     * @return Display[0]: primary diaplay;Display[1]: secondary diaplay
     */
    public static Display[] getDisplay(Context context) {
        DisplayManager displayManager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        Display[] displays = displayManager.getDisplays();
        for (int i = 0; i < displays.length; i++) {
            Log.i(TAG, String.format(Locale.getDefault(), "find display[%d] : %s", i, displays[i].toString()));
        }
        return displays;
    }

    @Nullable
    public static Display getDisplay(Context context, int displayIndex) {
        DisplayManager displayManager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        Display[] displays = displayManager.getDisplays();
        if (displays.length > displayIndex) {
            Log.i(TAG, "find multi diaplay  : " + displays[displayIndex].toString());
            return displays[displayIndex];
        }
        return null;
    }

    @Nullable
    public static Display getSecondaryDisplay(Context context) {
        Display[] displays = getDisplay(context);
        return displays.length > 1 ? displays[1] : null;
    }

    public static int getCurDisplayId() {
        Activity topActivity = ActivityUtils.getTopActivity();
        if (topActivity == null) {
            return Display.DEFAULT_DISPLAY;
        } else {
            Display display = topActivity.getWindow().getDecorView().getDisplay();
            int displayId = display != null ? display.getDisplayId() : Display.DEFAULT_DISPLAY;
            Log.i(TAG, "getCurDisplayId : " + displayId);
            return displayId;
        }
    }

    public static int getDisplayId(@NonNull Activity activity) {
        Display display = activity.getWindow().getDecorView().getDisplay();
        int displayId = display != null ? display.getDisplayId() : Display.DEFAULT_DISPLAY;
        Log.i(TAG, "getDisplayId : " + displayId);
        return displayId;
    }

    public static boolean curDisplayIsDefault() {
        return curDisplayIsDefault(getCurDisplayId());
    }


    public static boolean curDisplayIsDefault(@NonNull Activity activity) {
        return curDisplayIsDefault(getDisplayId(activity));
    }

    public static boolean curDisplayIsDefault(int curDisplayId) {
        return curDisplayId == Display.DEFAULT_DISPLAY;
    }
}