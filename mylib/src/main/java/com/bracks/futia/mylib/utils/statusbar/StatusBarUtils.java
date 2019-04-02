package com.bracks.futia.mylib.utils.statusbar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.FloatRange;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.bracks.futia.mylib.utils.device.RomUtils;
import com.bracks.futia.mylib.utils.log.TLog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * good programmer.
 *
 * @date : 2019-02-20 上午 10:04
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class StatusBarUtils {

    public static final String TAG = "StatusBarUtils";

    /**
     * 判断是否有StatusBar
     *
     * @param activity
     * @return
     */
    public static boolean hasStatusBar(Activity activity) {
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        if ((attrs.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 得到StatusBar高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        // 默认为38，貌似大部分是这样的
        int x = 0, sbar = 38;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    /**
     * 得到StatusBar高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight1(Context context) {
        int result = 24;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelSize(resId);
        } else {
            result = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    result, Resources.getSystem().getDisplayMetrics());
        }
        return result;
    }

    /**
     * 显示状态栏
     *
     * @param activity
     */
    public void showStatusBar(Activity activity) {
        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        uiFlags |= 0x00001000;
        activity.getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        TLog.d(TAG, "Launcher showStatusBar()");
    }

    /**
     * 隐藏状态栏
     *
     * @param activity
     */
    public void hideStatusBar(Activity activity) {
        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        uiFlags |= 0x00001000;
        activity.getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        TLog.d(TAG, "Launcher hideStatusBar()");
    }

    /**
     * 增加View的paddingTop,增加的值为状态栏高度
     *
     * @param context
     * @param view
     */
    public static void setPadding(Context context, View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatusBarHeight(context),
                    view.getPaddingRight(), view.getPaddingBottom());
        }
    }

    /**
     * 增加View的高度以及paddingTop,增加的值为状态栏高度.一般是在沉浸式全屏给ToolBar用的
     *
     * @param context
     * @param view
     */
    public static void setHeightAndPadding(Context context, View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            //增高
            lp.height += getStatusBarHeight(context);
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatusBarHeight(context),
                    view.getPaddingRight(), view.getPaddingBottom());
        }
    }

    /**
     * 增加View上边距（MarginTop）一般是给高度为 WARP_CONTENT 的小控件用的
     *
     * @param context
     * @param view
     */
    public static void setMargin(Context context, View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp instanceof ViewGroup.MarginLayoutParams) {
                //增高
                ((ViewGroup.MarginLayoutParams) lp).topMargin += getStatusBarHeight(context);
            }
            view.setLayoutParams(lp);
        }
    }

    /**
     * 状态栏透明
     *
     * @param activity
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void transparencyBar(Activity activity) {
        //5.0及以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4-5.0
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            //小于4.4
            TLog.d(TAG, "系统版本号小于4.4,不能修改状态栏颜色...");
        }
    }

    /**
     * 沉浸式状态栏可改变透明度，支持4.4以上版本
     *
     * @param activity
     */
    public static void immersive(Activity activity) {
        immersive(activity.getWindow(), 0, 0);
    }

    /**
     * 沉浸式状态栏可改变透明度，支持4.4以上版本
     *
     * @param activity
     * @param color
     * @param alpha
     */
    public static void immersive(Activity activity, int color, @FloatRange(from = 0.0, to = 1.0) float alpha) {
        immersive(activity.getWindow(), color, alpha);
    }

    /**
     * 沉浸式状态栏可改变透明度，支持4.4以上版本
     *
     * @param activity
     * @param color
     */
    public static void immersive(Activity activity, int color) {
        immersive(activity.getWindow(), color, 1f);
    }

    /**
     * 沉浸式状态栏可改变透明度，支持4.4以上版本
     *
     * @param window
     */
    public static void immersive(Window window) {
        immersive(window, 0, 0);
    }

    /**
     * 沉浸式状态栏可改变透明度，支持4.4以上版本
     *
     * @param window
     * @param color
     */
    public static void immersive(Window window, int color) {
        immersive(window, color, 1f);
    }

    /**
     * 沉浸式状态栏可改变透明度，支持4.4以上版本
     *
     * @param window
     * @param color
     * @param alpha
     */
    public static void immersive(Window window, int color, @FloatRange(from = 0.0, to = 1.0) float alpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(mixtureColor(color, alpha));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            setTranslucentView((ViewGroup) window.getDecorView(), color, alpha);
        } else if (Build.VERSION.SDK_INT > 16) {
            int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            window.getDecorView().setSystemUiVisibility(systemUiVisibility);
        }
    }

    /**
     * 修改状态栏颜色，支持4.4以上版本
     *
     * @param activity
     * @param colorId
     */
    public static void setStatusBarColor(Activity activity, int colorId) {
        //5.0及以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(colorId));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4-5.0
            //使用SystemBarTint库使4.4版本状态栏变色，需要先将状态栏设置为透明
            transparencyBar(activity);
            //若需要4.4系统状态栏变色需要导入SystemBarTint库
            //SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            //tintManager.setStatusBarTintEnabled(true);
            //tintManager.setStatusBarTintResource(colorId);
        }
    }

    /**
     * 创建假的透明栏
     *
     * @param container
     * @param color
     * @param alpha
     */
    public static void setTranslucentView(ViewGroup container, int color, @FloatRange(from = 0.0, to = 1.0) float alpha) {
        if (Build.VERSION.SDK_INT >= 19) {
            int mixtureColor = mixtureColor(color, alpha);
            View translucentView = container.findViewById(android.R.id.custom);
            if (translucentView == null && mixtureColor != 0) {
                translucentView = new View(container.getContext());
                translucentView.setId(android.R.id.custom);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(container.getContext()));
                container.addView(translucentView, lp);
            }
            if (translucentView != null) {
                translucentView.setBackgroundColor(mixtureColor);
            }
        }
    }

    /**
     * 根据颜色和透明度计算颜色值
     *
     * @param color
     * @param alpha
     * @return
     */
    public static int mixtureColor(int color, @FloatRange(from = 0.0, to = 1.0) float alpha) {
        int a = (color & 0xff000000) == 0 ? 0xff : color >>> 24;
        return (color & 0x00ffffff) | (((int) (a * alpha)) << 24);
    }


    /**********************************************状态栏文字**************************************************/


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
}
