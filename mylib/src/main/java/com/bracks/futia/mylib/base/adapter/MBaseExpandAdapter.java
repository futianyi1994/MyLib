package com.bracks.futia.mylib.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * good programmer.
 *
 * @date : 2017-11-17 下午 04:02
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public abstract class MBaseExpandAdapter<G, C> extends BaseExpandableListAdapter {
    public static final int BODY_LAYOUT = 0;
    public static final int FOOT_LAYOUT = 1;
    private List<G> groupDatas = null;
    private List<C> childDatas = null;
    private LayoutInflater inflater;
    private Context context;

    public Context getContext() {
        return context;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    public MBaseExpandAdapter(Context context) {
        this.groupDatas = new ArrayList<G>();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return groupDatas.size();
    }

    @Override
    public G getGroup(int i) {
        return groupDatas.get(i);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public int getChildrenCount(int i) {
        return childDatas == null ? 0 : childDatas.size();
    }

    @Override
    public C getChild(int i, int i1) {
        return childDatas == null ? null : getChildData().get(i1);
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    /**
     * 添加一个集合
     *
     * @param dd
     */
    public void addAll(List<G> dd) {
        groupDatas.clear();
        groupDatas.addAll(dd);
        notifyDataSetChanged();
    }

    /**
     * 清空数据源
     */
    public void clear() {
        groupDatas.clear();
        notifyDataSetChanged();
    }

    /**
     * 添加子集合
     *
     * @param childData
     */
    public void addChildData(List<C> childData) {
        this.childDatas = childData;
    }

    /**
     * 得到子集合
     *
     * @return
     */
    public List<C> getChildData() {
        return childDatas;
    }

    /**
     * 获父集合
     */
    public List<G> getGroupData() {
        return groupDatas;
    }
}
