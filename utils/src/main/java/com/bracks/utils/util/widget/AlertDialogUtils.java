package com.bracks.utils.util.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;

import androidx.appcompat.app.AlertDialog;

import com.bracks.utils.R;


/**
 * good programmer.
 *
 * @date : 2016-12-29 上午 10:19
 * @author: futia
 * @email : futianyi1994@126.com
 * @description : 对话框辅助类
 */
public class AlertDialogUtils {

    /***
     * 获取一个dialog
     * @param context
     * @return
     */
    public static AlertDialog.Builder getDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        return builder;
    }

    /***
     * 获取一个耗时等待对话框
     * @param context
     * @param message
     * @return
     */
    public static ProgressDialog getWaitDialog(Context context, String message) {
        ProgressDialog waitDialog = new ProgressDialog(context);
        if (!TextUtils.isEmpty(message)) {
            waitDialog.setMessage(message);
        }
        return waitDialog;
    }

    /**
     * 进度条加载框
     *
     * @param context
     * @param message
     * @return
     */
    public static ProgressDialog getHorizontalWaitDialog(Context context, String message) {
        ProgressDialog waitDialog = new ProgressDialog(context);
        if (!TextUtils.isEmpty(message)) {
            waitDialog.setMessage(message);
        }
        waitDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //进度最大值
        waitDialog.setMax(ProgressHandler.MAX_PROGRESS);
        return waitDialog;
    }

    /**
     * 显示进度条加载框的方法
     *
     * @param waitDialog
     */
    public static void showHorizontalDialog(ProgressDialog waitDialog) {
        //显示
        waitDialog.show();
        //必须设置到show之后
        waitDialog.setProgress(0);
        //线程
        ProgressHandler handler = new ProgressHandler(waitDialog);
        handler.sendEmptyMessage(ProgressHandler.PRO);
    }

    /***
     * 获取一个信息对话框，注意需要自己手动调用show方法显示
     * @param context
     * @param message
     * @param onClickListener
     * @return
     */
    public static AlertDialog.Builder getMessageDialog(Context context, String message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.common_confirm, onClickListener);
        return builder;
    }

    public static AlertDialog.Builder getMessageDialog(Context context, String message) {
        return getMessageDialog(context, message, null);
    }

    public static AlertDialog.Builder getConfirmDialog(Context context, String message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setMessage(Html.fromHtml(message));
        builder.setPositiveButton(R.string.common_confirm, onClickListener);
        builder.setNegativeButton(R.string.common_cancel, null);
        return builder;
    }

    public static AlertDialog.Builder getConfirmDialog(Context context, String message, DialogInterface.OnClickListener onOkClickListener, DialogInterface.OnClickListener onCancleClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.common_confirm, onOkClickListener);
        builder.setNegativeButton(R.string.common_cancel, onCancleClickListener);
        return builder;
    }

    public static AlertDialog.Builder getSelectDialog(Context context, String title, String[] arrays, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setItems(arrays, onClickListener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setPositiveButton(R.string.common_cancel, null);
        return builder;
    }

    public static AlertDialog.Builder getSelectDialog(Context context, String[] arrays, DialogInterface.OnClickListener onClickListener) {
        return getSelectDialog(context, "", arrays, onClickListener);
    }

    public static AlertDialog.Builder getSingleChoiceDialog(Context context, String title, String[] arrays, int selectIndex, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setSingleChoiceItems(arrays, selectIndex, onClickListener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setNegativeButton(R.string.common_cancel, null);
        return builder;
    }

    public static AlertDialog.Builder getSingleChoiceDialog(Context context, String[] arrays, int selectIndex, DialogInterface.OnClickListener onClickListener) {
        return getSingleChoiceDialog(context, "", arrays, selectIndex, onClickListener);
    }

    public static class ProgressHandler extends Handler {
        public static final int MAX_PROGRESS = 100;
        public static final int PRO = 10;
        private int progress = 10;
        private final ProgressDialog waitDialog;

        public ProgressHandler(ProgressDialog waitDialog) {
            super(Looper.getMainLooper());
            this.waitDialog = waitDialog;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PRO:
                    if (progress >= MAX_PROGRESS) {
                        //重新设置
                        progress = 0;
                        waitDialog.dismiss();//销毁对话框
                    } else {
                        progress++;
                        waitDialog.incrementProgressBy(1);
                        //延迟发送消息
                        this.sendEmptyMessageDelayed(PRO, 100);
                    }
                    break;
                default:
                    break;
            }
        }
    }

}