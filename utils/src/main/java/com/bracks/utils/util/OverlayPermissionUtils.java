package com.bracks.utils.util;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ScreenUtils;

/**
 * good programmer.
 *
 * @date : 2019-03-20 上午 11:42
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class OverlayPermissionUtils {

    private static final String TAG = "OverlayPermissionUtils";
    /**
     * 悬浮窗默认宽度
     */
    private static final int WIDTH = 200;
    /**
     * 悬浮窗默认高度
     */
    private static final int HEIGHT = 150;

    /**
     * 状态栏高度
     */
    private static int statusbarHeight = BarUtils.getStatusBarHeight();

    public static final int REQUEST_CODE = 20000;

    private static Dialog dialog;

    private static WindowManager.LayoutParams params;
    private static WindowManager windowManager;

    private static volatile OverlayPermissionUtils singleton = null;

    /**
     * 单利构造器私有化
     */
    private OverlayPermissionUtils() {

    }

    /**
     * 对外唯一实例的接口
     */
    public static OverlayPermissionUtils getInstance() {
        if (singleton == null) {
            synchronized (OverlayPermissionUtils.class) {
                if (singleton == null) {
                    singleton = new OverlayPermissionUtils();
                }
            }
        }
        return singleton;
    }

    public static WindowManager.LayoutParams getParams() {
        return params;
    }

    /**
     * 初始化传入参数
     *
     * @param windowManager
     * @param params
     * @return
     */
    public WindowManager.LayoutParams init(WindowManager windowManager, WindowManager.LayoutParams params, boolean withParam) {
        OverlayPermissionUtils.windowManager = windowManager;
        params.width = params.width <= 0 ? WIDTH : params.width;
        params.height = params.height <= 0 ? HEIGHT : params.height;
        params.y = params.y == 0 ? statusbarHeight : params.y;
        OverlayPermissionUtils.params = params;
        return withParam ? withWMParams(params) : params;
    }

    public WindowManager.LayoutParams init(WindowManager windowManager, WindowManager.LayoutParams params) {
        return init(windowManager, params, true);
    }

    /**
     * 包装参数
     *
     * @param params
     * @return
     */
    private WindowManager.LayoutParams withWMParams(WindowManager.LayoutParams params) {
        params.gravity = Gravity.TOP | Gravity.START;
        params.format = PixelFormat.OPAQUE;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        //params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            params.type = WindowManager.LayoutParams.TYPE_PHONE;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            params.type = WindowManager.LayoutParams.TYPE_TOAST;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
       /* params.type = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                ?
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                :
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;*/
        return params;
    }

    /**
     * 更新视图宽高
     *
     * @param v
     */
    public void updateViewLayout(View v, int width, int height) {
        params.width = width;
        params.height = height;
        windowManager.updateViewLayout(v, params);
    }

    /**
     * 更新视图
     * 需要外部先设置params
     *
     * @param v
     */
    public void updateViewLayout(View v) {
        windowManager.updateViewLayout(v, params);
    }

    /**
     * 触摸监听器
     */
    public static class FloatingOnTouchListener implements View.OnTouchListener {
        /**
         * y轴偏移量默认为状态栏高度
         */
        private int topOffset;
        /**
         * 触摸点相对于view的坐标
         */
        private int touchX, touchY;
        /**
         * 屏幕的宽高
         */
        private int screenWidth = ScreenUtils.getScreenWidth();
        private int screenHeight = ScreenUtils.getScreenHeight();
        /**
         * 是否在移动
         */
        private boolean isMove;
        /**
         * 开始移动的时间
         */
        private long startTime;
        /**
         * 最后通过动画将mView的X轴坐标移动到finalMoveX
         */
        private int finalMoveX;

        public FloatingOnTouchListener() {
            topOffset = statusbarHeight;
        }

        public FloatingOnTouchListener(int topOffset) {
            this.topOffset = topOffset;
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.d(TAG, "width: " + params.width + ",height: " + params.height);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchX = (int) event.getX();
                    touchY = (int) event.getY();
                    startTime = System.currentTimeMillis();
                    isMove = false;
                    return false;
                case MotionEvent.ACTION_MOVE:
                    params.x = (int) (event.getRawX() - touchX);
                    params.y = (int) (event.getRawY() - touchY);
                    params.y = params.y <= topOffset ? topOffset : params.y;
                    windowManager.updateViewLayout(v, params);
                    return true;
                case MotionEvent.ACTION_UP:
                    long curTime = System.currentTimeMillis();
                    //判断mView是在Window中的位置，以中间为界
                    isMove = curTime - startTime > 100;
                    if (params.x + v.getMeasuredWidth() / 2 >= screenWidth / 2) {
                        finalMoveX = screenWidth - v.getMeasuredWidth();
                    } else {
                        finalMoveX = 0;
                    }
                    //使用动画移动mView             
                    ValueAnimator animator = ValueAnimator.ofInt(params.x, finalMoveX).setDuration(Math.abs(params.x - finalMoveX));
                    animator.addUpdateListener((ValueAnimator animation) -> {
                        params.x = (int) animation.getAnimatedValue();
                        windowManager.updateViewLayout(v, params);
                    });
                    animator.start();
                    return isMove;
                default:
                    return false;
            }
        }
    }

    /**
     * 权限申请
     *
     * @param context
     * @param result
     */
    public boolean applay(Context context, OnConfirmResult result) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(context)) {
                showConfirmDialog(context, new OnConfirmResult() {
                    @Override
                    public void confirmResult(boolean confirm) {
                        if (confirm) {
                            try {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setData(Uri.parse("package:" + context.getPackageName()));
                                context.startActivity(intent);
                            } catch (Exception e) {
                                Log.e(TAG, Log.getStackTraceString(e));
                            }
                        } else {
                            Log.d(TAG, "user manually refuse OVERLAY_PERMISSION");
                        }
                        result.confirmResult(confirm);
                    }
                });
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    /**
     * 权限申请
     *
     * @param activity
     * @param result
     */
    public boolean applayForResult(Activity activity, OnConfirmResult result) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(activity)) {
                showConfirmDialog(activity, new OnConfirmResult() {
                    @Override
                    public void confirmResult(boolean confirm) {
                        if (confirm) {
                            try {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setData(Uri.parse("package:" + activity.getPackageName()));
                                activity.startActivityForResult(intent, REQUEST_CODE);
                            } catch (Exception e) {
                                Log.e(TAG, Log.getStackTraceString(e));
                            }
                        } else {
                            Log.d(TAG, "user manually refuse OVERLAY_PERMISSION");
                        }
                        result.confirmResult(confirm);
                    }
                });
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    /**
     * 判断是否有悬浮窗权限
     *
     * @param context
     * @return
     */
    public static boolean canDrawOverlays(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        } else {
            return true;
        }
    }

    private void showConfirmDialog(Context context, OnConfirmResult result) {
        showConfirmDialog(context, "您的手机没有授予悬浮窗权限，请开启后再试", result);
    }

    private void showConfirmDialog(Context context, String message, final OnConfirmResult result) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog = new AlertDialog
                .Builder(context)
                .setCancelable(true)
                .setTitle("")
                .setMessage(message)
                .setPositiveButton("现在去开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirmResult(true);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("暂不开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirmResult(false);
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }

    public interface OnConfirmResult {
        void confirmResult(boolean confirm);
    }
}
