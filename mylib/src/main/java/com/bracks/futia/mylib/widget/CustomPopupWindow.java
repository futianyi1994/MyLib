package com.bracks.futia.mylib.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.RequiresApi;


/**
 * Good programmer.
 *
 * @author futia
 * @date 2017-09-27 上午 10:06
 * Email:futianyi1994@126.com
 * Description: 封装一个通用的PopupWindow参考:http://blog.csdn.net/u013700502/article/details/71275093
 * <p>Example:
 * <pre><code>
 * popup = new CustomPopupWindow
 *                         .Builder(this)
 *                         .setView(R.layout.pop_filter)
 *                         .setFocusable(true)
 *                         .setBackgroundDrawable(new BitmapDrawable())
 *                         .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
 *                         .setViewOnclickListener((view, layoutResId) -> {
 *
 *                         })
 *                         .create();
 * popup.showAsDropDown(
 *         tvSelect,
 *         ConvertUtils.dp2px(10),
 *         ConvertUtils.dp2px(10),
 *         Gravity.CENTER,
 *         () -> BarUtils.hideNavBar(popup.getContentView())
 * );
 * </code></pre>
 */
public class CustomPopupWindow extends PopupWindow {
    private Context context;
    private Activity activity;
    private LayoutInflater inflater;
    private static CountDownTimer timer;
    private PopupController controller;

    public interface TimerFinishListener {
        /**
         * Timer计时回调
         *
         * @param millisUntilFinished
         */
        void onTick(long millisUntilFinished);

        /**
         * Timer结束时的回调
         */
        void onFinish();
    }

    public interface AfterShowListener {
        /**
         * 处理一些在需要获取焦点前、显示popwind之后的操作：如隐藏导航栏需要在显示之前失去焦点显示之后重新获取焦点注意需要通过BarUtils.hideNavBar(popupWindow.getContentView())
         */
        void onAfterShow();
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

    public CustomPopupWindow(Context context) {
        super(context);
        if (context instanceof Activity) {
            this.activity = (Activity) context;
        }
        this.context = context;
        this.inflater = LayoutInflater.from(context);

        controller = new PopupController(context, this);
    }

    public Context getContext() {
        return context;
    }

