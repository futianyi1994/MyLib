package com.bracks.mylib.activity;

import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import com.bracks.futia.mylib.base.basemvp.BasePresenter;
import com.bracks.futia.mylib.base.basemvp.BaseView;
import com.bracks.futia.mylib.base.basemvp.CreatePresenter;
import com.bracks.mylib.R;

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
public class ArticleUi extends BaseUi<BaseView, BasePresenter<BaseView>> {

    public static final String EXTRA_LINK = "link";

    @BindView(R.id.webView)
    WebView webView;
    private String link;

    @Override
    protected ViewModel initViewModel() {
        return null;
    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activty_article;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null) {
            link = intent.getStringExtra(EXTRA_LINK);
        }

        webView.loadUrl(link);
    }

}
