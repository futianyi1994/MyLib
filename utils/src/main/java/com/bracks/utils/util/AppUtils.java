package com.bracks.utils.util;

import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;

import java.util.List;

/**
 * good programmer.
 *
 * @date : 2019-11-15 17:48
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class AppUtils {
    private static final String TAG = "AppUtils";

    public static boolean openMainApp(@NonNull Context context, @NonNull String packageName) {
        if (checkApplication(packageName)) {
            Intent localIntent = new Intent(Intent.ACTION_MAIN, null);
            localIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> appList = Utils.getApp().getPackageManager().queryIntentActivities(localIntent, 0);
            for (int i = 0; i < appList.size(); i++) {
                ResolveInfo resolveInfo = appList.get(i);
                String mPackageName = resolveInfo.activityInfo.packageName;
                String mClassName = resolveInfo.activityInfo.name;
                if (packageName.equals(mPackageName)) {
                    return openMainApp(context, mPackageName, mClassName);
                }
            }
        } else {
            ToastUtils.showLong("没有找到这个APP");
        }
        return false;
    }

    public static boolean openMainApp(@NonNull Context context, @NonNull String packageName, @NonNull String className) {
        if (checkApplication(packageName)) {
            if (!(className.contains("test") || className.contains("Test"))) {
                ComponentName cn = new ComponentName(packageName, className);
                if (className.contains("Vice")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Intent intent = new Intent();
                        ActivityOptions options = ActivityOptions.makeBasic();
                        options.setLaunchDisplayId(1);
                        intent.setComponent(cn);
                        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent, options.toBundle());
                        Log.v(TAG, "openAppByPackageName: " + className);
                        return true;
                    }
                } else {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setComponent(cn);
                    context.startActivity(intent);
                    Log.v(TAG, "openAppByPackageName: " + className);
                    return true;
                }
            }
        } else {
            Log.e(TAG, "openAppByPackageName: not find " + className);
        }
        return false;
    }

    /**
     * 判断该包名的应用是否安装
     *
     * @param packageName 应用包名
     * @return 是否安装
     */
    public static boolean checkApplication(String packageName) {
        if (packageName == null || "".equals(packageName)) {
            return false;
        }
        try {
            Utils.getApp().getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}