    public Activity getActivity() {
        return activity;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    protected void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void setBackgroundAlpha(float bgAlpha) {
        controller.setBackGroundLevel(bgAlpha);
    }

    /**
     * 计时消失pop
     *
     * @param millisInFuture    第一个参数表示总时间，
     * @param countDownInterval 第二个参数表示间隔时间
     */
    public CountDownTimer setTimerDismiss(long millisInFuture, long countDownInterval) {
        return setTimerDismiss(millisInFuture, countDownInterval, null);
    }

    public CountDownTimer setTimerDismiss(long millisInFuture, long countDownInterval, final TimerFinishListener timerFinishListener) {
        return timer != null ? timer : new CountDownTimer(millisInFuture, countDownInterval) {

            @Override
            public void onTick(long millisUntilFinished) {
                if (timerFinishListener != null) {
                    timerFinishListener.onTick(millisUntilFinished);
                }
            }

            @Override
            public void onFinish() {
                dismiss();
                if (timerFinishListener != null) {
                    timerFinishListener.onFinish();
                }
            }
        }.start();
    }

    public void cancalTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public int getWidth() {
        return controller.mPopupView.getMeasuredWidth();
    }

    @Override
    public int getHeight() {
        return controller.mPopupView.getMeasuredHeight();
    }

    /**
     * 处理一些在需要获取焦点前、显示popwind之后的操作：如隐藏导航栏需要在显示之前失去焦点显示之后重新获取焦点注意需要通过BarUtils.hideNavBar(popupWindow.getContentView())
     *
     * @param anchor
     * @param listener
     */
    public void showAsDropDown(View anchor, AfterShowListener listener) {
        showAsDropDown(anchor, 0, 0, listener);
    }


    /**
     * 处理一些在需要获取焦点前、显示popwind之后的操作：如隐藏导航栏需要在显示之前失去焦点显示之后重新获取焦点注意需要通过BarUtils.hideNavBar(popupWindow.getContentView())
     *
     * @param anchor
     * @param xoff
     * @param yoff
     * @param listener
     */
    public void showAsDropDown(View anchor, int xoff, int yoff, AfterShowListener listener) {
        setFocusable(false);
        update();
        super.showAsDropDown(anchor, xoff, yoff);
        if (listener != null) {
            listener.onAfterShow();
        }
        setFocusable(true);
        update();
    }

    /**
     * 处理一些在需要获取焦点前、显示popwind之后的操作：如隐藏导航栏需要在显示之前失去焦点显示之后重新获取焦点注意需要通过BarUtils.hideNavBar(popupWindow.getContentView())
     *
     * @param anchor
     * @param xoff
     * @param yoff
     * @param gravity
     * @param listener
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity, AfterShowListener listener) {
        setFocusable(false);
        update();
        super.showAsDropDown(anchor, xoff, yoff, gravity);
        if (listener != null) {
            listener.onAfterShow();
        }
        setFocusable(true);
        update();
    }

    /**
     * 处理一些在需要获取焦点前、显示popwind之后的操作：如隐藏导航栏需要在显示之前失去焦点显示之后重新获取焦点注意需要通过BarUtils.hideNavBar(popupWindow.getContentView())
     *
     * @param parent
     * @param gravity
     * @param x
     * @param y
     * @param listener
     */
    public void showAtLocation(View parent, int gravity, int x, int y, AfterShowListener listener) {
        setFocusable(false);
        update();
        super.showAtLocation(parent, gravity, x, y);
        if (listener != null) {
            listener.onAfterShow();
        }
        setFocusable(true);
        update();
    }

    @Override
    public void dismiss() {
        if (isShowing()) {
            super.dismiss();
        }
        cancalTimer();
        controller.setBackGroundLevel(1.0f);
    }


    public static class Builder {
        private PopupController.PopupParams params;
        private ViewInterface listener;
        private CustomPopupWindow popupWindow;

        public Builder(Context context) {
            params = new PopupController.PopupParams(context);
        }

        /**
         * @param layoutResId 设置PopupWindow 布局ID
         * @return Builder
         */
        public Builder setView(int layoutResId) {
            params.mView = null;
            params.layoutResId = layoutResId;
            return this;
        }

        /**
         * @param view 设置PopupWindow布局
         * @return Builder
         */
        public Builder setView(View view) {
            params.mView = view;
            params.layoutResId = 0;
            return this;
        }

        /**
         * 设置子View
         *
         * @param listener ViewInterface
         * @return Builder
         */
        public Builder setViewOnclickListener(ViewInterface listener) {
            this.listener = listener;
            return this;
        }

        /**
         * 设置宽度和高度 如果不设置 默认是wrap_content
         *
         * @param width 宽
         * @return Builder
         */
        public Builder setWidthAndHeight(int width, int height) {
            params.mWidth = width;
            params.mHeight = height;
            return this;
        }

        /**
         * 设置背景灰色程度
         * <p>
         * 注意在activity的主题设置有：<item name="android:windowIsTranslucent">true</item> 时请设置level为1.0f，否则会导致透明而使得activity页面重叠
         *
         * @param level 0.0f-1.0f
         * @return Builder
         */
        public Builder setBackGroundLevel(float level) {
            params.isShowBg = true;
            params.bg_level = level;
            return this;
        }

        /**
         * 设置软键盘的状态可以解决虚拟导航栏底部被挡住setClippingEnabled(false)时无效
         *
         * @param softInputMode
         * @return
         */
        public Builder setSoftInputMode(int softInputMode) {
            if (softInputMode == WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE) {
                params.clippingEnabled = true;
            }
            params.softInputMode = softInputMode;
            return this;
        }

        /**
         * 设置背景图片
         *
         * @param background
         * @return
         */
        public Builder setBackgroundDrawable(Drawable background) {
            params.background = background;
            return this;
        }

        /**
         * 设置动画
         *
         * @return Builder
         */
        public Builder setAnimationStyle(int animationStyle) {
            params.isShowAnim = true;
            params.animationStyle = animationStyle;
            return this;
        }

        public Builder setTouchable(boolean touchable) {
            params.touchable = touchable;
            return this;
        }

        /**
         * 是否可点击Outside
         *
         * @param outsideTouchable 是否可点击
         * @return Builder
         */
        public Builder setOutsideTouchable(boolean outsideTouchable) {
            params.outsideTouchable = outsideTouchable;
            return this;
        }


        public Builder setFocusable(boolean focusable) {
            params.focusable = focusable;
            return this;
        }

        /**
         * 设置是否允许PopupWindow的范围超过屏幕范围
         *
         * @param clippingEnabled
         * @return
         */
        public Builder setClippingEnabled(boolean clippingEnabled) {
            params.clippingEnabled = clippingEnabled;
            return this;
        }

        /**
         * 计时消失pop
         *
         * @param millisInFuture      第一个参数表示总时间，
         * @param countDownInterval   第二个参数表示间隔时间
         * @param timerFinishListener
         */
        public Builder setTimerDismiss(long millisInFuture, long countDownInterval, final TimerFinishListener timerFinishListener) {
            timer = timer != null ? timer : new CountDownTimer(millisInFuture, countDownInterval) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (timerFinishListener != null) {
                        timerFinishListener.onTick(millisUntilFinished);
                    }
                }

                @Override
                public void onFinish() {
                    popupWindow.dismiss();
                    if (timerFinishListener != null) {
                        timerFinishListener.onFinish();
                    }
                }
            }.start();
            return this;
        }

