package com.bracks.utils.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bracks.utils.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


/**
 * good programmer.
 *
 * @date : 2018-11-17 上午 11:39
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public abstract class CustomBottomSheetFrag extends BottomSheetDialogFragment {
    /**
     * 对应于BottomSheetBehavior的状态
     */
    public int bottomSheetBehaviorState;
    protected Context mContext;
    protected Dialog dialog;
    protected View rootView;
    protected BottomSheetBehavior mBehavior;
    /**
     * 顶部向下偏移量
     */
    private int topOffset;
    /**
     * PeekHeight高度
     */
    private int peekHeight = -1;
    /**
     * 布局高度
     */
    private int layoutHeight;
    /**
     * 折叠显示的菜单布局
     */
    private View peekView;
    /**
     * peekHeight高度和布局是否一样
     */
    private boolean isPeekHeightWithLayout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyles(BottomSheetDialogFragment.STYLE_NORMAL, R.style.TransBottomSheetDialogStyle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解除缓存View和当前ViewGroup的关联
        ((ViewGroup) (rootView.getParent())).removeView(rootView);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //每次打开都调用该方法 类似于onCreateView 用于返回一个Dialog实例
        dialog = super.onCreateDialog(savedInstanceState);
        if (rootView == null) {
            //缓存下来的View 当为空时才需要初始化 并缓存
            rootView = View.inflate(mContext, getLayoutResId(), null);
            //默认设置等于view的高
            setPeekHeightWithLayout();
            initView(rootView);
            //设置默认布局高度为全屏高度减去向上偏量
            setLayoutHeight();
        }
        resetView();
        //设置View重新关联
        dialog.setContentView(rootView);
        //圆角边的关键
        ((View) rootView.getParent()).setBackgroundColor(Color.TRANSPARENT);
        rootView.post(new Runnable() {
            @Override
            public void run() {
                /**
                 * PeekHeight默认高度256dp 会在该高度上悬浮
                 * 设置等于view的高 就不会卡住
                 */
                if (peekHeight == -1 && peekView != null) {
                    peekHeight = peekView.getHeight();
                }
                if (peekHeight != -1) {
                    mBehavior.setPeekHeight(peekHeight);
                } else {
                    if (isPeekHeightWithLayout) {
                        if (topOffset != 0) {
                            mBehavior.setPeekHeight(rootView.getHeight());
                        }
                    }
                }
            }
        });
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置软键盘不自动弹出
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        FrameLayout bottomSheet = dialog.getDelegate().findViewById(R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomSheet.getLayoutParams();
            layoutParams.height = layoutHeight;
            mBehavior = BottomSheetBehavior.from(bottomSheet);
            mBehavior.setHideable(true);
            switch (bottomSheetBehaviorState) {
                case BottomSheetBehavior.STATE_EXPANDED:
                    expanded();
                    break;
                case BottomSheetBehavior.STATE_COLLAPSED:
                    collapsed(peekHeight);
                    break;
                case BottomSheetBehavior.STATE_HIDDEN:
                    hidde();
                    break;
                default:
                    //默认为展开状态
                    expanded();
                    break;
            }
        }
    }

    public abstract int getLayoutResId();

    /**
     * 初始化View和设置数据等操作的方法
     */
    public abstract void initView(View rootView);

    /**
     * 重置的View和数据的空方法 子类可以选择实现
     * 为避免多次inflate 父类缓存rootView
     * 所以不会每次打开都调用{@link #initView(View rootView)}方法
     * 但是每次都会调用该方法 给子类能够重置View和数据
     */
    public void resetView() {

    }

    /**
     * 设置style：可以设置弹出时背景不变暗
     * * <style name="TransBottomSheetDialogStyle" parent="Theme.Design.Light.BottomSheetDialog">
     * * <item name="android:windowFrame">@null</item>
     * * <item name="android:windowIsFloating">true</item>
     * * <item name="android:windowIsTranslucent">true</item>
     * * <item name="android:background">@android:color/transparent</item>
     * * <item name="android:backgroundDimEnabled">false</item>
     * * </style>
     *
     * @param style
     * @param theme
     */
    public void setStyles(int style, @StyleRes int theme) {
        setStyle(style, theme);
    }

    public boolean isShowing() {
        return dialog != null && dialog.isShowing();
    }

    protected int getTopOffset() {
        return topOffset;
    }

    /**
     * 设置偏离顶部的距离：isPeekHeightWithLayout = true时才有意义
     *
     * @param topOffset
     */
    protected void setTopOffset(int topOffset) {
        this.topOffset = topOffset;
        setPeekHeightWithLayout();
    }

    public int getPeekHeight() {
        return peekHeight;
    }

    public void setPeekHeight(int peekHeight) {
        this.peekHeight = peekHeight;
    }

    /**
     * 设置布局高度
     */
    private int setLayoutHeight() {
        if (layoutHeight == 0 && getContext() != null) {
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Point point = new Point();
            if (wm != null) {
                //使用Point已经减去了状态栏高度
                wm.getDefaultDisplay().getSize(point);
                layoutHeight = point.y - topOffset;
            }
        }
        return layoutHeight;
    }

    /**
     * 设置布局高度（优先级大于设置setTopOffset）
     *
     * @param layoutHeight
     */
    public void setLayoutHeight(int layoutHeight) {
        this.layoutHeight = layoutHeight;
    }

    /**
     * 设置peekHeight高度和布局一样
     *
     * @param withLayout
     */
    protected void setPeekHeightWithLayout(boolean withLayout) {
        //只有当没有设置topOffset时才可以设置为true，否则setTopOffset失效
        if (topOffset != 0) {
            isPeekHeightWithLayout = true;
        } else {
            isPeekHeightWithLayout = withLayout;
        }
    }

    protected void setPeekHeightWithLayout() {
        isPeekHeightWithLayout = true;
    }

    /**
     * 隐藏
     */
    public void hidde() {
        if (mBehavior != null) {
            mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            bottomSheetBehaviorState = BottomSheetBehavior.STATE_HIDDEN;
        }
    }

    /**
     * 展开
     */
    public void expanded() {
        if (mBehavior != null) {
            mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            bottomSheetBehaviorState = BottomSheetBehavior.STATE_EXPANDED;
        }
    }

    /**
     * 折叠
     *
     * @param peekHeight
     */
    public void collapsed(int peekHeight) {
        this.peekHeight = peekHeight;
        if (mBehavior != null) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            bottomSheetBehaviorState = BottomSheetBehavior.STATE_COLLAPSED;
        }
    }

    /**
     * 折叠
     */
    public void collapsed() {
        collapsed(-1);
    }

    /**
     * 折叠并设置peekView
     *
     * @param peekView
     */
    public void collapsed(View peekView) {
        this.peekView = peekView;
        collapsed(-1);
    }

    public void setPeekView(View peekView) {
        this.peekView = peekView;
    }
}