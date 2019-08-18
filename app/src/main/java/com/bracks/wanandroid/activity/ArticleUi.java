package com.bracks.wanandroid.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModel;

import com.bracks.mylib.base.basemvp.BasePresenter;
import com.bracks.mylib.base.basemvp.BaseView;
import com.bracks.mylib.base.basemvp.CreatePresenter;
import com.bracks.wanandroid.R;
import com.bracks.wanandroid.widget.webview.X5WebView;
import com.bracks.wanandroid.widget.webview.X5WebViewClient;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.smtt.sdk.WebView;

import butterknife.BindView;

/**
 * good programmer.
 *
 * @date : 2019-02-19 下午 04:48
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
@CreatePresenter(BasePresenter.class)
public class ArticleUi extends BaseH5Ui<BaseView, BasePresenter<BaseView>> {

    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.webView)
    NestedScrollView rootWebView;

    public static final String EXTRA_LINK = "link";

    private String link;

    @Override
    public int getLayoutId() {
        return R.layout.activity_baseh5;
    }

    @Override
    protected ViewModel initViewModel() {
        return null;
    }

    @Override
    protected X5WebView addWebView() {
        return new X5WebView(this);
    }

    @Override
    protected ViewGroup webViewContainer() {
        return rootWebView;
    }

    @Override
    protected void initWebView(X5WebView mWebView) {
        setSupportActionBar(toolbar);
        //设置是否显示返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbar.setTitle("公众号");
        collapsingToolbar.setExpandedTitleColor(Color.WHITE);
        collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
        refreshLayout.setEnableOverScrollDrag(true);
        refreshLayout.setOnRefreshListener(refreshLayout -> mWebView.loadUrl(link));
        mWebView.setWebViewClient(new X5WebViewClient(this) {
            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                refreshLayout.finishRefresh();
            }
        });
        toolbar.setNavigationOnClickListener(v -> backFn());
    }

    @Override
    protected String loadUrl() {
        Intent intent = getIntent();
        if (intent != null) {
            link = intent.getStringExtra(EXTRA_LINK);
        }
        return link;
    }
}
