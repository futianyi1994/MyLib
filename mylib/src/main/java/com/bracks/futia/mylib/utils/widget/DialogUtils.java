package com.bracks.futia.mylib.utils.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bracks.futia.mylib.R;


/**
 * good programmer.
 *
 * @date : 2018/6/25  15:50
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :  使用方法： DialogUtils.createLoadingDialog(getActivity(), "玩命加载中。。。",false).show();
 */
public class DialogUtils {
    /**
     * 得到简单自定义的progressDialog（图片加文字）
     *
     * @param context
     * @param msg
     * @return
     */
    public static Dialog createLoadingDialog(Context context, String msg, boolean isCancelable) {
        LayoutInflater inflater = LayoutInflater.from(context);
        //得到加载view
        View v = inflater.inflate(R.layout.custom_dialog_loading, null);
        //加载布局
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);
        //提示文字
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);
        //xml中的ImageView
        //ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        //加载动画
        //Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.loading_animation);
        //使用ImageView显示动画
        //spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        //设置加载信息
        tipTextView.setText(msg);
        /**
         * 创建自定义样式dialog
         * <style name="loading_dialog" parent="android:style/Theme.Dialog">
         * <item name="android:windowFrame">@null</item>
         * <item name="android:windowNoTitle">true</item>
         * <item name="android:windowBackground">@android:color/transparent</item>
         * <item name="android:windowIsFloating">true</item>
         * <item name="android:windowContentOverlay">@null</item>
         * </style>
         */
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);
        // 不可以用“返回键”取消
        loadingDialog.setCancelable(isCancelable);
        // 设置布局
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return loadingDialog;
    }

    public static void dismissDialog(Dialog dialog) {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
