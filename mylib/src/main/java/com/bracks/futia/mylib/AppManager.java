package com.bracks.futia.mylib;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.bracks.futia.mylib.utils.log.TLog;

import java.util.List;
import java.util.Stack;

/**
 * good programmer.
 *
 * @date : 2018/10/25 14:47
 * @author: futia
 * @email : futianyi1994@126.com
 * @description : activity堆栈式管理
 */
public class AppManager {

    private static final String TAG = "AppManager";
    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的），带有动画
     */
    public void finishActivity(int enterAnim, int exitAnim) {
        Activity activity = activityStack.lastElement();
        finishActivity(activity, enterAnim, exitAnim);
    }


    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束指定类名的Activity,带有动画
     */
    public void finishActivity(Class<?> cls, int enterAnim, int exitAnim) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity, enterAnim, exitAnim);
                break;
            }
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定的Activity,带有动画
     */
    public void finishActivity(Activity activity, int enterAnim, int exitAnim) {
        if (activity != null && !activity.isFinishing()) {
            activityStack.remove(activity);
            activity.finish();
            activity.overridePendingTransition(enterAnim, exitAnim);
            activity = null;
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                //finishActivity方法中的activity.isFinishing()方法会导致某些activity无法销毁
                //貌似跳转的时候最后一个activity 是finishing状态，所以没有执行
                //内部实现不是很清楚，但是实测结果如此，使用下面代码则没有问题
                // find by TopJohn
                //finishActivity(activityStack.get(i));

                activityStack.get(i).finish();
                //break;
            }
        }
        activityStack.clear();
    }

    /**
     * 获取指定的Activity
     *
     * @author kymjs
     */
    public Activity getActivity(Class<?> cls) {
        if (activityStack != null) {
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        }
        return null;
    }

    /**
     * 退出应用程序
     */
    public void appExit() {
        try {
            finishAllActivity();
            // 杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
        }
    }


    /**
     * 双击退出函数
     */
    private Boolean isExit = false;

    public void exitBy2Click(Context context) {
        if (!isExit) {
            isExit = true;
            // 准备退出
            Toast.makeText(context, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 取消退出 .如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
                    isExit = false;
                }
            }, 2000);
        } else {
            appExit();
        }
    }

    /**
     * 当前应用处于前台还是后台
     *
     * @param context
     * @return
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    TLog.i(TAG, "后台：" + appProcess.processName);
                    return true;
                } else {
                    TLog.i(TAG, "前台：" + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }


}
