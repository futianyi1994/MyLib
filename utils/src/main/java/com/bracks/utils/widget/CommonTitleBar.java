package com.bracks.utils.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.bracks.utils.R;


/**
 * good programmer.
 *
 * @data : 2017-11-14 下午 03:50
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */

public class CommonTitleBar extends FrameLayout {
    RelativeLayout rlTitle;
    FrameLayout flTitleLeft;
    ImageView ivTitleLeft;
    TextView tvTitleLeft;
    FrameLayout flTitleCenter;
    ImageView ivTitleCenter;
    TextView tvTitleCenter;
    FrameLayout flTitleRight;
    ImageView ivTitleRightLeft;
    TextView tvTitleRight;
    ImageView ivTitleRight;
    CommonSearchView searchView;

    private Context mContext;
    /**
     * 标题左边点击监听事件
     */
    private TitleBarLeftListener mTitleBarLeftListener;
    /**
     * 标题中间点击监听事件
     */
    private TitleBarCenterListener mTitleBarCenterListener;
    /**
     * 标题右边点击监听事件
     */
    private TitleBarRightListener mTitleBarRightListener;
    /**
     * 标题右边点击监听事件（左边图标）
     */
    private TitleBarRightLeftListener mTitleBarRightLeftListener;
    /**
     * 标题右边点击监听事件（右边图标）
     */
    private TitleBarRightRightListener mTitleBarRightRightListener;

    /**
     * 左边点击事件接口
     */
    public interface TitleBarLeftListener {
        void onClickTitleLeftListener(View v);
    }

    /**
     * 中间点击事件接口
     */
    public interface TitleBarCenterListener {
        void onClickTitleCenterListener(View v);
    }

    /**
     * 右边点击事件接口
     */
    public interface TitleBarRightListener {
        void onClickTitleRightListener(View v);
    }

    /**
     * 右边点击事件接口(左边图标)
     */
    public interface TitleBarRightLeftListener {
        void onClickTitleRightLeftListener(View v);
    }

    /**
     * 右边点击事件接口(右边图标)
     */
    public interface TitleBarRightRightListener {
        void onClickTitleRightRightListener(View v);
    }

    /**
     * 设置左边点击事件
     *
     * @param mTitleBarLeftListener
     * @return
     */
    public CommonTitleBar setmTitleBarLeftListener(TitleBarLeftListener mTitleBarLeftListener) {
        this.mTitleBarLeftListener = mTitleBarLeftListener;
        return this;
    }

    /**
     * 设置中间点击事件
     *
     * @param mTitleBarCenterListener
     * @return
     */
    public CommonTitleBar setmTitleBarCenterListener(TitleBarCenterListener mTitleBarCenterListener) {
        this.mTitleBarCenterListener = mTitleBarCenterListener;
        return this;
    }

    /**
     * 设置右边点击事件
     *
     * @param mTitleBarRightListener
     * @return
     */
    public CommonTitleBar setmTitleBarRightListener(TitleBarRightListener mTitleBarRightListener) {
        this.mTitleBarRightListener = mTitleBarRightListener;
        return this;
    }

    /**
     * 设置右边点击事件(左边图标)
     *
     * @param mTitleBarRightLeftListener
     * @return
     */
    public CommonTitleBar setmTitleBarRightLeftListener(TitleBarRightLeftListener mTitleBarRightLeftListener) {
        this.mTitleBarRightLeftListener = mTitleBarRightLeftListener;
        return this;
    }

    /**
     * 设置右边点击事件(右边图标)
     *
     * @param mTitleBarRightRightListener
     * @return
     */
    public CommonTitleBar setmTitleBarRightRightListener(TitleBarRightRightListener mTitleBarRightRightListener) {
        this.mTitleBarRightRightListener = mTitleBarRightRightListener;
        return this;
    }

    /**
     * 获得左边点击事件接口
     *
     * @return
     */
    public TitleBarLeftListener getmTitleBarLeftListener() {
        return mTitleBarLeftListener;
    }

    /**
     * 获得中间点击事件接口
     *
     * @return
     */
    public TitleBarCenterListener getmTitleBarCenterListener() {
        return mTitleBarCenterListener;
    }

