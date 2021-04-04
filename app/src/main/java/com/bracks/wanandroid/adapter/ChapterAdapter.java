package com.bracks.wanandroid.adapter;

import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;

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
import com.jakewharton.rxbinding2.view.RxView;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.listener.OnPageChangeListener;

import java.text.SimpleDateFormat;
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
    private static final int BANNER_VIEW = 0;
    private static final int ITEM_VIEW = 1;
    private static final int EMPTY_VIEW = 2;


    private List<com.bracks.wanandroid.model.bean.Banner.DataBean> bannerData;


    public ChapterAdapter(FragmentActivity activity) {
        super(activity);
    }

    public void setBannerData(List<com.bracks.wanandroid.model.bean.Banner.DataBean> bannerData) {
        this.bannerData = bannerData;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() == 0 ? 1 : bannerData == null ? super.getItemCount() : super.getItemCount() + 1;
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
        super.onBindViewHolder(holder, holder.getViewType() == EMPTY_VIEW ? position : bannerData == null ? position : position == 0 ? 0 : position - 1);
    }

    @Override
    protected void bindData(BaseViewHolder holder, int position, Chapter.DataBean.DatasBean datasBean) {
        switch (holder.getViewType()) {
            case BANNER_VIEW:
                Banner<String, BannerImageAdapter<com.bracks.wanandroid.model.bean.Banner.DataBean>> banner = (Banner) holder.getView(R.id.banner);
                banner
                        .setAdapter(new BannerImageAdapter<com.bracks.wanandroid.model.bean.Banner.DataBean>(bannerData) {
                            @Override
                            public void onBindView(BannerImageHolder holder, com.bracks.wanandroid.model.bean.Banner.DataBean data, int position, int size) {
                                Glide.with(holder.itemView).load(data.getImagePath()).into(holder.imageView);
                            }
                        })
                        .addOnPageChangeListener(new OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                            }

                            @Override
                            public void onPageSelected(int position) {
                                holder.setText(R.id.tvTitle, bannerData.get(position).getTitle());
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {
                            }
                        })
                        .setOnBannerListener((data, position1) ->
                                ActivityUtils.startActivity(new Intent(getContext(), ArticleUi.class).putExtra(ArticleUi.EXTRA_LINK, bannerData.get(position1).getUrl())
                                )
                        )
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
