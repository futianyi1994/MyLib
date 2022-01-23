package com.bracks.utils.widget.recycleView;

import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * good programmer.
 *
 * @date : 2018-09-17 11:08
 * @author: futia
 * @email : futianyi1994@126.com
 * @description : https://blog.csdn.net/w690333243/article/details/77175422
 */
public class RecycleViewUtils {
    private static final int[] headerHeight = new int[2];
    /**
     * 计算RecyclerView滑动的距离(是适用于除头部外其他item高度相同的情况)这个里为两个头部的情况
     *
     * @return 滑动的距离
     */
    private static int scrollYheight;

    public static int getScollYHeight(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        //获取到第一个可见的position,其添加的头部不算其position当中
        int position = layoutManager.findFirstVisibleItemPosition();
        //通过position获取其管理器中的视图
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        //获取自身的高度
        int itemHeight = firstVisiableChildView.getHeight();

        if (position == 0) {
            headerHeight[0] = layoutManager.findViewByPosition(0).getHeight();
            scrollYheight = itemHeight * position - firstVisiableChildView.getTop();
        } else if (position == 1) {
            headerHeight[1] = layoutManager.findViewByPosition(1).getHeight();
            scrollYheight = headerHeight[0] + itemHeight * position - firstVisiableChildView.getTop();
        } else {
            scrollYheight = headerHeight[0] + headerHeight[1] + itemHeight * position - firstVisiableChildView.getTop();

        }
        Log.d("scrollYheight", "scrollYheight: " + scrollYheight);
        return scrollYheight;
    }

    /**
     * 判断recycleView是否滑动到底部
     * RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部
     *
     * @param recyclerView
     * @return
     */
    public static boolean isSlideToBottom(RecyclerView recyclerView) {
        return !recyclerView.canScrollVertically(1);
    }

    /**
     * 判断recycleView是否滑动到顶部
     * RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
     *
     * @param recyclerView
     * @return
     */
    public static boolean isSlideToTop(RecyclerView recyclerView) {
        return !recyclerView.canScrollVertically(-1);
    }
}
