package com.bracks.utils.manager;

import android.app.Activity;

import java.lang.ref.WeakReference;
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
    private static Stack<WeakReference<Activity>> activityStack;
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
    public void addActivity(WeakReference<Activity> activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement().get();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        WeakReference<Activity> activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的），带有动画
     */
    public void finishActivity(int enterAnim, int exitAnim) {
        WeakReference<Activity> activity = activityStack.lastElement();
        finishActivity(activity, enterAnim, exitAnim);
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (WeakReference<Activity> activity : activityStack) {
            if (activity.get().getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束指定类名的Activity,带有动画
     */
    public void finishActivity(Class<?> cls, int enterAnim, int exitAnim) {
        for (WeakReference<Activity> activity : activityStack) {
            if (activity.get().getClass().equals(cls)) {
                finishActivity(activity, enterAnim, exitAnim);
                break;
            }
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(WeakReference<Activity> activity) {
        if (activity != null && !activity.get().isFinishing()) {
            activityStack.remove(activity);
            activity.get().finish();
            activity = null;
        }
    }

    /**
     * 结束指定的Activity,带有动画
     */
    public void finishActivity(WeakReference<Activity> activity, int enterAnim, int exitAnim) {
        if (activity != null && !activity.get().isFinishing()) {
            activityStack.remove(activity);
            activity.get().finish();
            activity.get().overridePendingTransition(enterAnim, exitAnim);
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

                activityStack.get(i).get().finish();
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
            for (WeakReference<Activity> activity : activityStack) {
                if (activity.get().getClass().equals(cls)) {
                    return activity.get();
                }
            }
        }
        return null;
    }
}
