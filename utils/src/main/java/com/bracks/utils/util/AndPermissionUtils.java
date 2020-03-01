package com.bracks.utils.util;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.bracks.utils.R;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.RequestExecutor;

import java.util.List;

/**
 * good programmer.
 *
 * @data : 2018\5\25 20:17
 * @author: futia
 * @email : futianyi1994@126.com
 * @description : https://github.com/yanzhenjie/AndPermission
 */
public class AndPermissionUtils {
    private Context context;
    private Activity activity;

    /**
     * 获取权限,权限被用户总是拒绝需要去设置中设置
     *
     * @param context
     * @param permissions
     */
    public void getPermission(final Context context, final String[] permissions, final PermissionRequest callBack) {
        getPermission(context, permissions, true, callBack);
    }

    /**
     * 获取权限
     *
     * @param context
     * @param permissions
     * @param hasGoSetting 是否需要去设置中设置
     * @param callBack
     */
    public void getPermission(final Context context, final String[] permissions, final boolean hasGoSetting, final PermissionRequest callBack) {
        this.context = context;
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
        AndPermission
                .with(context)
                .runtime()
                .permission(permissions)
                .rationale((context1, data, executor) -> {
                    callBack.rationale(context1, data, executor);
                    // 自定义对话框询问用户是否继续授权。。
                    new AlertDialog
                            .Builder(context1)
                            .setTitle(context1.getString(R.string.permission_tip))
                            .setMessage(context1.getString(R.string.permission_need) +
                                    context1.getString(R.string.permission_some) +
                                    context1.getString(R.string.permission_come_useapp))
                            .setPositiveButton(
                                    R.string.permission_allow
                                    , (dialog, which) -> {
                                        dialog.cancel();
                                        // 如果用户继续：
                                        executor.execute();
                                    }
                            )
                            .setNegativeButton(
                                    R.string.permission_refuse
                                    , (dialog, which) -> {
                                        dialog.cancel();
                                        // 如果用户中断：
                                        executor.cancel();
                                    }
                            )
                            .show();
                })
                .onGranted(callBack::onGranted)
                .onDenied(data -> {
                    callBack.onDenied(data);
                    // 这些权限被用户总是拒绝。
                    if (AndPermission.hasAlwaysDeniedPermission(context, data) && hasGoSetting) {
                        //获取权限的名字
                        String permissionText = TextUtils.join("，", Permission.transformText(activity, permissions));
                        // 自定义对话框在用户选择不再询问后继续询问用户是否继续授权。。
                        new AlertDialog
                                .Builder(context)
                                .setTitle(context.getString(R.string.permission_tip))
                                .setMessage(
                                        context.getString(R.string.permission_need) +
                                                permissionText +
                                                context.getString(R.string.permission_message))
                                .setPositiveButton(
                                        R.string.permission_setting
                                        , (dialog, which) -> {
                                            //这里使用一个Dialog展示没有这些权限应用程序无法继续运行，去设置中授权。
                                            AndPermission.with(context)
                                                    .runtime()
                                                    .setting()
                                                    .onComeback(() -> {
                                                        //用户从设置回来了
                                                        callBack.onSetting(data);
                                                    })
                                                    .start();
                                            dialog.cancel();
                                        }
                                )
                                .setNegativeButton(
                                        R.string.permission_cancel
                                        , (dialog, which) -> dialog.cancel()
                                )
                                .show();
                    }
                })
                .start();
    }

    /**
     * 回调接口
     */
    public interface PermissionRequest {
        /**
         * Set request rationale.
         */
        void rationale(Context context, List<String> permissons, final RequestExecutor executor);

        /**
         * Action to be taken when all permissions are granted.
         */
        void onGranted(List<String> granted);

        /**
         * Action to be taken when all permissions are denied.
         */
        void onDenied(List<String> denied);

        /**
         * go setting
         */
        void onSetting(List<String> permissons);
    }
}
