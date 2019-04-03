package com.bracks.futia.mylib.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Looper;
import android.view.View;

import com.blankj.utilcode.util.ProcessUtils;
import com.bracks.futia.mylib.utils.log.TLog;

import java.security.Security;
import java.util.List;


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

    private static boolean debug;


    private CommonUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static boolean init(Context context) {
        mContext = context.getApplicationContext();
        debug = context.getApplicationInfo() != null && (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        return appOneInit();
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

    /**
     * 是否debug模式
     *
     * @return
     */
    public static boolean isDebug() {
        return debug;
    }

    /**
     * Application单次初始化：防止多次调用
     *
     * @Data : 2017-11-06  下午 08:57
     * @Author: futia
     * @Description:
     */
    public static boolean appOneInit() {
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

    /*****************************************************************************************************************/

    /**
     * 判断当前线程是否在主线程
     *
     * @return
     */
    public static boolean isMainThread() {
        //return Thread.currentThread() == Looper.getMainLooper().getThread();
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * 检查集合
     *
     * @param list
     * @return
     */
    public static boolean checkList(List list) {
        if (list == null) {
            return false;
        }
        if (list.size() <= 0) {
            return false;
        }
        return true;
    }

    /**
     * 测量View的宽高
     *
     * @param view View
     */
    public static void measureWidthAndHeight(View view) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 多次点击一定时间内取第一次点击事件
     */
    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        return isFastDoubleClick(500L);
    }

    public static boolean isFastDoubleClick(long clickDelayTime) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime < clickDelayTime) {
            return false;
        }
        lastClickTime = currentTime;
        return true;
    }
}
