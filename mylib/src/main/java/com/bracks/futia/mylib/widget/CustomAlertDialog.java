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

    @IntDef({DEFAULT_PROMPT1, EMPTY_VIEW, DEFAULT_DIALOG, DEFAULT_PROMPT2})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ViewStyle {
        public int style() default DEFAULT_PROMPT1;

    }

    private Context context;
    private String title;
    private String message;
    private String positiveButtonText;
    private String negativeButtonText;
    private String neutralButtonText;
    private @StyleRes
    int themeResId;
    private View contentView;
    private View view;
    private @ViewStyle
    int viewStyle = EMPTY_VIEW;
    private int bgResId;
    private int tipResId;
    private int height;
    private int width;
    private long delayMillis;
    private PostDelayListener postDelayListener = null;
    private OnClickListener positiveButtonClickListener = null;
    private OnClickListener negativeButtonClickListener = null;
    private OnClickListener neutralButtonClickListener = null;
    private View.OnClickListener positiveClickListener = null;
    private View.OnClickListener negativeClickListener = null;

    private AlertDialog dialog;

    public interface PostDelayListener {
        void delay();
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
        this.themeResId = builder.themeResId;
        this.contentView = builder.contentView;
        this.view = builder.view;
        this.viewStyle = builder.viewStyle;
        this.bgResId = builder.bgResId;
        this.tipResId = builder.tipResId;
        this.width = builder.width;
        this.height = builder.height;
        this.delayMillis = builder.delayMillis;
        this.postDelayListener = builder.postDelayListener;
        this.positiveButtonClickListener = builder.positiveButtonClickListener;
        this.negativeButtonClickListener = builder.negativeButtonClickListener;
        this.neutralButtonClickListener = builder.neutralButtonClickListener;
        this.positiveClickListener = builder.positiveClickListener;
        this.negativeClickListener = builder.negativeClickListener;
        getDialog(themeResId);

        //设置显示Dialog
        switch (this.viewStyle) {
            case EMPTY_VIEW:
                setDialogView(DEFAULT_PROMPT1);
                break;
            case DEFAULT_PROMPT1:
                setDialogView(DEFAULT_PROMPT1);
                break;
            case DEFAULT_PROMPT2:
                setDialogView(DEFAULT_PROMPT2);
                break;
            case DEFAULT_DIALOG:
                setDialogView(DEFAULT_DIALOG);
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
     * 设置Dialog的布局样式
     */
    private void setDialogView(@ViewStyle int viewStyle) {
        View view = null;
        TextView tvTitle;
        TextView tvMessage;
        ImageView image;
        switch (viewStyle) {
            case DEFAULT_PROMPT1:
                view = View.inflate(context, R.layout.custom_dialog_default_prompt, null);
                tvMessage = (TextView) view.findViewById(R.id.tv_message);
                image = (ImageView) view.findViewById(R.id.iv_image);
                tvMessage.setText(message);
                if (tipResId != 0) {
                    image.setBackgroundResource(tipResId);
                }
                setCustomView(view);
                break;
            case DEFAULT_PROMPT2:
                view = View.inflate(context, R.layout.common_dialog_layout, null);
                tvTitle = (TextView) view.findViewById(R.id.title);
                tvMessage = (TextView) view.findViewById(R.id.message);
                Button positiveButton = (Button) view.findViewById(R.id.positiveButton);
                Button negativeButton = (Button) view.findViewById(R.id.negativeButton);
                tvTitle.setText(title);
                tvMessage.setText(message);
                if (TextUtils.isEmpty(message)) {
                    tvMessage.setVisibility(View.GONE);
                } else if (TextUtils.isEmpty(title)) {
                    tvTitle.setVisibility(View.GONE);
                }
                positiveButton.setText(positiveButtonText);
                negativeButton.setText(negativeButtonText);
                positiveButton.setOnClickListener(positiveClickListener);
                negativeButton.setOnClickListener(negativeClickListener);
                setCustomView(view);
                break;
            case DEFAULT_DIALOG:
                creatDefaultDialog();
                break;
            default:
                break;
        }
    }

    /***
     * 获取一个dialog
     * @return
     */
    private AlertDialog getDialog(@StyleRes int themeResId) {
        return dialog = new AlertDialog.Builder(context, themeResId).create();
    }

    /**
     * 创建默认的Dialog
     */
    private void creatDefaultDialog() {
        //创建对话框
        AlertDialog dialog = getDialog(themeResId);
        //设置对话框icon
        dialog.setIcon(R.drawable.common_empty_failed);
        //设置对话框标题
        dialog.setTitle(title);
        //设置文字显示内容
        dialog.setMessage(message);
        //分别设置三个button
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, positiveButtonText, positiveButtonClickListener);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, negativeButtonText, negativeButtonClickListener);
        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, neutralButtonText, neutralButtonClickListener);
        //显示对话框
        dialog.show();
    }

    /**
     * 创建自定义AlertDialog布局
     *
     * @param view
     * @return
     */
    private void setCustomView(View view) {
        //添加布局
        dialog.show();
        dialog.setContentView(view);
    }

    private void setMView(View view) {
        //添加布局
        dialog.setView(view);
        dialog.show();
    }


    /**
     * 设置AlertDialog背景
     *
     * @param resId
     * @return
     */
    private void setBackgroundDrawableResource(@DrawableRes int resId) {
        Window window = dialog.getWindow();
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
        Window window = dialog.getWindow();
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
        Window window = dialog.getWindow();
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
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                if (postDelayListener != null) {
                    postDelayListener.delay();
                }
            }
        }, delayMillis);
    }

    /**
     * 重写dismiss方法
     */
    @Override
    public void dismiss() {
        super.dismiss();
        if (dialog != null && dialog.isShowing()) {
        	dialog.dismiss();
            dialog = null;
        }
    }


    public static class Builder {
        private Context context = null;
        private @StyleRes
        int themeResId = 0;
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

        public Builder setIcon(int iconId) {
            return this;
        }

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

        public Builder setDefaultPromptView1(String tipMessage, @DrawableRes int tipResId) {
            viewStyle = DEFAULT_PROMPT1;
            this.message = tipMessage;
            this.tipResId = tipResId;
            return this;
        }

        public Builder setDefaultPromptView2() {
            viewStyle = DEFAULT_PROMPT2;
            return this;
        }

        public Builder setDefaultPromptView2(String title, String message, String negativeButtonText, String positiveButtonText) {
            viewStyle = DEFAULT_PROMPT2;
            this.title = title;
            this.message = message;
            this.negativeButtonText = negativeButtonText;
            this.positiveButtonText = positiveButtonText;
            return this;
        }

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
         * 设置延迟消失
         *
         * @param delayMillis
         * @return
         */
        public Builder setDelay(long delayMillis) {
            this.setDelay(delayMillis, null);
            return this;
        }

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
