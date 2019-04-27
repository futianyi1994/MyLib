package com.bracks.futia.mylib.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bracks.futia.mylib.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * good programmer.
 *
 * @data : 2017-11-29 下午 03:59
 * @author: futia
 * @email : futianyi1994@126.com
 * @description : AlertDialog对话框辅助类
 * <p>Example:
 * <pre><code>
 * new CustomAlertDialog
 *                 .Builder(this)
 *                 .creatDefaultDialog()
 *                 .setAfterShowListener(dialog -> BarUtils.hideNavBar(dialog.getWindow().getDecorView()))
 *                 .build();
 * </code></pre>
 */
public class CustomAlertDialog extends AlertDialog {
    /**
     * 没有任何布局
     */
    public static final int EMPTY_VIEW = -1;
    /**
     * 默认的提示信息布局（只有提示信息文字和图标）
     */
    public static final int DEFAULT_PROMPT1 = 0;
    /**
     * 默认的提示信息布局（有标题，信息，确定和取消按钮）
     */
    public static final int DEFAULT_PROMPT2 = 1;
    /**
     * 创建默认的Dialog
     */
    public static final int DEFAULT_DIALOG = 2;
    /**
     * 自定义布局的Dialopg
     */
    public static final int CUSTOM_DIALOG1 = 3;
    public static final int CUSTOM_DIALOG2 = 4;

    @IntDef({EMPTY_VIEW, DEFAULT_PROMPT1, DEFAULT_PROMPT2, DEFAULT_DIALOG, CUSTOM_DIALOG1, CUSTOM_DIALOG2})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ViewStyle {
        int style() default DEFAULT_PROMPT1;

    }

    private Context context;
    private String title;
    private String message;
    private String positiveButtonText;
    private String negativeButtonText;
    private String neutralButtonText;
    private View contentView;
    private View view;
    private @ViewStyle
    int viewStyle = EMPTY_VIEW;
    private int bgResId;
    private int tipResId;
    private int height;
    private int width;
    private long delayMillis;
    private AfterShowListener afterShowListener = null;
    private PostDelayListener postDelayListener = null;
    private OnClickListener positiveButtonClickListener = null;
    private OnClickListener negativeButtonClickListener = null;
    private OnClickListener neutralButtonClickListener = null;
    private View.OnClickListener positiveClickListener = null;
    private View.OnClickListener negativeClickListener = null;


    public interface PostDelayListener {
        /**
         * 设置延迟消失
         */
        void delay();
    }

    public interface AfterShowListener {
        /**
         * 处理一些在需要获取焦点前、显示popwind之后的操作：如隐藏导航栏需要在显示之前失去焦点显示之后重新获取焦点注意需要通过BarUtils.hideNavBar(dialog.getWindow().getDecorView());
         */
        void onAfterShow(CustomAlertDialog dialog);
    }

    private CustomAlertDialog(@NonNull Context context) {
        this(context, 0, null);
    }

    private CustomAlertDialog(@NonNull Context context, Builder builder) {
        this(context, 0, builder);
    }

    private CustomAlertDialog(@NonNull Context context, int themeResId, Builder builder) {
        super(context, themeResId);
        if (builder != null) {
            init(builder);
        }
    }

