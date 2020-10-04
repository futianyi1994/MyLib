package com.bracks.wanandroid.widget.webview;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.AppUtils;
import com.bracks.mylib.base.BaseActivity;

/**
 * good programmer.
 *
 * @date : 2018-09-06 12:39
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class X5JavaScriptinterface {
    public static final String TAG = "X5JavaScriptinterface";
    private Context mContext;
    private BaseActivity mActivity;
    private X5WebView webView;


    public X5JavaScriptinterface(Context context) {
        this(context, null);
    }

    public X5JavaScriptinterface(Context context, X5WebView webView) {
        this.mContext = context;
        if (context instanceof BaseActivity) {
            mActivity = (BaseActivity) context;
        }
        this.webView = webView;
    }

    public X5JavaScriptinterface setWebView(X5WebView webView) {
        this.webView = webView;
        return this;
    }

    /**
     * 设置标题
     *
     * @param title
     */
    @JavascriptInterface
    public void setTitle(String title) {
    }

    /**
     * 跳转到登陆
     *
     * @param data
     */
    @JavascriptInterface
    public void toLogin(String data) {
    }

    /**
     * 微信支付
     *
     * @param data
     */
    @JavascriptInterface
    public void WxPayH5(String data) {
    }

    /**
     * 支付宝支付(当需要支付成功及失败回调时需要重写该方法传入handler)
     *
     * @param data
     */
    @JavascriptInterface
    public void AliPayH5(String data) {
    }

    /**
     * 传递版本号
     *
     * @param data
     */
    @JavascriptInterface
    public void H5Ready(String data) {
        if (webView != null) {
            webView.loadUrl("javascript:setVersion('" + AppUtils.getAppVersionName() + "')");
        } else {
            Log.e(TAG, "H5Ready: " + "传递版本号失败--webView=null");
        }
    }

    /**
     * 退出
     *
     * @param data
     */
    @JavascriptInterface
    public void backFn(String data) {
        mActivity.finish();
    }
}
