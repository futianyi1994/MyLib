package com.bracks.wanandroid.utils;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.bracks.wanandroid.R;

/**
 * good programmer.
 *
 * @date : 2019-06-11 上午 09:56
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class ToolbarUtils {
    /**
     * 设置自带toolbar标题居中
     *
     * @param toolbar
     * @param title
     */
    public static void setTitleCenter(Toolbar toolbar, String title) {
        CharSequence originalTitle = toolbar.getTitle();
        toolbar.setTitle(title);
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                if (title.contentEquals(textView.getText())) {
                    textView.setGravity(Gravity.CENTER);
                    Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.MATCH_PARENT);
                    params.gravity = Gravity.CENTER;
                    textView.setLayoutParams(params);
                }
            }
            toolbar.setTitle(originalTitle);
        }
    }

    /**
     * 添加剧中的title
     *
     * @param toolbar
     * @param title
     */
    public static void addTitleCenter(Toolbar toolbar, String title) {
        TextView titleText = new TextView(toolbar.getContext());
        titleText.setTextColor(ContextCompat.getColor(toolbar.getContext(), R.color.white));
        titleText.setText(title);
        titleText.setTextSize(18);
        titleText.setGravity(Gravity.CENTER);
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        titleText.setLayoutParams(layoutParams);
        toolbar.addView(titleText);
    }
}
