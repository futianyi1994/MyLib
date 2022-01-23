package com.bracks.utils.util.widget;

import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ConvertUtils;
import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Field;

/**
 * good programmer.
 *
 * @date : 2019-04-13 上午 10:47
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class TabLayoutUtils {
    private static final String TAG = "TabLayoutUtils";

    public static void setIndicatorWidth(@NonNull TabLayout tabs, int margin) {
        setIndicatorWidth(tabs, margin, margin);
    }

    /**
     * 设置tabLayout item的宽度
     *
     * @param tabs
     * @param leftMargin
     * @param rightMargin
     */
    public static void setIndicatorWidth(@NonNull TabLayout tabs, int leftMargin, int rightMargin) {
        tabs.post(() -> {
            Class<?> tabLayout = tabs.getClass();
            Field tabStrip = null;
            try {
                tabStrip = tabLayout.getDeclaredField("mTabStrip");
            } catch (NoSuchFieldException e) {
                try {
                    //sdk=28改变了方法名
                    tabStrip = tabLayout.getDeclaredField("slidingTabIndicator");
                } catch (NoSuchFieldException e1) {
                    for (; tabLayout != Object.class; tabLayout = tabLayout.getSuperclass()) {
                        try {
                            tabStrip = tabLayout.getDeclaredField("slidingTabIndicator");
                        } catch (NoSuchFieldException noSuchFieldException) {
                        }
                    }
                    if (tabStrip == null) {
                        Log.e(TAG, "setIndicatorWidth tabStrip is null");
                        return;
                    }
                }
            }
            tabStrip.setAccessible(true);
            try {
                LinearLayout llTab = (LinearLayout) tabStrip.get(tabs);
                for (int i = 0; i < llTab.getChildCount(); i++) {
                    View child = llTab.getChildAt(i);
                    //拿到tabView的mTextView属性
                    Field mTextViewField = null;
                    try {
                        mTextViewField = child.getClass().getDeclaredField("mTextView");
                    } catch (NoSuchFieldException e) {
                        try {
                            mTextViewField = child.getClass().getDeclaredField("textView");
                        } catch (NoSuchFieldException ex) {
                            ex.printStackTrace();
                            return;
                        }
                    }
                    mTextViewField.setAccessible(true);
                    TextView mTextView = (TextView) mTextViewField.get(child);
                    child.setPadding(0, 0, 0, 0);
                    //因为我想要的效果是字多宽线就多宽，所以测量mTextView的宽度
                    int width = mTextView.getWidth();
                    if (width == 0) {
                        mTextView.measure(0, 0);
                        width = mTextView.getMeasuredWidth();
                    }
                    //设置tab左右间距,注意这里不能使用Padding,因为源码中线的宽度是根据tabView的宽度来设置的
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) child.getLayoutParams();
                    params.width = width;
                    params.leftMargin = leftMargin;
                    params.rightMargin = rightMargin;
                    child.setLayoutParams(params);
                    child.invalidate();
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    public static void setTabLayoutIndicator(TabLayout tabLayout) {
        setTabLayoutIndicator(tabLayout, ConvertUtils.dp2px(6), ConvertUtils.dp2px(6));
    }

    public static void setTabLayoutIndicator(TabLayout tabLayout, int leftDip, int rightDip) {
        tabLayout.post(() -> TabLayoutUtils.setIndicator(tabLayout, leftDip, rightDip));
    }

    /**
     * 设置tabLayout 下划线的宽度
     *
     * @param tabs
     * @param leftDip
     * @param rightDip
     */
    private static void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            try {
                //sdk=28改变了方法名
                tabStrip = tabLayout.getDeclaredField("slidingTabIndicator");
            } catch (NoSuchFieldException e1) {
                for (; tabLayout != Object.class; tabLayout = tabLayout.getSuperclass()) {
                    try {
                        tabStrip = tabLayout.getDeclaredField("slidingTabIndicator");
                    } catch (NoSuchFieldException noSuchFieldException) {
                    }
                }
                if (tabStrip == null) {
                    Log.e(TAG, "setIndicator tabStrip is null");
                    return;
                }
            }
        }
        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }
}
