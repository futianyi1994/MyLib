package com.bracks.wanandroid.widget.webview;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import com.bracks.mylib.utils.CommonUtils;
import com.bracks.wanandroid.utils.CookieUtils;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;


/**
 * good programmer.
 *
 * @date : 2018-08-31 16:58
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class X5WebViewClient extends WebViewClient {
    private Context mContext;
    private Activity mActivity;
    /**
     * true:开启全局监听支付宝支付url
     */
    private boolean payInterceptorWithUrl;

    public X5WebViewClient(Context mContext) {
        this(mContext, false);
    }

    public X5WebViewClient(Context mContext, boolean payInterceptorWithUrl) {
        this.mContext = mContext;
        if (mContext instanceof Activity) {
            mActivity = (Activity) mContext;
        }
        this.payInterceptorWithUrl = payInterceptorWithUrl;
    }

    /**
     * 防止加载网页时调起系统浏览器
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (!(url.startsWith("http") || url.startsWith("https"))) {
            return true;
        }
        if (payInterceptorWithUrl) {
            /**
             * 判断是否成功拦截
             * 若成功拦截，则无需继续加载该URL；否则继续加载
             */
            if (!payInterceptorWithUrl(mActivity, view, url, true)) {
                view.loadUrl(url);
            }
        } else {
            view.loadUrl(url);
        }
        return true;
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        String requesetUrl = request.getUrl().toString();
        CookieUtils.synCookies(CommonUtils.getContext(), requesetUrl);
        return super.shouldInterceptRequest(view, requesetUrl);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView webView, String s) {
        CookieUtils.synCookies(CommonUtils.getContext(), s);
        return super.shouldInterceptRequest(webView, s);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        //viewClientCallBack.onReceivedSslError(view,handler,error);
        //接受证书
        handler.proceed();
        super.onReceivedSslError(view, handler, error);
    }

    /**
     * 手机网站支付转Native支付
     * 支付宝H5支付URL拦截器，完成拦截及支付方式转化
     * https://docs.open.alipay.com/203/106493/
     *
     * @param h5PayUrl         待过滤拦截的 URL
     * @param isShowPayLoading 是否出现loading
     * @return true：表示URL为支付宝支付URL，URL已经被拦截并支付转化,商户容器无需再加载该URL；false：表示URL非支付宝支付URL,商户容器需要继续加载该URL；
     */
    public boolean payInterceptorWithUrl(Activity mActivity, WebView view, String h5PayUrl, boolean isShowPayLoading) {
        // TODO: 2019-06-10 Fty: 使用以下注释代码，需要支付宝SDK
        /*final PayTask task = new PayTask(mActivity);
        boolean isIntercepted = task.payInterceptorWithUrl(h5PayUrl, true, new H5PayCallback() {
            @Override
            public void onPayResult(final H5PayResultModel result) {
                //支付结果返回
                final String url = result.getReturnUrl();
                if (!TextUtils.isEmpty(url)) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.loadUrl(url);
                        }
                    });
                }
            }
        });
        return isIntercepted;*/
        return false;
    }

    /**
     * 唤起支付宝App
     *
     * @param context
     * @param url
     * @return
     */
    public void startAliPay(Context context, String url) {
        //对alipays:相关的scheme处理
        if (url.startsWith("alipays:") || url.startsWith("alipay")) {
            try {
                context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
            } catch (Exception e) {
                new AlertDialog.Builder(context)
                        .setMessage("未检测到支付宝客户端，请安装后重试。")
                        .setPositiveButton("立即安装", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Uri alipayUrl = Uri.parse("https://d.alipay.com");
                                context.startActivity(new Intent("android.intent.action.VIEW", alipayUrl));
                            }
                        }).setNegativeButton("取消", null).show();
            }
        }
    }
}
