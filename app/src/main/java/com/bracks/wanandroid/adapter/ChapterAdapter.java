package com.bracks.wanandroid.adapter;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.bracks.wanandroid.model.bean.Chapter;
import com.bracks.wanandroid.model.bean.Result;
import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding3.view.RxView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
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
public class ChapterAdapter extends BaseRecyclerViewAdapter<Chapter.DataBean.DatasBean> {
    public static final int BANNER_VIEW = 0;
    public static final int ITEM_VIEW = 1;
    public static final int EMPTY_VIEW = 2;


    private List<com.bracks.wanandroid.model.bean.Banner.DataBean> bannerData;


    public ChapterAdapter(FragmentActivity activity) {
        super(activity);
    }

    public void setBannerData(List<com.bracks.wanandroid.model.bean.Banner.DataBean> bannerData) {
        this.bannerData = bannerData;
    }

    @Override
    public int getItemCount() {
        return getData().size() == 0 ? 1 : bannerData == null ? super.getItemCount() : super.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (getData().size() == 0) {
            return EMPTY_VIEW;
        }
        if (bannerData == null) {
            return ITEM_VIEW;
        }
        if (position == 0) {
            return BANNER_VIEW;
        } else {
            return ITEM_VIEW;
        }
    }

    @Override
    protected int inflaterItemLayout(int viewType) {
        switch (viewType) {
            case BANNER_VIEW:
                return R.layout.item_banner;
            case ITEM_VIEW:
                return R.layout.item_chapter;
            case EMPTY_VIEW:
            default:
                return R.layout.item_empty;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position == 0 ? 0 : position - 1);
    }

    @Override
    protected void bindData(BaseViewHolder holder, int position, Chapter.DataBean.DatasBean datasBean) {
        switch (holder.getViewType()) {
            case BANNER_VIEW:
                List<String> images = new ArrayList<>();
                List<String> titles = new ArrayList<>();
                for (com.bracks.wanandroid.model.bean.Banner.DataBean dataBean : bannerData) {
                    images.add(dataBean.getImagePath());
                    titles.add(dataBean.getTitle());
                }
                Banner banner = (Banner) holder.getView(R.id.banner);
                banner
                        .setImageLoader(new ImageLoader() {
                            @Override
                            public void displayImage(Context context, Object path, ImageView imageView) {
                                Glide.with(context).load(path).into(imageView);
                            }
                        })
                        .setOnBannerListener(pos ->
                                ActivityUtils.startActivity(
                                        new Intent(getContext(), ArticleUi.class).putExtra(ArticleUi.EXTRA_LINK, bannerData.get(pos).getUrl())
                                )
                        )
                        .setImages(images)
                        .setBannerTitles(titles)
                        .setBannerAnimation(Transformer.DepthPage)
                        .isAutoPlay(true)
                        .setDelayTime(1500)
                        .setDelayTime(1500)
                        .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE)
                        .setIndicatorGravity(BannerConfig.CENTER)
                        .start()
                ;
                break;
            case ITEM_VIEW:
                holder.setText(R.id.tvName, datasBean.getAuthor());
                holder.setText(R.id.tvLable, String.format("%s/%s", datasBean.getSuperChapterName(), datasBean.getChapterName()));
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
                            CollectM collectM = new CollectM(datasBean.getId());
                            if (!datasBean.isCollect()) {
                                collectM
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
                                collectM.
                                        cancelCollect()
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
                break;
            case EMPTY_VIEW:
                holder.setText(R.id.tvEmpty, "Empty");
                break;
            default:
                break;
        }
    }

    @Override
    protected void onItemClickListener(View itemView, int position, Chapter.DataBean.DatasBean datasBean) {
        ActivityUtils.startActivity(
                new Intent(itemView.getContext(), ArticleUi.class).putExtra(ArticleUi.EXTRA_LINK, datasBean.getLink())
        );
    }
}
