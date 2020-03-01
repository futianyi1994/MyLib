package com.bracks.mylib.base;

import android.content.Intent;
import android.support.multidex.MultiDexApplication;

import com.blankj.utilcode.util.Utils;
import com.bracks.mylib.utils.CommonUtils;
import com.bracks.mylib.utils.TLog;


/**
 * good programmer.
 *
 * @date : 2019-01-22 下午 03:04
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public abstract class BaseApp extends MultiDexApplication {
    public static final String TAG = "BaseApp";
    /**
     * 表示应用是被杀死后在启动的
     */
    public final static int APP_STATUS_KILLED = 0;
    /**
     * 表示应用时正常的启动流程
     */
    public final static int APP_STATUS_NORMAL = 1;
    /**
     * <p>Example:
     * <pre><code>
     * 记录App的启动状态
     * //非正常启动流程，直接重新初始化应用界面(缺点：第一次一定会重启)
     * if (BaseApp.APP_STATUS != BaseApp.APP_STATUS_NORMAL) {
     *     //App正常的启动，设置App的启动状态为正常启动
     *     BaseApp.APP_STATUS = BaseApp.APP_STATUS_NORMAL;
     *     BaseApp.reInitApp(HomeActivity.class);
     *     return;
     * } else {
     *     //正常启动流程
     * }
     * </code></pre>
     */
    public static int APP_STATUS = APP_STATUS_KILLED;

    @Override
    public void onCreate() {
        super.onCreate();
        onCreate(CommonUtils.init(this));
    }

    /**
     * 判断是否App进程启动
     *
     * @param isAppCreate true: App进程启动；false：service进程启动等
     */
    protected abstract void onCreate(boolean isAppCreate);

    /**
     * 重新初始化应用界面，清空当前Activity棧，并启动首页
     *
     * @param cls
     */
    public static void reInitApp(Class<?> cls) {
        Intent intent = new Intent(Utils.getApp(), cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        Utils.getApp().startActivity(intent);
        TLog.i(TAG, "reInitApp");
    }

}
