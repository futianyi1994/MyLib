package com.bracks.wanandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.bracks.mylib.base.adapter.MBaseRecyclerViewAdapter;
import com.bracks.wanandroid.R;
import com.bracks.wanandroid.activity.HistoryUi;
import com.bracks.wanandroid.model.bean.PublicList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * good programmer.
 *
 * @date : 2019-01-24 上午 11:21
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class PublicAdapter extends MBaseRecyclerViewAdapter<PublicList.DataBean> {

    @Override
    public int getItemLayoutResId(int viewType) {
        return R.layout.item_public;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(@NonNull View itemView) {
        return new MViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Context context = viewHolder.itemView.getContext();
        TextView tvTitle = ((MViewHolder) viewHolder).tvTitle;
        tvTitle.setText(getItem(i).getName());
        tvTitle.setOnClickListener(v -> {
            ActivityUtils.startActivity(
                    new Intent(context, HistoryUi.class)
                            .putExtra(HistoryUi.EXTRA_ID, getItem(i).getId())
                            .putExtra(HistoryUi.EXTRA_PAGE, 1)
                            .putExtra(HistoryUi.EXTRA_TITLE, getItem(i).getName())
            );
        });

    }

    class MViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;

        MViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}