    private void init(Builder builder) {
        this.context = builder.context;
        this.title = builder.title;
        this.message = builder.message;
        this.positiveButtonText = builder.positiveButtonText;
        this.negativeButtonText = builder.negativeButtonText;
        this.neutralButtonText = builder.neutralButtonText;
        this.contentView = builder.contentView;
        this.view = builder.view;
        this.viewStyle = builder.viewStyle;
        this.bgResId = builder.bgResId;
        this.tipResId = builder.tipResId;
        this.width = builder.width;
        this.height = builder.height;
        this.delayMillis = builder.delayMillis;
        this.afterShowListener = builder.afterShowListener;
        this.postDelayListener = builder.postDelayListener;
        this.positiveButtonClickListener = builder.positiveButtonClickListener;
        this.negativeButtonClickListener = builder.negativeButtonClickListener;
        this.neutralButtonClickListener = builder.neutralButtonClickListener;
        this.positiveClickListener = builder.positiveClickListener;
        this.negativeClickListener = builder.negativeClickListener;

        //设置显示Dialog
        switch (this.viewStyle) {
            case EMPTY_VIEW:
                setDefaultDialogView(DEFAULT_PROMPT1);
                break;
            case DEFAULT_PROMPT1:
                setDefaultDialogView(DEFAULT_PROMPT1);
                break;
            case DEFAULT_PROMPT2:
                setDefaultDialogView(DEFAULT_PROMPT2);
                break;
            case DEFAULT_DIALOG:
                setDefaultDialogView(DEFAULT_DIALOG);
                break;
            case CUSTOM_DIALOG1:
                //setContentView要在设置宽高之前否则宽高设置无效
                setCustomView(this.contentView);
                break;
            case CUSTOM_DIALOG2:
                setMView(this.view);
                break;
            default:
                break;
        }

        setDialogWindow();
        if (this.delayMillis != 0) {
            postDelay(this.delayMillis, this.postDelayListener);
        }
    }

    /**
     * 设置DialogWindow的参数
     */
    private void setDialogWindow() {
        if (this.bgResId != 0) {
            setBackgroundDrawableResource(this.bgResId);
        }
        if (this.width != 0) {
            setWidth(this.width);
        }
        if (this.height != 0) {
            setHeight(this.height);
        }
    }

    /**
     * 设置默认的Dialog的布局样式
     *
     * @param viewStyle
     */
    private void setDefaultDialogView(@ViewStyle int viewStyle) {
        View view;
        TextView tvTitle;
        TextView tvMessage;
        ImageView image;
        switch (viewStyle) {
            case DEFAULT_PROMPT1:
                view = View.inflate(this.context, R.layout.custom_dialog_default_prompt, null);
                tvMessage = view.findViewById(R.id.tv_message);
                image = view.findViewById(R.id.iv_image);
                tvMessage.setText(this.message);
                if (this.tipResId != 0) {
                    image.setBackgroundResource(this.tipResId);
                }
                setCustomView(view);
                break;
            case DEFAULT_PROMPT2:
                view = View.inflate(this.context, R.layout.common_dialog_layout, null);
                tvTitle = view.findViewById(R.id.title);
                tvMessage = view.findViewById(R.id.message);
                Button positiveButton = view.findViewById(R.id.positiveButton);
                Button negativeButton = view.findViewById(R.id.negativeButton);
                tvTitle.setText(this.title);
                tvMessage.setText(this.message);
                if (TextUtils.isEmpty(this.message)) {
                    tvMessage.setVisibility(View.GONE);
                } else if (TextUtils.isEmpty(this.title)) {
                    tvTitle.setVisibility(View.GONE);
                }
                positiveButton.setText(this.positiveButtonText);
                negativeButton.setText(this.negativeButtonText);
                positiveButton.setOnClickListener(this.positiveClickListener);
                negativeButton.setOnClickListener(this.negativeClickListener);
                setCustomView(view);
                break;
            case DEFAULT_DIALOG:
                creatDefaultDialog();
                break;
            case CUSTOM_DIALOG1:
            case CUSTOM_DIALOG2:
            case EMPTY_VIEW:
            default:
                break;
        }
    }

