package com.bracks.mylib.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ProcessUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;

import java.security.Security;


/**
 * good programmer.
 *
 * @date : 2019-01-02 上午 11:55
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class CommonUtils {
    public static final String TAG = "CommonUtils";

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;
    private static Runnable runnable;
    private static Handler handler = new Handler();

    private CommonUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static boolean init(Application context) {
        Utils.init(context);
        mContext = context.getApplicationContext();
        return isAppCreate();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (mContext != null) {
            return mContext;
        }
        throw new NullPointerException("u should init first");
    }

    /*****************************************************************************************************************/

    /**
     * Application单次初始化：防止多次调用
     *
     * @Data : 2017-11-06  下午 08:57
     * @Author: futia
     * @Description:
     */
    public static boolean isAppCreate() {
        String processAppName = ProcessUtils.getCurrentProcessName();
        TLog.i(TAG, "processAppName:  " + processAppName);
        //默认的app会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就return掉
        if (processAppName == null || !processAppName.equalsIgnoreCase(mContext.getPackageName())) {
            TLog.i(TAG, "enter the service process!");
            // 则此application::onCreate 是被service 调用的，直接返回
            return false;
        }
        return true;
    }

    /**
     * 防止首次安装点击home键重新实例化
     *
     * @param activity
     * @return true走正常流程。一般在启动界面的onCreate方法中加入以下代码
     */
    public static boolean isTaskRoot(Activity activity) {
        if (!activity.isTaskRoot()) {
            Intent intent = activity.getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    activity.finish();
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 清除DNS缓存
     * http://www.tuicool.com/articles/riy2Az
     */
    public static void setDNSCaches() {
        Security.setProperty("networkaddress.cache.ttl", String.valueOf(10));
        Security.setProperty("networkaddress.cache.negative.ttl", String.valueOf(10));
    }

    public static void clearExitFlag() {
        isExit = false;
        handler.removeCallbacks(runnable);
    }

    public static void exitBy2Click() {
        exitBy2Click("再按一次退出程序");
    }

    public static void exitBy2Click(String exitText) {
        exitBy2Click(exitText, true, 2000);
    }

    public static void exitBy2ClickNoTip() {
        exitBy2Click("", false, 2000);
    }

    public static void exitBy2Click(String exitText, boolean isToast, long delayMillis) {
        if (!isExit) {
            isExit = true;
            if (isToast) {
                ToastUtils.showLong(exitText);
            }
            runnable = () -> isExit = false;
            handler.postDelayed(runnable, delayMillis);
        } else {
            AppUtils.exitApp();
        }
    }
}
