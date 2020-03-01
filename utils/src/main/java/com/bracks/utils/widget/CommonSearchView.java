package com.bracks.utils.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bracks.utils.R;


/**
 * Good programmer.
 * Created by futia on 2017-09-25 下午 02:59.
 * Email:futianyi1994@126.com
 * Description:搜索框封装
 */
public class CommonSearchView extends SearchView {
    final float density = getResources().getDisplayMetrics().density;

    public CommonSearchView(Context context) {
        this(context, null);
    }

    public CommonSearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //true value will collapse the SearchView to an icon, while a false will expand it.左侧无放大镜 右侧有叉叉 可以关闭搜索框
        //setIconified(false);
        //The default value is true，设置为false直接展开显示 左侧有放大镜(在搜索框外) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
        setIconifiedByDefault(false);
        //内部调用了setIconified(false); 直接展开显示 左侧无放大镜 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
        //onActionViewExpanded();
        //设置最大宽度
        //setMaxWidth(500);
        //设置是否显示搜索框展开时的提交按钮
        setSubmitButtonEnabled(false);
        //清除焦点
        //clearFocus();
        setFocusable(false);
        //设置输入框提示语
        setQueryHint(context.getString(R.string.common_search));
        //修改搜索字体
        setSearchAutoComplete(Color.BLACK, Color.GRAY, 14, 0, 0, 0, 0)
                .setEditFrame(0, 0, 0, 0)
                .setSearchPlate(0, 0, 0, 0)
                //修改搜索图标
                .setHintIcon(R.drawable.common_search, 30, 0, 0, 0)
                .setCloseIcon(R.drawable.common_edittext_delete);

        //监听输入框变化和执行搜索操作后的回掉
        setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                setFocusable(false);
                clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        setOnQueryTextFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });
    }

    public CommonSearchView setHintIcon(@DrawableRes int resId, int left, int top, int right, int bottom) {
        ImageView imageView = findViewById(R.id.search_mag_icon);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        imageView.setImageResource(resId);
        imageView.setVisibility(VISIBLE);
        imageView.setPadding(0, 0, 0, 0);
        params.leftMargin = left;
        params.topMargin = top;
        params.rightMargin = right;
        params.bottomMargin = bottom;
        imageView.setLayoutParams(params);
        return this;
    }

    public CommonSearchView setCloseIcon(@DrawableRes int resId) {
        ImageView closeImg = findViewById(R.id.search_close_btn);
        LinearLayout.LayoutParams paramsImg = (LinearLayout.LayoutParams) closeImg.getLayoutParams();
        paramsImg.topMargin = (int) (density);
        closeImg.setImageResource(resId);
        closeImg.setLayoutParams(paramsImg);
        closeImg.setPadding(0, 0, 10, 0);
        return this;
    }

    public CommonSearchView setBackground(@ColorInt int resId) {
        LinearLayout rootView = findViewById(R.id.search_bar);
        //rootView.setBackgroundResource(resId);
        rootView.setBackgroundColor(resId);
        rootView.setClickable(true);
        return this;
    }

    public CommonSearchView setSearchPlate(int left, int top, int right, int bottom) {
        LinearLayout editLayout = findViewById(R.id.search_plate);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) editLayout.getLayoutParams();
        params.leftMargin = left;
        params.topMargin = top;
        params.rightMargin = right;
        params.bottomMargin = bottom;
        editLayout.setLayoutParams(params);
        return this;
    }

    public CommonSearchView setEditFrame(int left, int top, int right, int bottom) {
        @SuppressLint("WrongViewCast") LinearLayout tipLayout = findViewById(R.id.search_edit_frame);
        LinearLayout.LayoutParams tipParams = (LinearLayout.LayoutParams) tipLayout.getLayoutParams();
        tipParams.leftMargin = left;
        tipParams.topMargin = top;
        tipParams.rightMargin = right;
        tipParams.bottomMargin = bottom;
        tipLayout.setLayoutParams(tipParams);
        return this;
    }

    public CommonSearchView setSearchAutoComplete(@ColorInt int textColor, @ColorInt int hintTextColor, float size, int left, int top, int right, int bottom) {
        SearchView.SearchAutoComplete mSearchSrcTextView = findViewById(R.id.search_src_text);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mSearchSrcTextView.getLayoutParams();
        params.leftMargin = left;
        params.topMargin = top;
        params.rightMargin = right;
        params.bottomMargin = bottom;
        mSearchSrcTextView.setLayoutParams(params);
        mSearchSrcTextView.setPadding(0, 0, 0, 0);
        mSearchSrcTextView.setTextColor(textColor);
        mSearchSrcTextView.setTextSize(size);
        mSearchSrcTextView.setHintTextColor(hintTextColor);
        return this;
    }
}
