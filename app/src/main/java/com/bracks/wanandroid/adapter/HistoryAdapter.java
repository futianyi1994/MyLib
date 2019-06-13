package com.bracks.wanandroid.adapter;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bracks.mylib.base.adapter.BaseRecyclerViewAdapter;
import com.bracks.mylib.rx.RxAppActivity;
import com.bracks.mylib.rx.RxDefaultObserver;
import com.bracks.mylib.rx.RxObservHelper;
import com.bracks.mylib.rx.RxSchedulersCompat;
import com.bracks.wanandroid.R;
import com.bracks.wanandroid.activity.ArticleUi;
import com.bracks.wanandroid.model.bean.History;
import com.bracks.wanandroid.model.bean.Result;
import com.bracks.wanandroid.net.ApiService;
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
public class HistoryAdapter extends BaseRecyclerViewAdapter<History.DataBean.DatasBean> {

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public HistoryAdapter(FragmentActivity activity) {
        super(activity);
    }

    @Override
    protected int inflaterItemLayout(int viewType) {
        return R.layout.item_history;
    }

    @Override
    protected void bindData(BaseViewHolder holder, int position, History.DataBean.DatasBean datasBean) {
        holder.setText(R.id.tvName, name);
        holder.setText(R.id.tvLable, String.format("公众号/%s", name));
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
                        ApiService
                                .getService()
                                .collect(datasBean.getId())
                                .compose(RxSchedulersCompat.ioSchedulerObser())
                                .compose(RxObservHelper.applyProgressBar((RxAppActivity) getContext(), true))
                                .subscribe(new RxDefaultObserver<Result>() {
                                    @Override
                                    public void onSuccess(Result response) {
                                        datasBean.setCollect(true);
                                        ivCollect.setBackgroundResource(R.mipmap.collected);
                                    }
                                });
                    } else {
                        ApiService
                                .getService()
                                .cancelCollect(datasBean.getId())
                                .compose(RxSchedulersCompat.ioSchedulerObser())
                                .compose(RxObservHelper.applyProgressBar((RxAppActivity) getContext(), true))
                                .subscribe(new RxDefaultObserver<Result>() {
                                    @Override
                                    public void onSuccess(Result response) {
                                        datasBean.setCollect(false);
                                        ivCollect.setBackgroundResource(R.mipmap.collect);
                                    }
                                });
                    }
                });
    }

    @Override
    protected void onItemClickListener(View itemView, int position, History.DataBean.DatasBean datasBean) {
        ActivityUtils.startActivity(
                new Intent(itemView.getContext(), ArticleUi.class).putExtra(ArticleUi.EXTRA_LINK, datasBean.getLink())
        );
    }
}
