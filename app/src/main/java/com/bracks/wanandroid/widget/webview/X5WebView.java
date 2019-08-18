package com.bracks.wanandroid.widget.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Picture;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.bracks.wanandroid.R;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

/**
 * good programmer.
 *
 * @date : 2018/9/5 15:07
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class X5WebView extends WebView {

    private Context mContext;
    private Activity mActivity;

    private X5WebChromeClient x5WebChromeClient;
    private X5WebViewClient x5WebViewClient;

    public X5WebView(Context context) {
        this(context, null, null, true, false);
    }

    public X5WebView(Context context, boolean isShowDialog) {
        this(context, null, null, isShowDialog, false);
    }

    public X5WebView(Context context, boolean isShowDialog, boolean payInterceptorWithUrl) {
        this(context, null, null, isShowDialog, payInterceptorWithUrl);
    }

    public X5WebView(Context context, X5JavaScriptinterface o) {
        this(context, null, o, true, false);
    }

    public X5WebView(Context context, X5JavaScriptinterface o, boolean isShowDialog) {
        this(context, null, o, isShowDialog, false);
    }

    public X5WebView(Context context, X5JavaScriptinterface o, boolean isShowDialog, boolean payInterceptorWithUrl) {
        this(context, null, o, isShowDialog, payInterceptorWithUrl);
    }

    public X5WebView(Context context, AttributeSet arg1) {
        this(context, arg1, null, true, false);
    }

    public X5WebView(Context context, AttributeSet arg1, boolean isShowDialog) {
        this(context, arg1, null, isShowDialog, false);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public X5WebView(Context context, AttributeSet arg1, X5JavaScriptinterface o, boolean isShowDialog, boolean payInterceptorWithUrl) {
        super(context, arg1);
        mContext = context;
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
        this.addJavascriptInterface(o == null ? new X5JavaScriptinterface(context).setWebView(this) : o.setWebView(this), "android");

        x5WebViewClient = new X5WebViewClient(mContext, payInterceptorWithUrl);
        this.setWebViewClient(x5WebViewClient);

        x5WebChromeClient = new X5WebChromeClient(mContext, isShowDialog);
        this.setWebChromeClient(x5WebChromeClient);

        initWebViewSettings();
        this.getView().setClickable(true);
    }

    private void initWebViewSettings() {
        WebSettings webSetting = this.getSettings();
        //设置支持javascript脚本
        webSetting.setJavaScriptEnabled(true);
        //支持通过JS打开新窗口
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        //允许访问文件
        webSetting.setAllowFileAccess(true);

        //支持缩放，默认为true。是下面那个的前提。
        webSetting.setSupportZoom(true);
        //设置内置的缩放控件。若为false，则该WebView不可缩放（wap网页不支持）
        webSetting.setBuiltInZoomControls(true);
        //隐藏原生的缩放控件
        //webSetting.setDisplayZoomControls(false);

        // 用WebView显示图片，可使用这个参数 设置网页布局类型：
        // 1、LayoutAlgorithm.NARROW_COLUMNS ：适应内容大小
        // 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

        //设置自适应屏幕，两者合用
        //将图片调整到适合webview的大小
        webSetting.setUseWideViewPort(true);
        //设置加载进来的页面自适应手机屏幕
        //webSetting.setLoadWithOverviewMode(true);

        //为25%，最小缩放等级.100:代表不缩放。解释： 里面的数字代表缩放等级
        setInitialScale(25);
        //设置WebView是否支持多窗口。
        webSetting.setSupportMultipleWindows(true);
        //开启 Application Caches 功能
        webSetting.setAppCacheEnabled(true);
        //设置缓存大小
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        //开启 database storage API 功能
        //webSetting.setDatabaseEnabled(true);
        //开启 DOM storage API 功能
        webSetting.setDomStorageEnabled(true);
        //启用地理定位
        webSetting.setGeolocationEnabled(true);

        //webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        //webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setMixedContentMode(WebSettings.LOAD_DEFAULT);

        //缓存模式如下：
        //LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);

        //修改ua使得web端正确判断
        String ua = webSetting.getUserAgentString();
        //设置WebView的UserAgent值
        webSetting.setUserAgentString(ua + "koudaishu");
    }

    /**
     * 在X5内核中提供了一个截取整个WebView界面的方法snapshotWholePage(Canvas, boolean, boolean)，
     * 但是这个方法有个缺点，就是不以屏幕上WebView的宽高截图，只是以WebView的contentWidth和contentHeight为宽高截图，
     * 所以截出来的图片会不怎么清晰，但作为缩略图效果还是不错了。
     *
     * @return
     */
    public Bitmap snapshotWholePage() {
        measure(MeasureSpec.makeMeasureSpec(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
        setDrawingCacheEnabled(true);
        buildDrawingCache();
        Bitmap longImage = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        //画布的宽高和 WebView 的网页保持一致
        Canvas canvas = new Canvas(longImage);
        //画布的宽高和WebView的网页保持一致
        Canvas longCanvas = new Canvas(longImage);
        Paint paint = new Paint();
        canvas.drawBitmap(longImage, 0, getMeasuredHeight(), paint);

        float scale = getResources().getDisplayMetrics().density;
        Bitmap x5Bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas x5Canvas = new Canvas(x5Bitmap);
        x5Canvas.drawColor(ContextCompat.getColor(getContext(), R.color.transparent));
        //少了这行代码就无法正常生成长图
        getX5WebViewExtension().snapshotWholePage(x5Canvas, false, false);
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        longCanvas.drawBitmap(x5Bitmap, matrix, paint);

        return longImage;
    }

    /**
     * X5内核下使用capturePicture()进行截图，可以直接拿到WebView的清晰长图
     *
     * @return
     */
    public Bitmap getLongCapture() {
        Picture picture = capturePicture();
        int width = picture.getWidth();
        int height = picture.getHeight();
        if (width > 0 && height > 0) {
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            //少了这行代码就无法正常生成长图
            getX5WebViewExtension().snapshotWholePage(canvas, false, false);
            picture.draw(canvas);
            return bitmap;
        }
        return null;
    }

    /**
     * 销毁webview
     */
    public void destoryWebView() {
        try {
            this.loadUrl("about:blank");
            this.clearHistory();
            this.clearCache(true);
            this.freeMemory();
            ViewGroup viewGroup = (ViewGroup) getParent();
            if (viewGroup != null) {
                viewGroup.removeView(this);
            }
            this.removeAllViews();
            this.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //webView.pauseTimers();
    }

    /**
     * 销毁dialog
     */
    public void dialogDismiss() {
        x5WebChromeClient.dialogDismiss();
    }

    public X5WebChromeClient getX5WebChromeClient() {
        return x5WebChromeClient;
    }

    public X5WebViewClient getX5WebViewClient() {
        return x5WebViewClient;
    }
}
