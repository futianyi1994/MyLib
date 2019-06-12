package com.bracks.wanandroid.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bracks.mylib.base.adapter.MBaseRecyclerViewAdapter;
import com.bracks.wanandroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * good programmer.
 *
 * @date : 2019-06-12 下午 11:01
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class MyAdapter extends MBaseRecyclerViewAdapter<String> {

    @Override
    public int getItemLayoutResId(int viewType) {
        return R.layout.item_my;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(@NonNull View itemView) {
        return new MViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Context context = viewHolder.itemView.getContext();
        TextView tvItem = ((MViewHolder) viewHolder).tvItem;
        tvItem.setText(getItem(i));
        tvItem.setOnClickListener(v -> {

        });
    }

    class MViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvItem)
        TextView tvItem;

        MViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
