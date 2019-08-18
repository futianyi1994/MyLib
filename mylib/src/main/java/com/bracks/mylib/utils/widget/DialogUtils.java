package com.bracks.mylib.utils.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import androidx.annotation.ColorInt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bracks.mylib.R;


/**
 * good programmer.
 *
 * @date : 2018/6/25  15:50
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 * <p>Example:
 * <pre><code>
 * DialogUtils.createLoadingDialog(getActivity(), "玩命加载中。。。",false).show();
 * </code></pre>
 */
public class DialogUtils {

    private static Dialog loadingDialog;

    public interface AfterShowListener {
        /**
         * 处理一些在需要获取焦点前、显示popwind之后的操作：如隐藏导航栏需要在显示之前失去焦点显示之后重新获取焦点注意需要通过BarUtils.hideNavBar(dialog.getWindow().getDecorView());
         */
        void onAfterShow();
    }

    /**
     * 得到简单自定义的progressDialog（图片加文字）
     *
     * @param context
     * @param msg
     * @return
     */
    public static Dialog createLoadingDialog(Context context, String msg, boolean isCancelable) {
        return createLoadingDialog(context, msg, 10, 1, 0xFFFFFFFF, 0xFFFFFFFF, isCancelable);
    }

    /**
     * 得到简单自定义的progressDialog（图片加文字）
     *
     * @param context
     * @param msg
     * @param bgRadius
     * @param strokeWidth
     * @param strokeColor
     * @param bgColor
     * @param isCancelable
     * @return
     */
    public static Dialog createLoadingDialog(Context context,
                                             String msg,
                                             float bgRadius,
                                             int strokeWidth,
                                             @ColorInt int strokeColor,
                                             @ColorInt int bgColor,
                                             boolean isCancelable) {
        LayoutInflater inflater = LayoutInflater.from(context);
        //得到加载view
        View v = inflater.inflate(R.layout.custom_dialog_loading, null);
        //加载布局
        LinearLayout layout = v.findViewById(R.id.llRoot);
        //提示文字
        TextView tipTextView = v.findViewById(R.id.tipTextView);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(bgRadius);
        drawable.setStroke(strokeWidth, strokeColor);
        drawable.setColor(bgColor);
        layout.setBackground(drawable);

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
        if (loadingDialog == null) {
            loadingDialog = new Dialog(context, R.style.loading_dialog);
        }
        // 不可以用“返回键”取消
        loadingDialog.setCancelable(isCancelable);
        // 设置布局
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        //防止内存泄漏
        loadingDialog.setOnDismissListener(dialog -> destroy());
        return loadingDialog;
    }

    /**
     * 处理一些在需要获取焦点前、显示popwind之后的操作：如隐藏导航栏需要在显示之前失去焦点显示之后重新获取焦点注意需要通过BarUtils.hideNavBar(dialog.getWindow().getDecorView());
     *
     * @param listener
     */
    public static void afterShow(AfterShowListener listener) {
        if (loadingDialog == null) {
            throw new NullPointerException("请先创建Dialog");
        }
        if (listener != null) {
            //Dialog 在初始化时会生成新的 Window，先禁止 Dialog Window 获取焦点，
            //等Dialog显示后对DialogWindow的DecorView设置setSystemUiVisibility，接着再获取焦点。这样表面上看起来就没有退出沉浸模式。
            loadingDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            loadingDialog.show();
            listener.onAfterShow();
            //Clear the not focusable flag from the window
            loadingDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        }
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

    public static void destroy() {
        if (loadingDialog != null) {
            if (loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
            loadingDialog = null;
        }
    }

}
