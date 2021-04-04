package com.bracks.mylib.utils;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * good programmer.
 *
 * @date : 2019-04-01 下午 02:52
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class BarUtils {


    /************************************************************状态栏文字*****************************************************/


    /**
     * 修改状态栏文字颜色，这里小米，魅族区别对待：小米的MIUI和魅族的Flyme在Android 4.4之后各自提供了自家的修改方法，
     * 其他品牌只能在Android 6.0及以后才能修改
     *
     * @param activity
     * @param dark
     */
    public static void setLightStatusBar(final Activity activity, final boolean dark) {
        //小米的MIUI和魅族的Flyme在Android 4.4之后各自提供了自家的修改方法
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setAndroidNativeLightStatusBar(activity, dark);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (com.blankj.utilcode.util.RomUtils.isXiaomi()) {
                setMIUILightStatusBar(activity, dark);
            } else if (com.blankj.utilcode.util.RomUtils.isMeizu()) {
                setFlymeLightStatusBar(activity, dark);
            }
        }
    }

    /**
     * MIUI系统设置状态栏亮色
     * 开发版 7.7.13 及以后版本采用了系统API，此方法无效但不会报错，所以两个方式都要加上
     *
     * @param activity
     * @param dark
     * @return
     */
    private static boolean setMIUILightStatusBar(Activity activity, boolean dark) {
        boolean result = false;
        Window window = activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    //状态栏透明且黑色字体
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
                } else {
                    //清除黑色字体
                    extraFlagField.invoke(window, 0, darkModeFlag);
                }
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Flyme系统设置状态栏亮色
     *
     * @param activity
     * @param dark
     * @return
     */
    private static boolean setFlymeLightStatusBar(Activity activity, boolean dark) {
        boolean result = false;
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 原生系统设置状态栏亮色
     *
     * @param activity
     * @param dark
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private static void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
        View decor = activity.getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }


    /**********************************************************NavigationBar***************************************************/


    /**
     * 隐藏导航栏
     *
     * @param activity
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    public static void hideNavBar(Activity activity) {
        hideNavBar(activity.getWindow());
    }

    /**
     * 隐藏导航栏
     *
     * @param window
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    public static void hideNavBar(Window window) {
        WindowManager.LayoutParams params = window.getAttributes();
        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                //STICKY为粘性：边缘滑动暂时退出隐藏会有一段时间继续显示，然后隐藏不触发OnSystemUiVisibilityChangeListener
                //| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                //边缘滑动暂时退出隐藏并且马上又重新隐藏触发OnSystemUiVisibilityChangeListener
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                //保持整个View稳定, 常和控制System UI悬浮, 隐藏的Flags共用, 使View不会因为System UI的变化而重新layout
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        window.setAttributes(params);
    }

    /**
     * 隐藏导航栏
     *
     * @param view
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    public static void hideNavBar(View view) {
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                //STICKY为粘性：边缘滑动暂时退出隐藏会有一段时间继续显示，然后隐藏不触发OnSystemUiVisibilityChangeListener
                //| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                //边缘滑动暂时退出隐藏并且马上又重新隐藏触发OnSystemUiVisibilityChangeListener
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                //保持整个View稳定, 常和控制System UI悬浮, 隐藏的Flags共用, 使View不会因为System UI的变化而重新layout
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        view.setSystemUiVisibility(uiOptions);
    }
}
