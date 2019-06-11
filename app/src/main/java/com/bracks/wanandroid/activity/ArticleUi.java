package com.bracks.wanandroid.activity;

import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bracks.mylib.base.basemvp.BasePresenter;
import com.bracks.mylib.base.basemvp.BaseView;
import com.bracks.mylib.base.basemvp.CreatePresenter;
import com.bracks.wanandroid.R;
import com.bracks.wanandroid.utils.ToolbarUtils;
import com.bracks.wanandroid.widget.webview.X5WebView;

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

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.webView)
    FrameLayout rootWebView;

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
        //设置是否显示返回按钮，注意：这里个方法是actionBar的方法
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> backFn());
        ToolbarUtils.addTitleCenter(toolbar, "sdsad");
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
