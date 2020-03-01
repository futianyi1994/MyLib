package com.bracks.wanandroid.adapter;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bracks.mylib.base.adapter.BaseRecyclerViewAdapter;
import com.bracks.mylib.rx.RxAutoDispose;
import com.bracks.mylib.rx.RxDefaultObserver;
import com.bracks.mylib.rx.RxObservHelper;
import com.bracks.wanandroid.R;
import com.bracks.wanandroid.activity.ArticleUi;
import com.bracks.wanandroid.model.CollectM;
import com.bracks.wanandroid.model.bean.Collect;
import com.bracks.wanandroid.model.bean.Result;
import com.jakewharton.rxbinding2.view.RxView;

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
        holder.setText(R.id.tvLable, String.format("%s/%s", datasBean.getChapterName(), datasBean.getAuthor()));
        holder.setText(R.id.tvTitle, Html.fromHtml(datasBean.getTitle()).toString());
        holder.setText(R.id.tvTime, TimeUtils.millis2String(datasBean.getPublishTime(), new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())));
        ImageView ivCollect = ((ImageView) holder.getView(R.id.ivCollect));
        if (datasBean.isCollect()) {
            ivCollect.setBackgroundResource(R.mipmap.collected);
        } else {
            ivCollect.setBackgroundResource(R.mipmap.collect);
        }

        Disposable disposable = RxView
                .clicks(ivCollect)
                .subscribe(unit -> {
                    if (!datasBean.isCollect()) {
                        new CollectM(datasBean.getOriginId())
                                .loadData()
                                .compose(RxObservHelper.applyProgressBar(getContext(), true))
                                .as(RxAutoDispose.bindLifecycle((LifecycleOwner) getContext()))
                                .subscribe(new RxDefaultObserver<Result>() {

                                    @Override
                                    public void onSuccess(Result response) {
                                        datasBean.setCollect(true);
                                        ivCollect.setBackgroundResource(R.mipmap.collected);
                                    }
                                })
                        ;
                    } else {
                        new CollectM(datasBean.getId(), datasBean.getOriginId())
                                .cancelMyCollect()
                                .compose(RxObservHelper.applyProgressBar(getContext(), true))
                                .as(RxAutoDispose.bindLifecycle((LifecycleOwner) getContext()))
                                .subscribe(new RxDefaultObserver<Result>() {
                                    @Override
                                    public void onSuccess(Result response) {
                                        datasBean.setCollect(false);
                                        ivCollect.setBackgroundResource(R.mipmap.collect);
                                    }
                                })
                        ;
                    }
                });
    }

    @Override
    protected void onItemClickListener(View itemView, int position, Collect.DataBean.DatasBean datasBean) {
        ActivityUtils.startActivity(
                new Intent(itemView.getContext(), ArticleUi.class).putExtra(ArticleUi.EXTRA_LINK, datasBean.getLink())
        );
    }
}
