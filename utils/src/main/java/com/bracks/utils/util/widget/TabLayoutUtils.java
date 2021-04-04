package com.bracks.utils.util.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

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
                e1.printStackTrace();
                return;
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

    public static void setTabLayoutIndicator(final Context context, final TabLayout tabLayout) {
        setTabLayoutIndicator(tabLayout, ConvertUtils.dp2px(6), ConvertUtils.dp2px(6));
    }

    public static void setTabLayoutIndicator(final TabLayout tabLayout, final int leftDip, final int rightDip) {
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                TabLayoutUtils.setIndicator(tabLayout, leftDip, rightDip);
            }
        });
    }
}
