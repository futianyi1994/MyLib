package com.bracks.mylib.base.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * good programmer.
 *
 * @date : 2019-05-13 下午 03:52
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public abstract class MBaseRecyclerViewAdapter<T> extends RecyclerView.Adapter {
    /**
     * 数据源
     */
    private final List<T> data = new ArrayList<>();

    /**
     * 获取Item布局资源文件ID
     *
     * @param viewType Item布局类型
     * @return 资源文件ID
     */
    @LayoutRes
    public abstract int getItemLayoutResId(int viewType);

    /**
     * 获得ViewHolder
     *
     * @param itemView itemView
     * @return
     */
    public abstract RecyclerView.ViewHolder getViewHolder(@NonNull View itemView);

    public final T getItem(int position) {
        return data == null || data.isEmpty() ? null : data.get(position);
    }

    public List<T> getData() {
        return data;
    }

    public final void setData(@NonNull List<T> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final int layoutResId = getItemLayoutResId(viewType);
        if (layoutResId < 0) {
            throw new IllegalArgumentException("Layout file does not exist!");
        }
        final View inflateView = LayoutInflater.from(parent.getContext()).inflate(getItemLayoutResId(viewType), parent, false);
        final RecyclerView.ViewHolder viewHolder = getViewHolder(inflateView);
        if (viewHolder == null) {
            throw new IllegalArgumentException("ViewHolder is null!");
        }
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return data == null || data.isEmpty() ? 0 : data.size();
    }
}
