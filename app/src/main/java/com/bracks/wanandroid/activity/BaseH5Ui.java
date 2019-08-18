package com.bracks.wanandroid.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.bracks.mylib.base.basemvp.BasePresenter;
import com.bracks.mylib.base.basemvp.BaseView;
import com.bracks.wanandroid.widget.webview.X5WebView;


/**
 * good programmer.
 *
 * @data : 2018\5\15 14:07
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :根据需要继承BaseUi、BaseProxyUi、BaseVmUi、BaseVmProxyUi、BaseActivity等
 */
@SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
public abstract class BaseH5Ui<V extends BaseView, P extends BasePresenter<V>> extends BaseUi<V, P> {

    protected String url;
    protected X5WebView mWebView;


    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.destoryWebView();
        }
        super.onDestroy();
    }

    @Override
    public void showToast(String msg) {
        ToastUtils.showLong(msg);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mWebView = addWebView();
        webViewContainer().addView(mWebView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initWebView(mWebView);

        mWebView.loadUrl(TextUtils.isEmpty(url) ? loadUrl() : url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backFn();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        mWebView.getX5WebChromeClient().onActivityResult(requestCode, resultCode, intent);
    }

    /**
     * 返回键
     *
     * @return
     */
    public void backFn() {
        if (mWebView.canGoBack()) {
            //返回上一页面
            mWebView.goBack();
        } else {
            //退出
            finish();
        }
    }

    /**
     * 描述：添加webview布局
     */
    protected abstract X5WebView addWebView();

    /**
     * webView容器
     *
     * @return
     */
    protected abstract ViewGroup webViewContainer();

    /**
     * 描述：初始化webview
     */
    protected abstract void initWebView(X5WebView mWebView);

    /**
     * 描述:加载H5Url
     */
    protected abstract String loadUrl();
}
