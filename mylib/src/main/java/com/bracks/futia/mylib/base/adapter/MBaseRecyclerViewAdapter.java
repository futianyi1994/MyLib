package com.bracks.futia.mylib.base.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bracks.futia.mylib.utils.log.TLog;

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
public abstract class MBaseRecyclerViewAdapter<ENTITY> extends RecyclerView.Adapter {

    private static final String TAG = "RecyclerViewAdapter";
    /**
     * 数据源
     */
    private List<ENTITY> data = new ArrayList<>();

    /**
     * 获取Item布局资源文件ID
     *
     * @param viewType Item布局类型
     * @return 资源文件ID
     */
    @LayoutRes
    public abstract int getItemLayoutResId(int viewType);

    public abstract RecyclerView.ViewHolder getViewHolder(@NonNull View itemView);

    public final void setData(@NonNull List<ENTITY> data) {
        this.data.clear();

        if (data != null) {
            this.data.addAll(data);
        } else {
            TLog.e(TAG, "data is null or empty");
        }
        notifyDataSetChanged();
    }

    public final ENTITY getItem(int position) {
        return data == null || data.isEmpty() ? null : data.get(position);
    }

    public List<ENTITY> getData() {
        return data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