    /**
     * 获得右边点击事件接口
     *
     * @return
     */
    public TitleBarRightListener getmTitleBarRightListener() {
        return mTitleBarRightListener;
    }

    /**
     * 获得右边点击事件接口(左边图标)
     *
     * @return
     */
    public TitleBarRightLeftListener getmTitleBarRightLeftListener() {
        return mTitleBarRightLeftListener;
    }

    /**
     * 获得右边点击事件接口(右边图标)
     *
     * @return
     */
    public TitleBarRightRightListener getmTitleBarRightRightListener() {
        return mTitleBarRightRightListener;
    }


    public CommonTitleBar(Context context) {
        this(context, null);
    }

    public CommonTitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        String mTextLeft = null;
        String mTextCenter = null;
        String mTextRight = null;
        int mTextColorLeft = Color.WHITE;
        int mTextColorCenter = Color.WHITE;
        int mTextColorRight = Color.WHITE;
        int mTitlebarBg = Color.WHITE;
        int mTextSizeLeft = 14;
        int mTextSizeCenter = 18;
        int mTextSizeRight = 14;
        //状态栏的高度
        int statusHeight = BarUtils.getStatusBarHeight();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CommonTitleBar, defStyleAttr, R.style.TitleBarDefaultStyle);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.CommonTitleBar_TextLeft) {
                mTextLeft = a.getString(attr);
            } else if (attr == R.styleable.CommonTitleBar_TextCenter) {
                mTextCenter = a.getString(attr);
            } else if (attr == R.styleable.CommonTitleBar_TextRight) {
                mTextRight = a.getString(attr);
            } else if (attr == R.styleable.CommonTitleBar_TextColorLeft) {
                mTextColorLeft = a.getColor(attr, Color.BLACK);
            } else if (attr == R.styleable.CommonTitleBar_TextColorCenter) {
                mTextColorCenter = a.getColor(attr, Color.BLACK);
            } else if (attr == R.styleable.CommonTitleBar_TextColorRight) {
                mTextColorRight = a.getColor(attr, Color.BLACK);
            } else if (attr == R.styleable.CommonTitleBar_TextSizeLeft) {
                mTextSizeLeft = a.getDimensionPixelSize(attr, 14);
            } else if (attr == R.styleable.CommonTitleBar_TextSizeCenter) {
                mTextSizeCenter = a.getDimensionPixelSize(attr, 18);
            } else if (attr == R.styleable.CommonTitleBar_TextSizeRight) {
                mTextSizeRight = a.getDimensionPixelSize(attr, 14);
            } else if (attr == R.styleable.CommonTitleBar_srcLeft) {
                mDrawLeft = a.getDrawable(attr);
            } else if (attr == R.styleable.CommonTitleBar_srcCenter) {
                mDrawCenter = a.getDrawable(attr);
            } else if (attr == R.styleable.CommonTitleBar_srcRightLeft) {
                mDrawRightLeft = a.getDrawable(attr);
            } else if (attr == R.styleable.CommonTitleBar_srcRight) {
                mDrawRight = a.getDrawable(attr);
            } else if (attr == R.styleable.CommonTitleBar_bg) {
                mTitlebarBg = a.getColor(attr, Color.WHITE);
            } else if (attr == R.styleable.CommonTitleBar_statusHeight) {
                statusHeight = a.getDimensionPixelSize(attr, statusHeight);
            }
        }
        a.recycle();
        init(context);
        setTextLeft(mTextLeft);
        setTextCenter(mTextCenter);
        setTextRight(mTextRight);
        setTextColorLeft(mTextColorLeft);
        setTextColorCenter(mTextColorCenter);
        setTextColorRight(mTextColorRight);
        setTextSizeLeft(mTextSizeLeft);
        setTextSizeCenter(mTextSizeCenter);
        setTextSizeRight(mTextSizeRight);
        setImgLeft(mDrawLeft);
        setImgCenter(mDrawCenter);
        setImgRightLeft(mDrawRightLeft);
        setImgRight(mDrawRight);
        setStatusHeight(statusHeight);
        setTitleBgColor(mTitlebarBg);
        //setTitleBgColor(SkinUtils.getInstance(context).getSkinColor());
    }

    public void init(Context c) {
        this.mContext = c;
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.custom_titlebar, this, true);
        this.ivTitleLeft = rootView.findViewById(R.id.iv_title_left);
        this.tvTitleLeft = rootView.findViewById(R.id.tv_title_left);
        this.flTitleLeft = rootView.findViewById(R.id.fl_title_left);
        this.ivTitleCenter = rootView.findViewById(R.id.iv_title_center);
        this.tvTitleCenter = rootView.findViewById(R.id.tv_title_center);
        this.flTitleCenter = rootView.findViewById(R.id.fl_title_center);
        this.ivTitleRightLeft = rootView.findViewById(R.id.iv_title_right_left);
        this.tvTitleRight = rootView.findViewById(R.id.tv_title_right);
        this.ivTitleRight = rootView.findViewById(R.id.iv_title_right);
        this.flTitleRight = rootView.findViewById(R.id.fl_title_right);
        this.searchView = rootView.findViewById(R.id.commonSearchView);
        this.rlTitle = rootView.findViewById(R.id.rl_title_all);
        initListener();
    }

    /**
     * 初始化点击事件监听
     */
    private void initListener() {
        flTitleLeft.setOnClickListener(v -> {
            if (mTitleBarLeftListener != null) {
                mTitleBarLeftListener.onClickTitleLeftListener(v);
            }
        });
        tvTitleCenter.setOnClickListener(v -> {
            if (mTitleBarCenterListener != null) {
                mTitleBarCenterListener.onClickTitleCenterListener(v);
            }
        });
        flTitleRight.setOnClickListener(v -> {
            if (mTitleBarRightListener != null) {
                mTitleBarRightListener.onClickTitleRightListener(v);
            }
        });
        ivTitleRightLeft.setOnClickListener(view -> {
            if (mTitleBarRightLeftListener != null) {
                mTitleBarRightLeftListener.onClickTitleRightLeftListener(view);
            }
        });
        ivTitleRight.setOnClickListener(view -> {
            if (mTitleBarRightRightListener != null) {
                mTitleBarRightRightListener.onClickTitleRightRightListener(view);
            }
        });
    }

    /**
     * 设置status,根据不同版本号，4.4版本号以上才能设置状态栏的颜色。
     * 小于4.4,不能设置状态栏，这个属性失效
     *
     * @return
     */
    public CommonTitleBar setStatusHeight(int height) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.setPadding(0, height, 0, 0);
        } else {
            //小于4.4
            this.setPadding(0, 0, 0, 0);
        }
        return this;
    }

    /**
     * 设置标题栏背景颜色
     *
     * @param bgColor
     * @return
     */
    public CommonTitleBar setTitleBgColor(int bgColor) {
        rlTitle.setBackgroundColor(bgColor);
        this.setBackgroundColor(bgColor);
        return this;
    }

    /**
     * 设置左边视图图标
     *
     * @return
     */
    public CommonTitleBar setImgLeft(Drawable drawLeft) {
        if (null != drawLeft) {
            ivTitleLeft.setImageDrawable(drawLeft);
            ivTitleLeft.setVisibility(VISIBLE);
        }
        return this;
    }

    private Drawable mDrawLeft;

    public CommonTitleBar setImgLeft(int resource) {
        try {
            mDrawLeft = mContext.getResources().getDrawable(resource);
        } catch (Resources.NotFoundException e) {
            mDrawLeft = null;
            e.printStackTrace();
        }
        ivTitleLeft.setImageDrawable(mDrawLeft);
        ivTitleLeft.setVisibility(VISIBLE);
        return this;
    }

    /**
     * 设置中间视图图标
     *
     * @return
     */
    public CommonTitleBar setImgCenter(Drawable drawCenter) {
        if (null != drawCenter) {
            ivTitleCenter.setImageDrawable(drawCenter);
            ivTitleCenter.setVisibility(VISIBLE);
        }
        return this;
    }

    private Drawable mDrawCenter;

    public CommonTitleBar setImgCenter(int resource) {
        try {
            mDrawCenter = mContext.getResources().getDrawable(resource);
        } catch (Resources.NotFoundException e) {
            mDrawCenter = null;
            e.printStackTrace();
        }
        ivTitleCenter.setImageDrawable(mDrawCenter);
        ivTitleCenter.setVisibility(VISIBLE);
        return this;
    }

    /**
     * 设置右边视图图标(左边图标)
     *
     * @param drawRightLeft
     * @return
     */
    public CommonTitleBar setImgRightLeft(Drawable drawRightLeft) {
        if (null != drawRightLeft) {
            ivTitleRightLeft.setImageDrawable(drawRightLeft);
            ivTitleRightLeft.setVisibility(VISIBLE);
        }
        return this;
    }

    private Drawable mDrawRightLeft;

    public CommonTitleBar setImgRightLeft(int resource) {
        try {
            mDrawRightLeft = mContext.getResources().getDrawable(resource);
        } catch (Resources.NotFoundException e) {
            mDrawRightLeft = null;
            e.printStackTrace();
        }
        ivTitleRightLeft.setImageDrawable(mDrawRightLeft);
        ivTitleRightLeft.setVisibility(VISIBLE);
        return this;
    }

    /**
     * 设置右边视图图标(右边图标)
     *
     * @param drawRight
     * @return
     */
    public CommonTitleBar setImgRight(Drawable drawRight) {
        if (null != drawRight) {
            ivTitleRight.setImageDrawable(drawRight);
            ivTitleRight.setVisibility(VISIBLE);
        }
        return this;
    }

    private Drawable mDrawRight;

    public CommonTitleBar setImgRight(int resource) {
        try {
            mDrawRight = mContext.getResources().getDrawable(resource);
        } catch (Resources.NotFoundException e) {
            mDrawRight = null;
            e.printStackTrace();
        }
        ivTitleRight.setImageDrawable(mDrawRight);
        ivTitleRight.setVisibility(VISIBLE);
        return this;
    }

    /**
     * 设置中间标题的drawableBottom
     *
     * @param resource
     * @return
     */
    public CommonTitleBar setDrawableBottomCenter(int resource) {
        Drawable drawableBottom = mContext.getResources().getDrawable(resource);
        tvTitleCenter.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawableBottom);
        return this;
    }

    /**
     * 显示左边图片,隐藏文字
     *
     * @return
     */
    public CommonTitleBar showImgLeft() {
        flTitleLeft.setVisibility(VISIBLE);
        ivTitleLeft.setVisibility(VISIBLE);
        tvTitleLeft.setVisibility(GONE);
        return this;
    }

    /**
     * 显示中间图片,隐藏文字
     *
     * @return
     */
    public CommonTitleBar showImgCenter() {
        flTitleCenter.setVisibility(VISIBLE);
        ivTitleCenter.setVisibility(VISIBLE);
        tvTitleCenter.setVisibility(GONE);
        return this;
    }

    /**
     * 显示右边图片，隐藏文字
     *
     * @return
     */
    public CommonTitleBar showImgRight() {
        flTitleRight.setVisibility(VISIBLE);
        ivTitleRight.setVisibility(VISIBLE);
        tvTitleRight.setVisibility(GONE);
        return this;
    }

    /**
     * 显示左边文字，隐藏图片
     *
     * @return
     */
    public CommonTitleBar showTvLeft() {
        flTitleLeft.setVisibility(VISIBLE);
        tvTitleLeft.setVisibility(VISIBLE);
        ivTitleLeft.setVisibility(GONE);
        return this;
    }

    /**
     * 显示中间文字，隐藏图片
     *
     * @return
     */
    public CommonTitleBar showTvCenter() {
        flTitleCenter.setVisibility(VISIBLE);
        tvTitleCenter.setVisibility(VISIBLE);
        ivTitleCenter.setVisibility(GONE);
        return this;
    }

    /**
     * 显示右边文字，隐藏图片
     *
     * @return
     */
    public CommonTitleBar showTvRight() {
        flTitleRight.setVisibility(VISIBLE);
        tvTitleRight.setVisibility(VISIBLE);
        ivTitleRight.setVisibility(GONE);
        return this;
    }

    /**
     * 获取左边的图片
     *
     * @return
     */
    public ImageView getImgLeft() {
        return ivTitleLeft;
    }

    /**
     * 获取中间的图片
     *
     * @return
     */
    public ImageView getImgCenter() {
        return ivTitleCenter;
    }

    /**
     * 获取右边的图片
     *
     * @return
     */
    public ImageView getImgRight() {
        return ivTitleRight;
    }

    /**
     * 隐藏左边视图
     *
     * @return
     */
    public CommonTitleBar hideLeftView() {
        flTitleLeft.setVisibility(View.INVISIBLE);
        return this;
    }

    /**
     * 隐藏中间视图
     *
     * @return
     */
    public CommonTitleBar hideCenterView() {
        flTitleCenter.setVisibility(View.INVISIBLE);
        return this;
    }

    /**
     * 隐藏右边视图
     *
     * @return
     */
    public CommonTitleBar hideRightView() {
        flTitleRight.setVisibility(View.INVISIBLE);
        return this;
    }

    /**
     * 隐藏左边视图的图标
     *
     * @return
     */
    public CommonTitleBar hideLeftImg() {
        ivTitleLeft.setVisibility(GONE);
        return this;
    }

    /**
     * 隐藏中间视图的图标
     *
     * @return
     */
    public CommonTitleBar hideCenterImg() {
        ivTitleCenter.setVisibility(GONE);
        return this;
    }

    /**
     * 隐藏右边视图的图标
     *
     * @return
     */
    public CommonTitleBar hideRightImg() {
        ivTitleRight.setVisibility(GONE);
        return this;
    }

    /**
     * 设置标题左边文字
     *
     * @param textLeft
     * @return
     */
    public CommonTitleBar setTextLeft(String textLeft) {
        tvTitleLeft.setText(textLeft);
        return this;
    }

    /**
     * 设置标题中间文字
     *
     * @param textCenter
     * @return
     */
    public CommonTitleBar setTextCenter(String textCenter) {
        tvTitleCenter.setText(textCenter);
        return this;
    }

    /**
     * 设置标题右边文字
     *
     * @param textRight
     * @return
     */
    public CommonTitleBar setTextRight(String textRight) {
        tvTitleRight.setText(textRight);
        return this;
    }

    /**
     * 设置左边文字颜色
     *
     * @param colorLeft
     * @return
     */
    public CommonTitleBar setTextColorLeft(int colorLeft) {
        tvTitleLeft.setTextColor(colorLeft);
        return this;
    }

    /**
     * 设置中间文字颜色
     *
     * @param colorCenter
     * @return
     */
    public CommonTitleBar setTextColorCenter(int colorCenter) {
        tvTitleCenter.setTextColor(colorCenter);
        return this;
    }

    /**
     * 设置右边文字颜色
     *
     * @param colorRight
     * @return
     */
    public CommonTitleBar setTextColorRight(int colorRight) {
        tvTitleRight.setTextColor(colorRight);
        return this;
    }

    /**
     * 设置左边文字大小
     *
     * @param sizeCenter
     * @return
     */
    public CommonTitleBar setTextSizeLeft(int sizeCenter) {
        tvTitleLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizeCenter);
        return this;
    }

    /**
     * 设置中间文字大小
     *
     * @param sizeCenter
     * @return
     */
    public CommonTitleBar setTextSizeCenter(int sizeCenter) {
        tvTitleCenter.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizeCenter);
        return this;
    }

    /**
     * 设置右边文字大小
     *
     * @param sizeCenter
     * @return
     */
    public CommonTitleBar setTextSizeRight(int sizeCenter) {
        tvTitleRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizeCenter);
        return this;
    }

    /**
     * 获取searchView并显示
     *
     * @return
     */
    public CommonSearchView getSearchView() {
        searchView.setVisibility(VISIBLE);
        return searchView;
    }
}