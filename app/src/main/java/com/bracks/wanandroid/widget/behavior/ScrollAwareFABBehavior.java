package com.bracks.wanandroid.widget.behavior;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * good programmer.
 *
 * @date : 2019-07-14 上午 10:04
 * @author: futia
 * @email : futianyi1994@126.com
 * @description : FAB上滑隐藏下滑显示
 */
public class ScrollAwareFABBehavior extends FloatingActionButton.Behavior {

    public ScrollAwareFABBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull FloatingActionButton child,
                                       @NonNull View directTargetChild,
                                       @NonNull View target,
                                       int axes,
                                       int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                               @NonNull FloatingActionButton child,
                               @NonNull View target,
                               int dxConsumed,
                               int dyConsumed,
                               int dxUnconsumed,
                               int dyUnconsumed,
                               int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        if ((dyConsumed > 0 || dyUnconsumed > 0) && child.getVisibility() == View.VISIBLE) {
            //sdk25以上，CoordinatorLayout的onNestedScroll方法会多出一段判断Gone的代码使得不继续回调onNestedScroll，而child.hide()会调用GONE
            //child.hide();
            child.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                @Override
                public void onHidden(FloatingActionButton fab) {
                    super.onHidden(fab);
                    child.setVisibility(View.INVISIBLE);
                }
            });
            //若直接调用INVISIBLE会没有动画
            //child.setVisibility(View.INVISIBLE);
        } else if ((dyConsumed < 0 || dyUnconsumed < 0) && child.getVisibility() != View.VISIBLE) {
            child.show();
        }
    }
}