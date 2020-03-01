package com.bracks.utils.util.widget;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * good programmer.
 *
 * @data : 2017-11-24 下午 03:50
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :listview工具类 （在设置适配器之后调用）
 */
public class ListViewUtils {
    private ListView listView;
    private ExpandableListView exListView;

    private ListViewUtils(ListView listView) {
        if (listView instanceof ExpandableListView) {
            this.exListView = (ExpandableListView) listView;
        } else if (listView != null) {
            this.listView = listView;
        }
    }

    /**
     * 设置groupitem是否可点击
     *
     * @param groupClickable true:可以点击
     * @return ListViewUtils
     */
    public ListViewUtils groupClickable(final boolean groupClickable) {
        exListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return !groupClickable;
            }
        });
        return this;
    }

    /**
     * 展开全部
     *
     * @param size 展开全部的个数
     * @return ListViewUtils
     */
    public ListViewUtils expandGroup(int size) {
        for (int i = 0; i < size; i++) {
            exListView.expandGroup(i);
        }
        return this;
    }

    /**
     * 设置ListView高度
     *
     * @return ListViewUtils
     */
    public ListViewUtils setMeasureHeight() {
        return setMeasureHeight(0, 0);
    }

    /**
     * 设置ListView高度
     *
     * @param itemCount item数量
     * @return ListViewUtils
     */
    public ListViewUtils setMeasureHeight(int itemCount) {
        return setMeasureHeight(0, itemCount);
    }

    /**
     * 设置ListView高度
     *
     * @param listViewWidth listView的宽度
     * @param itemCount     item数量
     * @return ListViewUtils
     */
    public ListViewUtils setMeasureHeight(int listViewWidth, int itemCount) {
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = measureHeight(listViewWidth, itemCount);
        ((ViewGroup.MarginLayoutParams) params).setMargins(0, 0, 0, 0);
        listView.setLayoutParams(params);
        return this;
    }

    /**
     * 测量listView的高度
     *
     * @return listView的高度
     */
    public int measureHeight() {
        return measureHeight(0, 0);
    }

    /**
     * 测量listView的高度
     *
     * @param itemCount item数量
     * @return ListViewUtils
     */
    public int measureHeight(int itemCount) {
        return measureHeight(0, itemCount);
    }

    /**
     * 测量listView的高度
     * 需要知道listview宽度以精确测量高度
     *
     * @param listViewWidth listView的宽度
     * @param itemCount     item数量
     * @return ListViewUtils
     */
    public int measureHeight(int listViewWidth, int itemCount) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return 0;
        }
        int totalHeight = 0;
        int mItemCount = itemCount == 0 ? listAdapter.getCount() : itemCount;
        for (int i = 0; i < mItemCount; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(listViewWidth == 0 ? 0 : View.MeasureSpec.makeMeasureSpec(listViewWidth, View.MeasureSpec.AT_MOST), 0);
            int itemHeight = listItem.getMeasuredHeight();
            totalHeight += itemHeight;
        }
        //加上底部分割线的高度
        int dividerHeight = listView.getDividerHeight();
        return totalHeight + (dividerHeight == 0 ? 1 : dividerHeight * mItemCount - 1);
    }

    /**
     * 获取listView
     *
     * @param listView
     * @return ListViewUtils
     */
    public static ListViewUtils with(ListView listView) {
        return new ListViewUtils(listView);
    }
}
