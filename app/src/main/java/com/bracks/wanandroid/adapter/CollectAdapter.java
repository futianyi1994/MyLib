package com.bracks.wanandroid.adapter;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bracks.mylib.base.adapter.BaseRecyclerViewAdapter;
import com.bracks.wanandroid.R;
import com.bracks.wanandroid.activity.ArticleUi;
import com.bracks.wanandroid.model.bean.Collect;
import com.jakewharton.rxbinding3.view.RxView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import io.reactivex.disposables.Disposable;

/**
 * good programmer.
 *
 * @date : 2019-02-18 下午 06:22
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class CollectAdapter extends BaseRecyclerViewAdapter<Collect.DataBean.DatasBean> {


    public CollectAdapter(FragmentActivity activity) {
        super(activity);
    }

    @Override
    protected int inflaterItemLayout(int viewType) {
        return R.layout.item_chapter;
    }

    @Override
    protected void bindData(BaseViewHolder holder, int position, Collect.DataBean.DatasBean datasBean) {
        holder.setText(R.id.tvName, datasBean.getAuthor());
        holder.setText(R.id.tvLable, String.format("%s/%s", datasBean.getChapterName(),datasBean.getAuthor()));
        holder.setText(R.id.tvTitle, Html.fromHtml(datasBean.getTitle()).toString());
        holder.setText(R.id.tvTime, TimeUtils.millis2String(datasBean.getPublishTime(), new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())));
        ImageView ivCollect = ((ImageView) holder.getView(R.id.ivCollect));
        ivCollect.setBackgroundResource(R.mipmap.collected);

        Disposable disposable = RxView
                .clicks(ivCollect)
                .subscribe(unit -> {
                });
    }

    @Override
    protected void onItemClickListener(View itemView, int position, Collect.DataBean.DatasBean datasBean) {
        ActivityUtils.startActivity(
                new Intent(itemView.getContext(), ArticleUi.class).putExtra(ArticleUi.EXTRA_LINK, datasBean.getLink())
        );
    }
}
