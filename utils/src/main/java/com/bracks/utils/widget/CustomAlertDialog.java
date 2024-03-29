package com.bracks.utils.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AlertDialog;

import com.bracks.utils.R;

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
     * 创建默认的Dialog
     */
    public static final int DEFAULT_DIALOG = 0;
    /**
     * 默认的提示信息布局（只有提示信息文字和图标）
     */
    public static final int DEFAULT_PROMPT1 = 1;
    /**
     * 默认的提示信息布局（有标题，信息，确定和取消按钮）
     */
    public static final int DEFAULT_PROMPT2 = 2;
    /**
     * 自定义布局的Dialopg
     */
    public static final int CUSTOM_DIALOG1 = 3;
    public static final int CUSTOM_DIALOG2 = 4;
    public static final int CUSTOM_DIALOG3 = 5;
    private Context context;
    private int iconId;
    private String title;
    private String message;
    private String positiveButtonText;
    private String negativeButtonText;
    private String neutralButtonText;
    private int titleColor;
    private int messageColor;
    private int positiveButtonTextColor;
    private int negativeButtonTextColor;
    private int neutralButtonTextColor;
    private int titleUnit;
    private int messageUnit;
    private int positiveButtonTextUnit;
    private int negativeButtonTextUnit;
    private int neutralButtonTextUnit;
    private float titleSize;
    private float messageSize;
    private float positiveButtonTextSize;
    private float negativeButtonTextSize;
    private float neutralButtonTextSize;
    private View contentView;
    private View view;
    private int layoutResId;
    private @ViewStyle
    int viewStyle = EMPTY_VIEW;
    private float bgRadius;
    private int bgStrokeWidth;
    private @ColorInt
    int bgStrokeColor;
    private @ColorInt
    int bgColor;
    private int bgResId;
    private int height;
    private int width;
    private long delayMillis;
    private int tipResId;
    private int tipWidth;
    private int tipHeight;
    private ViewInterface viewCallback;
    private AfterShowListener afterShowListener;
    private PostDelayListener postDelayListener;
    private OnClickListener positiveButtonClickListener;
    private OnClickListener negativeButtonClickListener;
    private OnClickListener neutralButtonClickListener;
    private View.OnClickListener positiveClickListener;
    private View.OnClickListener negativeClickListener;

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
        this.iconId = builder.iconId;
        this.title = builder.title;
        this.message = builder.message;

        this.positiveButtonText = builder.positiveButtonText;
        this.negativeButtonText = builder.negativeButtonText;
        this.neutralButtonText = builder.neutralButtonText;

        this.titleColor = builder.titleColor;
        this.messageColor = builder.messageColor;
        this.positiveButtonTextColor = builder.positiveButtonTextColor;
        this.negativeButtonTextColor = builder.negativeButtonTextColor;
        this.neutralButtonTextColor = builder.neutralButtonTextColor;

        this.titleUnit = builder.titleUnit;
        this.messageUnit = builder.messageUnit;
        this.positiveButtonTextUnit = builder.positiveButtonTextUnit;
        this.negativeButtonTextUnit = builder.negativeButtonTextUnit;
        this.neutralButtonTextUnit = builder.neutralButtonTextUnit;

        this.titleSize = builder.titleSize;
        this.messageSize = builder.messageSize;
        this.positiveButtonTextSize = builder.positiveButtonTextSize;
        this.negativeButtonTextSize = builder.negativeButtonTextSize;
        this.neutralButtonTextSize = builder.neutralButtonTextSize;

        this.contentView = builder.contentView;
        this.view = builder.view;
        this.layoutResId = builder.layoutResId;
        this.viewStyle = builder.viewStyle;

        this.bgRadius = builder.bgRadius;
        this.bgStrokeWidth = builder.bgStrokeWidth;
        this.bgStrokeColor = builder.bgStrokeColor;
        this.bgColor = builder.bgColor;

        this.bgResId = builder.bgResId;
        this.width = builder.width;
        this.height = builder.height;
        this.delayMillis = builder.delayMillis;

        this.tipResId = builder.tipResId;
        this.tipWidth = builder.tipWidth;
        this.tipHeight = builder.tipHeight;

        this.viewCallback = builder.viewCallback;
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
            case DEFAULT_DIALOG:
                setDefaultDialogView(DEFAULT_DIALOG);
                break;
            case DEFAULT_PROMPT1:
                setDefaultDialogView(DEFAULT_PROMPT1);
                break;
            case DEFAULT_PROMPT2:
                setDefaultDialogView(DEFAULT_PROMPT2);
                break;
            case CUSTOM_DIALOG1:
                //setContentView要在设置宽高之前否则宽高设置无效
                setCustomView(this.contentView);
                break;
            case CUSTOM_DIALOG2:
                setMView(this.view);
                break;
            case CUSTOM_DIALOG3:
                setCustomView(this.layoutResId);
                break;
            default:
                break;
        }

        setDialogWindow(this.viewStyle);
        if (this.delayMillis != 0) {
            postDelay(this.delayMillis, this.postDelayListener);
        }
    }

    /**
     * 设置默认的Dialog的布局样式
     *
     * @param viewStyle
     */
    private void setDefaultDialogView(@ViewStyle int viewStyle) {
        LinearLayout llRoot;
        View view;
        TextView tvTitle;
        TextView tvMessage;
        ImageView image;
        GradientDrawable drawable;
        switch (viewStyle) {
            case DEFAULT_DIALOG:
                creatDefaultDialog();
                break;
            case DEFAULT_PROMPT1:
                view = View.inflate(this.context, R.layout.custom_dialog_default_prompt, null);
                llRoot = view.findViewById(R.id.llRoot);
                tvMessage = view.findViewById(R.id.tvMessage);
                image = view.findViewById(R.id.image);

                drawable = new GradientDrawable();
                drawable.setCornerRadius(this.bgRadius);
                drawable.setStroke(this.bgStrokeWidth, this.bgStrokeColor);
                drawable.setColor(this.bgColor);
                llRoot.setBackground(drawable);

                tvMessage.setText(this.message);
                tvMessage.setTextColor(this.messageColor);
                tvMessage.setTextSize(this.messageUnit, this.messageSize);

                if (this.tipResId != 0) {
                    ViewGroup.LayoutParams lp = image.getLayoutParams();
                    lp.width = this.tipWidth;
                    lp.height = this.tipHeight;
                    image.setLayoutParams(lp);
                    image.setBackgroundResource(this.tipResId);
                }

                setCustomView(view);
                break;
            case DEFAULT_PROMPT2:
                view = View.inflate(this.context, R.layout.common_dialog_layout, null);
                llRoot = view.findViewById(R.id.llRoot);
                tvTitle = view.findViewById(R.id.tvTitle);
                tvMessage = view.findViewById(R.id.tvMessage);
                Button positiveButton = view.findViewById(R.id.positiveButton);
                Button negativeButton = view.findViewById(R.id.negativeButton);

                drawable = new GradientDrawable();
                drawable.setCornerRadius(this.bgRadius);
                drawable.setStroke(this.bgStrokeWidth, this.bgStrokeColor);
                drawable.setColor(this.bgColor);
                llRoot.setBackground(drawable);

                tvTitle.setText(this.title);
                tvTitle.setTextColor(this.titleColor);
                tvTitle.setTextSize(this.titleUnit, this.titleSize);
                tvMessage.setText(this.message);
                tvMessage.setTextColor(this.messageColor);
                tvMessage.setTextSize(this.messageUnit, this.messageSize);
                if (TextUtils.isEmpty(this.message)) {
                    tvMessage.setVisibility(View.GONE);
                } else if (TextUtils.isEmpty(this.title)) {
                    tvTitle.setVisibility(View.GONE);
                }

                positiveButton.setText(this.positiveButtonText);
                positiveButton.setTextColor(this.positiveButtonTextColor);
                positiveButton.setTextSize(this.positiveButtonTextUnit, this.positiveButtonTextSize);

                negativeButton.setText(this.negativeButtonText);
                negativeButton.setTextColor(this.negativeButtonTextColor);
                negativeButton.setTextSize(this.negativeButtonTextUnit, this.negativeButtonTextSize);

                positiveButton.setOnClickListener(this.positiveClickListener);
                negativeButton.setOnClickListener(this.negativeClickListener);

                setCustomView(view);
                break;
            case CUSTOM_DIALOG1:
            case CUSTOM_DIALOG2:
            case CUSTOM_DIALOG3:
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
        setIcon(this.iconId);
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

        //在show()执行过之后，才能获取到Button
        getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(this.positiveButtonTextColor);
        getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(this.negativeButtonTextColor);
        getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(this.neutralButtonTextColor);
        getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(this.positiveButtonTextUnit, this.positiveButtonTextSize);
        getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(this.negativeButtonTextUnit, this.negativeButtonTextSize);
        getButton(DialogInterface.BUTTON_NEUTRAL).setTextSize(this.neutralButtonTextUnit, this.neutralButtonTextSize);
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
     * 设置自定义AlertDialog布局
     *
     * @param layoutResId
     */
    private void setCustomView(int layoutResId) {
        //添加布局
        View contentView = View.inflate(this.context, layoutResId, null);
        setCustomView(contentView);
        if (this.viewCallback != null && this.layoutResId != 0) {
            this.viewCallback.getChildView(contentView, this.layoutResId);
        }
    }

    /**
     * 设置DialogWindow的参数
     *
     * @param viewStyle
     */
    private void setDialogWindow(@ViewStyle int viewStyle) {
        if (viewStyle != DEFAULT_DIALOG) {
            //将弹窗的背景设置为透明色，解决自定义布局的背景圆角无效问题。
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        if (this.bgResId != 0) {
            setBackgroundDrawableResource(this.bgResId);
        }
        setWidth(this.width);
        setHeight(this.height);
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
     * 重写dismiss方法
     */
    @Override
    public void dismiss() {
        if (isShowing()) {
            super.dismiss();
        }
    }

    @IntDef({EMPTY_VIEW, DEFAULT_DIALOG, DEFAULT_PROMPT1, DEFAULT_PROMPT2, CUSTOM_DIALOG1, CUSTOM_DIALOG2, CUSTOM_DIALOG3})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ViewStyle {
        int style() default DEFAULT_PROMPT1;

    }

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

    public interface ViewInterface {
        /**
         * popwindow内的子视图
         *
         * @param view
         * @param layoutResId
         */
        void getChildView(View view, int layoutResId);
    }

    public static class Builder {
        private final Context context;
        private @StyleRes
        final
        int themeResId;
        private int iconId = android.R.mipmap.sym_def_app_icon;
        private String title;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private String neutralButtonText;
        private int titleColor = 0x8A000000;
        private int messageColor = 0x8A000000;
        private int positiveButtonTextColor = 0x8A000000;
        private int negativeButtonTextColor = 0x8A000000;
        private int neutralButtonTextColor = 0x8A000000;
        private int titleUnit = TypedValue.COMPLEX_UNIT_SP;
        private int messageUnit = TypedValue.COMPLEX_UNIT_SP;
        private int positiveButtonTextUnit = TypedValue.COMPLEX_UNIT_SP;
        private int negativeButtonTextUnit = TypedValue.COMPLEX_UNIT_SP;
        private int neutralButtonTextUnit = TypedValue.COMPLEX_UNIT_SP;
        private float titleSize = 14f;
        private float messageSize = 14f;
        private float positiveButtonTextSize = 14f;
        private float negativeButtonTextSize = 14f;
        private float neutralButtonTextSize = 14f;
        private View contentView = null;
        private View view = null;
        private int layoutResId = 0;
        private @ViewStyle
        int viewStyle = EMPTY_VIEW;
        private float bgRadius = 10;
        private int bgStrokeWidth = 1;
        private @ColorInt
        int bgStrokeColor = 0xFFFFFFFF;
        private @ColorInt
        int bgColor = 0xFFFFFFFF;
        private int bgResId = 0;
        private int height = -2;
        private int width = -2;
        private long delayMillis = 0;
        private int tipResId = 0;
        private int tipWidth = -2;
        private int tipHeight = -2;
        private ViewInterface viewCallback = null;
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
            this.title = context.getString(android.R.string.dialog_alert_title);
            this.message = context.getString(android.R.string.unknownName);
            this.positiveButtonText = context.getString(android.R.string.ok);
            this.negativeButtonText = context.getString(android.R.string.cancel);
            this.neutralButtonText = context.getString(android.R.string.unknownName);
        }

        /**
         * 设置AlertDialog的Icon
         *
         * @param iconId
         * @return
         */
        public Builder setIcon(int iconId) {
            this.iconId = iconId;
            return this;
        }

        /**
         * 设置AlertDialog的title
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) this.context.getText(title);
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
         * 设置AlertDialog的title
         *
         * @param title
         * @param color
         * @param unit
         * @param size
         * @return
         */
        public Builder setTitle(String title, int color, int unit, float size) {
            this.title = title;
            this.titleColor = color;
            this.titleUnit = unit;
            this.titleSize = size;
            return this;
        }

        /**
         * 设置AlertDialog的message
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
            this.message = (String) this.context.getText(message);
            return this;
        }

        /**
         * 设置AlertDialog的title
         *
         * @param message
         * @param color
         * @param unit
         * @param size
         * @return
         */
        public Builder setMessage(String message, int color, int unit, float size) {
            this.message = message;
            this.messageColor = color;
            this.messageUnit = unit;
            this.messageSize = size;
            return this;
        }

        /**
         * 设置AlertDialog的titleColor
         *
         * @param titleColor
         * @return
         */
        public Builder setTitleColor(int titleColor) {
            this.titleColor = titleColor;
            return this;
        }

        /**
         * 设置AlertDialog的messageColor
         *
         * @param messageColor
         * @return
         */
        public Builder setMessageColor(int messageColor) {
            this.messageColor = messageColor;
            return this;
        }

        /**
         * 设置AlertDialog的positiveButtonTextColor
         *
         * @param positiveButtonTextColor
         * @return
         */
        public Builder setPositiveButtonTextColor(int positiveButtonTextColor) {
            this.positiveButtonTextColor = positiveButtonTextColor;
            return this;
        }

        /**
         * 设置AlertDialog的negativeButtonTextColor
         *
         * @param negativeButtonTextColor
         * @return
         */
        public Builder setNegativeButtonTextColor(int negativeButtonTextColor) {
            this.negativeButtonTextColor = negativeButtonTextColor;
            return this;
        }

        /**
         * 设置AlertDialog的neutralButtonTextColor
         *
         * @param neutralButtonTextColor
         * @return
         */
        public Builder setNeutralButtonTextColor(int neutralButtonTextColor) {
            this.neutralButtonTextColor = neutralButtonTextColor;
            return this;
        }

        /**
         * 设置AlertDialog的titleUnit
         *
         * @param titleUnit
         * @return
         */
        public Builder setTitleUnit(int titleUnit) {
            this.titleUnit = titleUnit;
            return this;
        }

        /**
         * 设置AlertDialog的messageUnit
         *
         * @param messageUnit
         * @return
         */
        public Builder setMessageUnit(int messageUnit) {
            this.messageUnit = messageUnit;
            return this;
        }

        /**
         * 设置AlertDialog的positiveButtonTextUnit
         *
         * @param positiveButtonTextUnit
         * @return
         */
        public Builder setPositiveButtonTextUnit(int positiveButtonTextUnit) {
            this.positiveButtonTextUnit = positiveButtonTextUnit;
            return this;
        }

        /**
         * 设置AlertDialog的negativeButtonTextUnit
         *
         * @param negativeButtonTextUnit
         * @return
         */
        public Builder setNegativeButtonTextUnit(int negativeButtonTextUnit) {
            this.negativeButtonTextUnit = negativeButtonTextUnit;
            return this;
        }

        /**
         * 设置AlertDialog的neutralButtonTextUnit
         *
         * @param neutralButtonTextUnit
         * @return
         */
        public Builder setNeutralButtonTextUnit(int neutralButtonTextUnit) {
            this.neutralButtonTextUnit = neutralButtonTextUnit;
            return this;
        }


        /**
         * 设置AlertDialog的titleSize
         *
         * @param titleSize
         * @return
         */
        public Builder setTitleSize(int titleSize) {
            this.titleSize = titleSize;
            return this;
        }

        /**
         * 设置AlertDialog的messageSize
         *
         * @param messageSize
         * @return
         */
        public Builder setMessageSize(int messageSize) {
            this.messageSize = messageSize;
            return this;
        }

        /**
         * 设置AlertDialog的positiveButtonTextSize
         *
         * @param positiveButtonTextSize
         * @return
         */
        public Builder setPositiveButtonTextSize(int positiveButtonTextSize) {
            this.positiveButtonTextSize = positiveButtonTextSize;
            return this;
        }

        /**
         * 设置AlertDialog的negativeButtonTextSize
         *
         * @param negativeButtonTextSize
         * @return
         */
        public Builder setNegativeButtonTextSize(int negativeButtonTextSize) {
            this.negativeButtonTextSize = negativeButtonTextSize;
            return this;
        }

        /**
         * 设置AlertDialog的neutralButtonTextSize
         *
         * @param neutralButtonTextSize
         * @return
         */
        public Builder setNeutralButtonTextSize(int neutralButtonTextSize) {
            this.neutralButtonTextSize = neutralButtonTextSize;
            return this;
        }

        /**
         * 设置全局字体（title、message、按钮）
         *
         * @param color
         * @param unit
         * @param size
         * @return
         */
        public Builder setGlobalFont(int color, int unit, float size) {
            titleColor = messageColor = positiveButtonTextColor = negativeButtonTextColor = neutralButtonTextColor = color;
            titleUnit = messageUnit = positiveButtonTextUnit = negativeButtonTextUnit = neutralButtonTextUnit = unit;
            titleSize = messageSize = positiveButtonTextSize = negativeButtonTextSize = neutralButtonTextSize = size;
            return this;
        }

        /**
         * 设置自定义布局
         *
         * @param v
         * @return
         */
        public Builder setContentView(View v) {
            this.viewStyle = CUSTOM_DIALOG1;
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
            this.viewStyle = CUSTOM_DIALOG2;
            this.view = v;
            return this;
        }

        /**
         * @param layoutResId 设置AlertDialog布局ID
         * @return Builder
         */
        public Builder setContentView(int layoutResId) {
            return setContentView(layoutResId, null);
        }

        /**
         * @param layoutResId  设置AlertDialog布局ID
         * @param viewCallback AlertDialog布局回调
         * @return Builder
         */
        public Builder setContentView(int layoutResId, ViewInterface viewCallback) {
            this.viewStyle = CUSTOM_DIALOG3;
            this.layoutResId = layoutResId;
            this.viewCallback = viewCallback;
            return this;
        }

        /**
         * 设置默认Dialog
         *
         * @return
         */
        public Builder creatDefaultDialog() {
            this.viewStyle = DEFAULT_DIALOG;
            return this;
        }

        /**
         * 设置默认的提示框（只有提示信息文字和图标）
         *
         * @return
         */
        public Builder setDefaultPromptView1() {
            return setDefaultPromptView1(this.message, this.tipResId);
        }

        /**
         * 设置默认的提示框（只有提示信息文字和图标）
         *
         * @param tipMessage
         * @param tipResId
         * @return
         */
        public Builder setDefaultPromptView1(String tipMessage, @DrawableRes int tipResId) {
            return setDefaultPromptView1(tipMessage, tipResId, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        /**
         * 设置默认的提示框（只有提示信息文字和图标）
         *
         * @param tipMessage
         * @param tipResId
         * @param tipWidth
         * @param tipHeight
         * @return
         */
        public Builder setDefaultPromptView1(String tipMessage, @DrawableRes int tipResId, int tipWidth, int tipHeight) {
            this.viewStyle = DEFAULT_PROMPT1;
            this.message = tipMessage;
            this.tipResId = tipResId;
            this.tipWidth = tipWidth;
            this.tipHeight = tipHeight;
            return this;
        }

        /**
         * 设置默认Dialog（有标题，信息，确定和取消按钮）
         *
         * @return
         */
        public Builder setDefaultPromptView2() {
            this.viewStyle = DEFAULT_PROMPT2;
            return this;
        }

        /**
         * 设置默认Dialog（有标题，信息，确定和取消按钮）
         *
         * @param title
         * @param message
         * @param negativeButtonText
         * @param positiveButtonText
         * @return
         */
        public Builder setDefaultPromptView2(String title, String message, String negativeButtonText, String positiveButtonText) {
            this.viewStyle = DEFAULT_PROMPT2;
            this.title = title;
            this.message = message;
            this.negativeButtonText = negativeButtonText;
            this.positiveButtonText = positiveButtonText;
            return this;
        }

        /**
         * 设置AlertDialog背景圆角度数
         *
         * @param bgRadius
         * @return
         */
        public Builder setBgRadius(float bgRadius) {
            this.bgRadius = bgRadius;
            return this;
        }

        /**
         * 设置AlertDialog背景边框宽度
         *
         * @param bgStrokeWidth
         * @return
         */
        public Builder setBgStrokeWidth(int bgStrokeWidth) {
            this.bgStrokeWidth = bgStrokeWidth;
            return this;
        }

        /**
         * 设置AlertDialog背景边框宽度颜色
         *
         * @param bgStrokeColor
         * @return
         */
        public Builder setBgStrokeColor(@ColorInt int bgStrokeColor) {
            this.bgStrokeColor = bgStrokeColor;
            return this;
        }

        /**
         * 设置AlertDialog背景颜色
         *
         * @param bgColor
         * @return
         */
        public Builder setBgColor(@ColorInt int bgColor) {
            this.bgColor = bgColor;
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
         * 设置子View
         *
         * @param viewCallback ViewInterface
         * @return Builder
         */
        public Builder setViewCallback(ViewInterface viewCallback) {
            this.viewCallback = viewCallback;
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
            this.positiveButtonText = (String) this.context.getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, int color, int unit, float size, OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonTextColor = color;
            this.positiveButtonTextUnit = unit;
            this.positiveButtonTextSize = size;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = (String) this.context.getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, int color, int unit, float size, OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonTextColor = color;
            this.negativeButtonTextUnit = unit;
            this.negativeButtonTextSize = size;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNeutralButton(int neutralButtonText, OnClickListener listener) {
            this.neutralButtonText = (String) this.context.getText(neutralButtonText);
            this.neutralButtonClickListener = listener;
            return this;
        }

        public Builder setNeutralButton(String neutralButtonText, OnClickListener listener) {
            this.neutralButtonText = neutralButtonText;
            this.neutralButtonClickListener = listener;
            return this;
        }

        public Builder setNeutralButton(String neutralButtonText, int color, int unit, float size, OnClickListener listener) {
            this.neutralButtonText = neutralButtonText;
            this.neutralButtonTextColor = color;
            this.neutralButtonTextUnit = unit;
            this.neutralButtonTextSize = size;
            this.neutralButtonClickListener = listener;
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
            this.positiveButtonText = (String) this.context.getText(positiveButtonText);
            this.positiveClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, View.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, int color, int unit, float size, View.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonTextColor = color;
            this.positiveButtonTextUnit = unit;
            this.positiveButtonTextSize = size;
            this.positiveClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText, View.OnClickListener listener) {
            this.negativeButtonText = (String) this.context.getText(negativeButtonText);
            this.negativeClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, View.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, int color, int unit, float size, View.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonTextColor = color;
            this.negativeButtonTextUnit = unit;
            this.negativeButtonTextSize = size;
            this.negativeClickListener = listener;
            return this;
        }

        public CustomAlertDialog build() {
            return new CustomAlertDialog(this.context, this.themeResId, this);
        }
    }
}