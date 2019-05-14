package com.bracks.futia.mylib.base.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * good programmer.
 *
 * @date : 2017/9/4 11:32
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public abstract class MBaseAdapter<T> extends BaseAdapter {
    private List<T> datas;
    private LayoutInflater inflater;
    private Context context;
    private int selectPosition = 0;
    /**
     * 存放已被选中的CheckBox
     */
    @SuppressLint("UseSparseArrays")
    private Map<Integer, Boolean> map = new HashMap<>();

    public Context getContext() {
        return context;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public Map<Integer, Boolean> getMap() {
        return map;
    }

    public MBaseAdapter(Context context) {
        this.datas = new ArrayList<T>();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public T getItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 添加一个集合
     *
     * @param dd
     */
    public void addAll(List<T> dd) {
        datas.addAll(dd);
        notifyDataSetChanged();
    }

    /**
     * 清空数据源
     */
    public void clear() {
        datas.clear();
        notifyDataSetChanged();
    }

    /**
     * 获得数据源
     */
    public List<T> getData() {
        return datas;
    }


    /**
     * 点击选择的位置
     *
     * @param position
     * @param reset    是否保存状态信息
     */
    public void setSelected(int position, boolean reset) {
        selectPosition = position;
        if (reset) {
            notifyDataSetChanged();
        } else {
            notifyDataSetInvalidated();
        }
    }

    /**
     * position位置的CheckBox选中,用map存放选中位置的选中状态防止复用导致的错乱
     *
     * @param checkBox
     * @param position
     */
    public void setCheckedForMap(CheckBox checkBox, int position) {
        if (map == null || !map.containsKey(position)) {
            checkBox.setChecked(false);
        }
        if (map != null && map.containsKey(position)) {
            checkBox.setChecked(true);
        }
    }

    public void putCheckedToMap(int position) {
        map.put(position, true);
    }

    public void removeCheckedFromMap(int position) {
        map.remove(position);
    }

}
