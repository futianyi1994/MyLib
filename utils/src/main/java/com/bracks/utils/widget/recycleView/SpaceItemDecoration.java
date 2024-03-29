package com.bracks.utils.widget.recycleView;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.IntDef;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * good programmer.
 *
 * @data : 2018-01-11 下午 03:28
 * @author: futia
 * @email : futianyi1994@126.com
 * @description : RecyclerView 设置间距
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    public static final int LINEARLAYOUT = 0;
    public static final int GRIDLAYOUT = 1;
    public static final int STAGGEREDGRIDLAYOUT = 2;
    /**
     * 头布局个数
     */
    private final int headItemCount;
    /**
     * 是否包含边距
     */
    private final boolean includeEdge;
    @LayoutManager
    private final int layoutManager;
    private int leftRight = -1;
    private int topBottom = -1;
    /**
     * 烈数
     */
    private int spanCount;

    /**
     * LinearLayoutManager or GridLayoutManager or StaggeredGridLayoutManager spacing
     *
     * @param leftRight
     * @param topBottom
     * @param headItemCount
     * @param includeEdge
     * @param layoutManager
     */
    public SpaceItemDecoration(int leftRight, int topBottom, int headItemCount, boolean includeEdge, @LayoutManager int layoutManager) {
        this.leftRight = leftRight;
        this.topBottom = topBottom;
        this.headItemCount = headItemCount;
        this.includeEdge = includeEdge;
        this.layoutManager = layoutManager;
    }

    /**
     * LinearLayoutManager or GridLayoutManager or StaggeredGridLayoutManager spacing
     *
     * @param leftRight
     * @param topBottom
     * @param headItemCount
     * @param layoutManager
     */
    public SpaceItemDecoration(int leftRight, int topBottom, int headItemCount, @LayoutManager int layoutManager) {
        this(leftRight, topBottom, headItemCount, true, layoutManager);
    }

    /**
     * LinearLayoutManager or GridLayoutManager or StaggeredGridLayoutManager spacing
     *
     * @param space
     * @param includeEdge
     * @param layoutManager
     */
    public SpaceItemDecoration(int space, boolean includeEdge, @LayoutManager int layoutManager) {
        this(space, space, 0, includeEdge, layoutManager);
    }

    /**
     * LinearLayoutManager or GridLayoutManager or StaggeredGridLayoutManager spacing
     *
     * @param space
     * @param headItemCount
     * @param layoutManager
     */
    public SpaceItemDecoration(int space, int headItemCount, @LayoutManager int layoutManager) {
        this(space, space, headItemCount, true, layoutManager);
    }

    /**
     * LinearLayoutManager or GridLayoutManager or StaggeredGridLayoutManager spacing
     *
     * @param space
     * @param layoutManager
     */
    public SpaceItemDecoration(int space, @LayoutManager int layoutManager) {
        this(space, space, 0, true, layoutManager);
    }

    /**
     * LinearLayoutManager spacing
     *
     * @param space
     */
    public SpaceItemDecoration(int space) {
        this(space, space, 0, true, LINEARLAYOUT);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        switch (layoutManager) {
            case LINEARLAYOUT:
                setLinearLayoutSpaceItemDecoration(outRect, view, parent, state);
                break;
            case GRIDLAYOUT:
                GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
                //列数
                spanCount = gridLayoutManager.getSpanCount();
                setNGridLayoutSpaceItemDecoration(outRect, view, parent, state);
                break;
            case STAGGEREDGRIDLAYOUT:
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) parent.getLayoutManager();
                //列数
                spanCount = staggeredGridLayoutManager.getSpanCount();
                setNGridLayoutSpaceItemDecoration(outRect, view, parent, state);
                break;
            default:
                break;
        }
    }

    /**
     * LinearLayoutManager spacing
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    private void setLinearLayoutSpaceItemDecoration(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = topBottom;
        int position = parent.getChildAdapterPosition(view) - headItemCount;
        if (headItemCount != 0 && position == -headItemCount) {
            return;
        }
        outRect.left = leftRight;
        outRect.right = leftRight;
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = topBottom;
        } else {
            outRect.top = 0;
        }
    }

    /**
     * GridLayoutManager or StaggeredGridLayoutManager spacing
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    private void setNGridLayoutSpaceItemDecoration(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view) - headItemCount;
        if (headItemCount != 0 && position == -headItemCount) {
            return;
        }
        int column = position % spanCount;
        if (includeEdge) {
            outRect.left = leftRight - column * leftRight / spanCount;
            outRect.right = (column + 1) * leftRight / spanCount;
            if (position < spanCount) {
                outRect.top = topBottom;
            }
            outRect.bottom = topBottom;
        } else {
            outRect.left = column * leftRight / spanCount;
            outRect.right = leftRight - (column + 1) * leftRight / spanCount;
            if (position >= spanCount) {
                outRect.top = topBottom;
            }
        }
    }

    /**
     * GridLayoutManager设置间距（此方法最左边和最右边间距为设置的一半）
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    private void setGridLayoutSpaceItemDecoration(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        //判断总的数量是否可以整除
        int totalCount = layoutManager.getItemCount();
        int surplusCount = totalCount % layoutManager.getSpanCount();
        int childPosition = parent.getChildAdapterPosition(view);
        //竖直方向的
        if (layoutManager.getOrientation() == RecyclerView.VERTICAL) {
            if (surplusCount == 0 && childPosition > totalCount - layoutManager.getSpanCount() - 1) {
                //后面几项需要bottom
                outRect.bottom = topBottom;
            } else if (surplusCount != 0 && childPosition > totalCount - surplusCount - 1) {
                outRect.bottom = topBottom;
            }
            //被整除的需要右边
            if ((childPosition + 1 - headItemCount) % layoutManager.getSpanCount() == 0) {
                //加了右边后最后一列的图就非宽度少一个右边距
                //outRect.right = leftRight;
            }
            outRect.top = topBottom;
            outRect.left = leftRight / 2;
            outRect.right = leftRight / 2;
        } else {
            if (surplusCount == 0 && childPosition > totalCount - layoutManager.getSpanCount() - 1) {
                //后面几项需要右边
                outRect.right = leftRight;
            } else if (surplusCount != 0 && childPosition > totalCount - surplusCount - 1) {
                outRect.right = leftRight;
            }
            //被整除的需要下边
            if ((childPosition + 1) % layoutManager.getSpanCount() == 0) {
                outRect.bottom = topBottom;
            }
            outRect.top = topBottom;
            outRect.left = leftRight;
        }
    }

    //限定为LINEARLAYOUT,GRIDLAYOUT,STAGGEREDGRIDLAYOUT
    @IntDef({LINEARLAYOUT, GRIDLAYOUT, STAGGEREDGRIDLAYOUT})
    //表示注解所存活的时间,在运行时,而不会存在. class 文件.
    @Retention(RetentionPolicy.SOURCE)
    public @interface LayoutManager {
        int type() default LINEARLAYOUT;
    }
}
