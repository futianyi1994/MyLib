package com.bracks.futia.mylib.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.bracks.futia.mylib.utils.CommonUtils;
import com.bracks.futia.mylib.utils.log.TLog;


/**
 * good programmer.
 *
 * @date : 2019-01-22 下午 03:04
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class BaseApp extends Application {
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
     * 记录App的启动状态
     *  //非正常启动流程，直接重新初始化应用界面(缺点：第一次一定会重启)
     *  if (BaseApp.APP_STATUS != BaseApp.APP_STATUS_NORMAL) {
     *      //App正常的启动，设置App的启动状态为正常启动
     *      BaseApp.APP_STATUS = BaseApp.APP_STATUS_NORMAL;
     *      BaseApp.reInitApp(HomeActivity.class);
     *      return;
     *  } else {
     *      //正常启动流程
     *  }
     */
    public static int APP_STATUS = APP_STATUS_KILLED;

    @SuppressLint("StaticFieldLeak")
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        CommonUtils.init(mContext);
    }

    /**
     * 重新初始化应用界面，清空当前Activity棧，并启动首页
     */
    public static void reInitApp(Class<?> cls) {
        Intent intent = new Intent(mContext, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        TLog.i(TAG, "reInitApp");
    }

}
