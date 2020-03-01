package com.bracks.utils.util;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.blankj.utilcode.util.SPUtils;
import com.bracks.utils.Constants;

/**
 * good programmer.
 *
 * @data : 2018\4\24 11:04
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class CookieUtils {

    public static final String TAG = "CookieUtils";

    /**
     * webview同步cookie
     *
     * @param context
     * @param url
     */
    public static void synCookies(Context context, String url) {
        if (!TextUtils.isEmpty(url)) {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            //cookieManager.removeSessionCookie();
            //cookieManager.removeAllCookie();

            for (String cookie : SPUtils.getInstance().getString(Constants.COOKIE).split(";")) {
                cookieManager.setCookie(url, cookie);
            }

            /*for (Cookie cookie : HttpManager.getCookies(url)) {
                //Tips:有多个cookie的时候必须设置多次，否则webView只读第一个cookie
                cookieManager.setCookie(url, cookie.toString());
            }*/

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cookieManager.flush();
            } else {
                CookieSyncManager.createInstance(context);
                CookieSyncManager.getInstance().sync();
            }

            String newCookie = cookieManager.getCookie(url);
            //Log.d(TAG, "synCookies: " + newCookie);
        }
    }

    /**
     * webview移除cookie
     *
     * @param context
     */
    public static void removeCookie(Context context) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        cookieManager.removeSessionCookie();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush();
        } else {
            CookieSyncManager.createInstance(context);
            CookieSyncManager.getInstance().sync();
        }
    }
}
