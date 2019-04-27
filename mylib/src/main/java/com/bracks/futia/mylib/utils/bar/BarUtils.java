package com.bracks.futia.mylib.utils.bar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bracks.futia.mylib.utils.device.RomUtils;

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
     * 修改状态栏文字颜色，这里小米，魅族区别对待
     *
     * @param activity
     * @param dark
     */
    public static void setLightStatusBar(final Activity activity, final boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            switch (RomUtils.getLightStatusBarAvailableRomType()) {
                case RomUtils.MIUI:
                    setMIUILightStatusBar(activity, dark);
                    break;
                case RomUtils.FLYME:
                    setFlymeLightStatusBar(activity, dark);
                    break;
                case RomUtils.ANDROID_NATIVE:
                    setAndroidNativeLightStatusBar(activity, dark);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * MIUI系统设置状态栏亮色
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

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && RomUtils.isMiUIV7OrAbove()) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (dark) {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                    }
                }
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
    @TargetApi(Build.VERSION_CODES.M)
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
     * 获取是否存在NavigationBar
     * 特定情况下有效的，比如虚拟按键是固定的，不能隐藏的，该方法有效
     *
     * @param context
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNavigationBar;
    }

    /**
     * 获取是否存在NavigationBar
     * 通过获取不同状态的屏幕高度对比判断是否有NavigationBar
     *
     * @param windowManager
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean checkDeviceHasNavigationBar(WindowManager windowManager) {
        Display d = windowManager.getDefaultDisplay();
        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);
        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);
        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;
        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }

    /**
     * 全屏并隐藏底部navigator
     *
     * @param activity
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    public static void fullScreenAndHideNavBar(Activity activity) {
        int currentApiVersion = Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        //This work only for android 4.4+
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = activity.getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        decorView.setSystemUiVisibility(flags);
                    }
                }
            });
        } else if (currentApiVersion > Build.VERSION_CODES.HONEYCOMB && currentApiVersion < Build.VERSION_CODES.KITKAT) {
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        }
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

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