        public Builder setTimerDismiss(long millisInFuture, long countDownInterval) {
            setTimerDismiss(millisInFuture, countDownInterval, null);
            return this;
        }

        public CustomPopupWindow create() {
            popupWindow = new CustomPopupWindow(params.mContext);
            params.apply(popupWindow.controller);
            if (listener != null && params.layoutResId != 0) {
                listener.getChildView(popupWindow.controller.mPopupView, params.layoutResId);
            }
            int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            popupWindow.controller.mPopupView.measure(widthMeasureSpec, heightMeasureSpec);
            return popupWindow;
        }
    }

    static class PopupController {
        private int layoutResId;
        private Context context;
        private PopupWindow popupWindow;
        private View mPopupView;
        private View mView;
        private Window mWindow;

        PopupController(Context context, PopupWindow popupWindow) {
            this.context = context;
            this.popupWindow = popupWindow;
        }

        public void setContentView(int layoutResId) {
            mView = null;
            this.layoutResId = layoutResId;
            installContent();
        }

        public void setContentView(View view) {
            mView = view;
            this.layoutResId = 0;
            installContent();
        }

        private void installContent() {
            if (layoutResId != 0) {
                mPopupView = LayoutInflater.from(context).inflate(layoutResId, null);
            } else if (mView != null) {
                mPopupView = mView;
            }
            popupWindow.setContentView(mPopupView);
        }

        private void setWidthAndHeight(int width, int height) {
            if (width == 0 || height == 0) {
                //如果没设置宽高，默认是WRAP_CONTENT
                popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            } else {
                popupWindow.setWidth(width);
                popupWindow.setHeight(height);
            }
        }

        private void setSoftInputMode(int softInputMode) {
            popupWindow.setSoftInputMode(softInputMode);
        }

        private void setBackGroundLevel(float level) {
            mWindow = ((Activity) context).getWindow();
            WindowManager.LayoutParams params = mWindow.getAttributes();
            params.alpha = level;
            mWindow.setAttributes(params);
        }

        private void setBackgroundDrawable(Drawable background) {
            popupWindow.setBackgroundDrawable(background);
        }


        private void setAnimationStyle(int animationStyle) {
            popupWindow.setAnimationStyle(animationStyle);
        }

        private void setOutsideTouchable(boolean touchable) {
            popupWindow.setOutsideTouchable(touchable);
        }

        public void setTouchable(boolean touchable) {
            popupWindow.setTouchable(touchable);
        }

        public void setFocusable(boolean focusable) {
            popupWindow.setFocusable(focusable);
        }

        public void setClippingEnabled(boolean clippingEnabled) {
            popupWindow.setClippingEnabled(clippingEnabled);
        }

        static class PopupParams {
            /**
             * 布局id
             */
            private int layoutResId;
            /**
             * 布局
             */
            private View mView;
            /**
             * 上下文
             */
            private Context mContext;
            /**
             * 宽高
             */
            private int mWidth, mHeight;
            /**
             * 是否有背景和动画
             */
            private boolean isShowBg, isShowAnim;
            /**
             * 屏幕背景灰色程度
             */
            private float bg_level;
            /**
             * 动画Id
             */
            private int animationStyle;

            /**
             * 软键盘状态
             */
            private int softInputMode;
            /**
             * 背景
             */
            private Drawable background;

            private boolean touchable = true;
            private boolean focusable;
            private boolean outsideTouchable = false;
            private boolean clippingEnabled = true;

            public PopupParams(Context mContext) {
                this.mContext = mContext;
            }

            public void apply(PopupController controller) {
                if (mView != null) {
                    controller.setContentView(mView);
                } else if (layoutResId != 0) {
                    controller.setContentView(layoutResId);
                } else {
                    throw new IllegalArgumentException("PopupView's contentView is null");
                }
                controller.setWidthAndHeight(mWidth, mHeight);
                controller.setSoftInputMode(softInputMode);
                controller.setBackgroundDrawable(background);
                controller.setTouchable(touchable);
                controller.setFocusable(focusable);
                controller.setOutsideTouchable(outsideTouchable);
                controller.setClippingEnabled(clippingEnabled);
                if (isShowBg) {
                    //设置背景颜色
                    controller.setBackGroundLevel(bg_level);
                }
                if (isShowAnim) {
                    controller.setAnimationStyle(animationStyle);
                }
            }
        }
    }
}