    /**
     * 创建默认的Dialog
     */
    private void creatDefaultDialog() {
        //设置对话框icon
        setIcon(R.drawable.common_empty_failed);
        //设置对话框标题
        setTitle(this.title);
        //设置文字显示内容
        setMessage(this.message);
        //分别设置三个button
        setButton(DialogInterface.BUTTON_POSITIVE, this.positiveButtonText, this.positiveButtonClickListener);
        setButton(DialogInterface.BUTTON_NEGATIVE, this.negativeButtonText, this.negativeButtonClickListener);
        setButton(DialogInterface.BUTTON_NEUTRAL, this.neutralButtonText, this.neutralButtonClickListener);
        //Dialog 在初始化时会生成新的 Window，先禁止 Dialog Window 获取焦点，
        //等Dialog显示后对DialogWindow的DecorView设置setSystemUiVisibility，接着再获取焦点。这样表面上看起来就没有退出沉浸模式。
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        //显示对话框
        show();
        if (this.afterShowListener != null) {
            this.afterShowListener.onAfterShow(this);
        }
        //Clear the not focusable flag from the window
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    /**
     * 设置自定义AlertDialog布局
     *
     * @param view
     * @return
     */
    private void setCustomView(View view) {
        //Dialog 在初始化时会生成新的 Window，先禁止 Dialog Window 获取焦点，
        //等Dialog显示后对DialogWindow的DecorView设置setSystemUiVisibility，接着再获取焦点。这样表面上看起来就没有退出沉浸模式。
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        show();
        if (this.afterShowListener != null) {
            this.afterShowListener.onAfterShow(this);
        }
        //Clear the not focusable flag from the window
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        //添加布局
        setContentView(view);
    }

    /**
     * 设置自定义AlertDialog布局
     *
     * @param view
     */
    private void setMView(View view) {
        //添加布局
        setView(view);
        //Dialog 在初始化时会生成新的 Window，先禁止 Dialog Window 获取焦点，
        //等Dialog显示后对DialogWindow的DecorView设置setSystemUiVisibility，接着再获取焦点。这样表面上看起来就没有退出沉浸模式。
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        show();
        if (this.afterShowListener != null) {
            this.afterShowListener.onAfterShow(this);
        }
        //Clear the not focusable flag from the window
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    /**
     * 设置AlertDialog背景
     *
     * @param resId
     * @return
     */
    private void setBackgroundDrawableResource(@DrawableRes int resId) {
        Window window = getWindow();
        window.setBackgroundDrawableResource(resId);
    }

    /**
     * 设置AlertDialog高度
     *
     * @param height
     * @return
     */
    private void setHeight(int height) {
        //获取对话框当前的参数值
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.height = height;
        //设置生效
        window.setAttributes(params);
    }

    /**
     * 设置AlertDialog高度
     *
     * @param width
     * @return
     */
    private void setWidth(int width) {
        //获取对话框当前的参数值
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = width;
        //设置生效
        window.setAttributes(params);
    }

    /**
     * 设置延迟消失
     *
     * @param delayMillis
     * @return
     */
    private void postDelay(long delayMillis, final PostDelayListener postDelayListener) {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            dismiss();
            if (postDelayListener != null) {
                postDelayListener.delay();
            }
        }, delayMillis);
    }

    /**
     * 重写dismiss方法
     */
    @Override
    public void dismiss() {
        super.dismiss();
        if (isShowing()) {
            dismiss();
        }
    }


    public static class Builder {
        private Context context;
        private @StyleRes
        int themeResId;
        private String title = "title";
        private String message = "message";
        private String positiveButtonText = "ok";
        private String negativeButtonText = "cancel";
        private String neutralButtonText = "custom";
        private View contentView = null;
        private View view = null;
        private @ViewStyle
        int viewStyle = EMPTY_VIEW;
        private int bgResId = 0;
        private int tipResId = 0;
        private int height = 0;
        private int width = 0;
        private long delayMillis = 0;
        private AfterShowListener afterShowListener = null;
        private PostDelayListener postDelayListener = null;
        private OnClickListener positiveButtonClickListener = null;
        private OnClickListener negativeButtonClickListener = null;
        private OnClickListener neutralButtonClickListener = null;
        private View.OnClickListener positiveClickListener = null;
        private View.OnClickListener negativeClickListener = null;

        public Builder(Context context) {
            this(context, 0);
        }

        public Builder(Context context, int themeResId) {
            this.context = context;
            this.themeResId = themeResId;
        }

        /**
         * 设置AlertDialog的Icon
         *
         * @param iconId
         * @return
         */
        public Builder setIcon(int iconId) {
            return this;
        }

