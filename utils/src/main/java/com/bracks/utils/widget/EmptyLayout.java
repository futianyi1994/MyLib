package com.bracks.utils.widget;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.bracks.utils.R;


/**
 * good programmer.
 *
 * @date : 2020/2/29 22:13
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class EmptyLayout extends LinearLayout implements
        View.OnClickListener {// , ISkinUIObserver {

    public static final int NETWORK_ERROR = 1;
    public static final int NETWORK_LOADING = 2;
    public static final int NODATA = 3;
    public static final int HIDE_LAYOUT = 4;
    public static final int NODATA_ENABLE_CLICK = 5;
    public static final int NO_LOGIN = 6;

    private ProgressBar animProgress;
    private boolean clickEnable = true;
    private final Context context;
    public ImageView img;
    private OnClickListener listener;
    private OnClickListener imgListener;
    private OnClickListener tvListener;
    private int mErrorState;
    private RelativeLayout mLayout;
    private String strNoDataContent = null;
    private String strNoDataContent_ = null;
    private TextView tv;
    private TextView tv_;
    private boolean isFromHtml;

    public EmptyLayout(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public EmptyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        View view = View.inflate(context, R.layout.common_error_layout, null);
        img = view.findViewById(R.id.img_error_layout);
        tv = view.findViewById(R.id.tv_error_layout);
        tv_ = view.findViewById(R.id.tv_error_layout_);
        mLayout = view.findViewById(R.id.pageerrLayout);
        animProgress = view.findViewById(R.id.animProgress);
        //添加下划线
        //tv_.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        setBackgroundColor(-1);
        addView(view);
        changeErrorLayoutBgMode(context);
    }

    public void changeErrorLayoutBgMode(Context context1) {
        // mLayout.setBackgroundColor(SkinsUtil.getColor(context1,
        // "bgcolor01"));
        // tv.setTextColor(SkinsUtil.getColor(context1, "textcolor05"));
    }

    public void dismiss() {
        mErrorState = HIDE_LAYOUT;
        setVisibility(View.GONE);
    }

    public int getErrorState() {
        return mErrorState;
    }

    public boolean isLoadError() {
        return mErrorState == NETWORK_ERROR;
    }

    public boolean isLoading() {
        return mErrorState == NETWORK_LOADING;
    }

    @Override
    public void onClick(View v) {
        if (clickEnable) {
            // setErrorType(NETWORK_LOADING);
            int id = v.getId();
            if (id == R.id.pageerrLayout) {
                listener.onClick(v);
            } else if (id == R.id.img_error_layout) {
                imgListener.onClick(v);
            } else if (id == R.id.tv_error_layout_) {
                tvListener.onClick(v);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // MyApplication.getInstance().getAtSkinObserable().registered(this);
        onSkinChanged();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // MyApplication.getInstance().getAtSkinObserable().unregistered(this);
    }

    public void onSkinChanged() {
        // mLayout.setBackgroundColor(SkinsUtil
        // .getColor(getContext(), "bgcolor01"));
        // tv.setTextColor(SkinsUtil.getColor(getContext(), "textcolor05"));
    }

    public void setDayNight(boolean flag) {
    }

    public void setErrorMessage(String msg) {
        tv.setText(msg);
    }

    public void setErrorMessage_(String msg) {
        tv_.setText(msg);
    }

    /**
     * 新添设置背景
     *
     * @author 火蚁 2015-1-27 下午2:14:00
     */
    public void setErrorImag(int imgResource) {
        try {
            img.setImageResource(imgResource);
        } catch (Exception e) {
        }
    }

    public void setErrorType(int i) {
        setVisibility(View.VISIBLE);
        switch (i) {
            case NETWORK_ERROR:
                mErrorState = NETWORK_ERROR;
                // img.setBackgroundDrawable(SkinsUtil.getDrawable(context,"common_empty_failed"));
                if (NetworkUtils.isConnected()) {
                    tv.setText("network error click to refresh");
                    img.setBackgroundResource(R.drawable.common_empty_failed);
                } else {
                    tv.setText("no internet click to refresh");
                    img.setBackgroundResource(R.drawable.common_empty_network);
                }
                img.setVisibility(View.VISIBLE);
                animProgress.setVisibility(View.GONE);
                clickEnable = true;
                break;
            case NETWORK_LOADING:
                mErrorState = NETWORK_LOADING;
                // animProgress.setBackgroundDrawable(SkinsUtil.getDrawable(context,"loadingpage_bg"));
                animProgress.setVisibility(View.VISIBLE);
                img.setVisibility(View.GONE);
                tv.setText(R.string.loading);
                clickEnable = false;
                break;
            case NODATA:
                mErrorState = NODATA;
                // img.setBackgroundDrawable(SkinsUtil.getDrawable(context,"page_icon_empty"));
                img.setBackgroundResource(R.drawable.common_empty_failed);
                img.setVisibility(View.VISIBLE);
                animProgress.setVisibility(View.GONE);
                setTvNoDataContent();
                clickEnable = false;
                break;
            case HIDE_LAYOUT:
                setVisibility(View.GONE);
                break;
            case NODATA_ENABLE_CLICK:
                mErrorState = NODATA_ENABLE_CLICK;
                img.setBackgroundResource(R.drawable.common_empty_failed);
                // img.setBackgroundDrawable(SkinsUtil.getDrawable(context,"page_icon_empty"));
                img.setVisibility(View.VISIBLE);
                tv_.setVisibility(View.VISIBLE);
                animProgress.setVisibility(View.GONE);
                setTvNoDataContent();
                clickEnable = true;
                break;
            default:
                break;
        }
    }

    public void setNoDataContent(String noDataContent) {
        setNoDataContent(noDataContent, "", false);
    }

    public void setNoDataContent(String noDataContent, String noDataContent_) {
        setNoDataContent(noDataContent, noDataContent_, false);
    }

    public void setNoDataContent(String noDataContent, String noDataContent_, boolean isFromHtml) {
        strNoDataContent = noDataContent;
        strNoDataContent_ = noDataContent_;
        this.isFromHtml = isFromHtml;
    }

    public void setOnLayoutClickListener(OnClickListener listener) {
        this.listener = listener;
        mLayout.setOnClickListener(this);
    }

    public void setOnImgClickListener(OnClickListener imgListener) {
        this.imgListener = imgListener;
        img.setOnClickListener(this);
    }

    public void setOnTvClickListener(OnClickListener tvListener) {
        this.tvListener = tvListener;
        tv_.setOnClickListener(this);
    }

    private void setTvNoDataContent() {
        if (!TextUtils.isEmpty(strNoDataContent)) {
            if (isFromHtml) {
                tv.setText(Html.fromHtml(strNoDataContent));
            } else {
                tv.setText(strNoDataContent);
            }
        } else {
            tv.setText("no data !");
        }
        if (!TextUtils.isEmpty(strNoDataContent_)) {
            if (isFromHtml) {
                tv_.setText(Html.fromHtml(strNoDataContent_));
            } else {
                tv_.setText(strNoDataContent_);
            }
        } else {
            tv_.setText("");
        }
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == View.GONE) {
            mErrorState = HIDE_LAYOUT;
        }
        super.setVisibility(visibility);
    }
}
