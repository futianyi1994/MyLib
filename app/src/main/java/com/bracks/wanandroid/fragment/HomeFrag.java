package com.bracks.wanandroid.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bracks.mylib.base.basemvp.BaseProxyFrag;
import com.bracks.mylib.base.basemvp.CreatePresenter;
import com.bracks.mylib.utils.bar.BarUtils;
import com.bracks.mylib.utils.widget.DialogUtils;
import com.bracks.wanandroid.R;
import com.bracks.wanandroid.activity.ArticleUi;
import com.bracks.wanandroid.contract.HomeFragContract;
import com.bracks.wanandroid.model.bean.HomeList;
import com.bracks.wanandroid.presenter.HomeFragP;
import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

@CreatePresenter(HomeFragP.class)
public class HomeFrag extends BaseProxyFrag<HomeFragContract.View, HomeFragP> implements HomeFragContract.View {

    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Dialog dialog;

    public static HomeFrag newInstance() {
        Bundle args = new Bundle();
        HomeFrag fragment = new HomeFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView(View view, @Nullable Bundle savedInstanceState) {
        getPresenter().fetch();
        refreshLayout.setEnableOverScrollDrag(true);
        refreshLayout.setOnRefreshListener(refreshLayout -> getPresenter().fetch());
    }

    @Override
    public void showDatas(List<HomeList.DataBean.DatasBean> data) {
//        PublicAdapter adapter = new PublicAdapter();
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        adapter.setData(data);
//        recyclerView.setAdapter(adapter);
        refreshLayout.finishRefresh();
    }

    @Override
    public void showBanner(List<com.bracks.wanandroid.model.bean.Banner.DataBean> data) {
        List<String> images = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        for (com.bracks.wanandroid.model.bean.Banner.DataBean dataBean : data) {
            images.add(dataBean.getImagePath());
            titles.add(dataBean.getTitle());
        }
        banner
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        Glide.with(context).load(path).into(imageView);
                    }
                })
                .setOnBannerListener(position -> ActivityUtils.startActivity(
                        new Intent(getContext(), ArticleUi.class).putExtra(ArticleUi.EXTRA_LINK, data.get(position).getUrl())
                ))
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
    }

    @Override
    public void showLoading(String msg, boolean isCancelable) {
        dialog = DialogUtils.createLoadingDialog(getActivity(), msg, isCancelable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            DialogUtils.afterShow(() -> BarUtils.hideNavBar(dialog.getWindow().getDecorView()));
        } else {
            dialog.show();
        }
    }

    @Override
    public void hideLoading() {
        DialogUtils.dismissDialog(dialog);
    }

    @Override
    public void showToast(String msg) {
        ToastUtils.showLong(msg);
    }
}