        /**
         * 设置AlertDialog的title
         *
         * @param message
         * @return
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * 设置AlertDialog的message
         *
         * @param message
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * 设置AlertDialog的title
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * 设置AlertDialog的title
         *
         * @param title
         * @return
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * 设置自定义布局
         *
         * @param v
         * @return
         */
        public Builder setContentView(View v) {
            viewStyle = CUSTOM_DIALOG1;
            this.contentView = v;
            return this;
        }

        /**
         * 设置自定义布局
         *
         * @param v
         * @return
         */
        public Builder setView(View v) {
            viewStyle = CUSTOM_DIALOG2;
            this.view = v;
            return this;
        }

        /**
         * 设置默认的提示框（只有一条提示信息和提示图片）
         *
         * @return
         */
        public Builder setDefaultPromptView1() {
            return setDefaultPromptView1(null, 0);
        }

        /**
         * 设置默认Dialog
         *
         * @param tipMessage
         * @param tipResId
         * @return
         */
        public Builder setDefaultPromptView1(String tipMessage, @DrawableRes int tipResId) {
            viewStyle = DEFAULT_PROMPT1;
            this.message = tipMessage;
            this.tipResId = tipResId;
            return this;
        }

        /**
         * 设置默认Dialog
         *
         * @return
         */
        public Builder setDefaultPromptView2() {
            viewStyle = DEFAULT_PROMPT2;
            return this;
        }

        /**
         * 设置默认Dialog
         *
         * @param title
         * @param message
         * @param negativeButtonText
         * @param positiveButtonText
         * @return
         */
        public Builder setDefaultPromptView2(String title, String message, String negativeButtonText, String positiveButtonText) {
            viewStyle = DEFAULT_PROMPT2;
            this.title = title;
            this.message = message;
            this.negativeButtonText = negativeButtonText;
            this.positiveButtonText = positiveButtonText;
            return this;
        }

        /**
         * 设置默认Dialog
         *
         * @return
         */
        public Builder creatDefaultDialog() {
            viewStyle = DEFAULT_DIALOG;
            return this;
        }

        /**
         * 设置AlertDialog背景
         *
         * @param bgResId
         * @return
         */
        public Builder setBackgroundDrawableResource(@DrawableRes int bgResId) {
            this.bgResId = bgResId;
            return this;
        }

        /**
         * 设置AlertDialog高度
         *
         * @param height
         * @return
         */
        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        /**
         * 设置AlertDialog宽度
         *
         * @param width
         * @return
         */
        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        /**
         * 设置AfterShowListener 处理一些在需要获取焦点前、显示popwind之后的操作：如隐藏导航栏需要在显示之前失去焦点显示之后重新获取焦点注意需要通过BarUtils.hideNavBar(dialog.getWindow().getDecorView());
         *
         * @param afterShowListener
         * @return
         */
        public Builder setAfterShowListener(AfterShowListener afterShowListener) {
            this.afterShowListener = afterShowListener;
            return this;
        }

        /**
         * 设置延迟消失
         *
         * @param delayMillis
         * @return
         */
        public Builder setDelay(long delayMillis) {
            this.setDelay(delayMillis, null);
            return this;
        }

        /**
         * 设置延迟消失
         *
         * @param delayMillis
         * @param postDelayListener
         * @return
         */
        public Builder setDelay(long delayMillis, PostDelayListener postDelayListener) {
            this.delayMillis = delayMillis;
            this.postDelayListener = postDelayListener;
            return this;
        }

        /**
         * 设置AlertDialog的监听事件
         *
         * @param positiveButtonText
         * @param listener
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = (String) context.getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = (String) context.getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        /**
         * 自定义布局确定取消监听事件
         *
         * @param positiveButtonText
         * @param listener
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText, View.OnClickListener listener) {
            this.positiveButtonText = (String) context.getText(positiveButtonText);
            this.positiveClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, View.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText, View.OnClickListener listener) {
            this.negativeButtonText = (String) context.getText(negativeButtonText);
            this.negativeClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, View.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeClickListener = listener;
            return this;
        }

        public CustomAlertDialog build() {
            return new CustomAlertDialog(context, themeResId, this);
        }
    }
}
