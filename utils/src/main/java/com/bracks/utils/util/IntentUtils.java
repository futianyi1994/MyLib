package com.bracks.utils.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.widget.Toast;

import java.util.List;

/**
 * good programmer.
 *
 * @date : 2019-04-01 下午 02:55
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class IntentUtils {


    /**************************************************Market**************************************************/


    /**
     * 打开GoogleMarket
     *
     * @param activity
     * @param pck
     * @return
     */
    public static boolean gotoGoogleMarket(Activity activity, String pck) {
        try {
            Intent intent = new Intent();
            intent.setPackage("com.android.vending");
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + pck));
            activity.startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断是否有应用市场
     *
     * @param context
     * @return
     */
    public static boolean isHaveMarket(Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.APP_MARKET");
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
        return infos.size() > 0;
    }

    /**
     * 打开应用市场
     *
     * @param context
     * @param pck
     */
    public static void gotoMarket(Context context, String pck) {
        if (!isHaveMarket(context)) {
            Toast.makeText(context, "你手机中没有安装应用市场！", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + pck));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    /**
     * 在应用市场中打开app
     *
     * @param context
     */
    public static void openAppInMarket(Context context) {
        if (context != null) {
            String pckName = context.getPackageName();
            try {
                gotoMarket(context, pckName);
            } catch (Exception ex) {
                try {
                    String otherMarketUri = "http://market.android.com/details?id=" + pckName;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(otherMarketUri));
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**************************************************System**************************************************/


    /**
     * 发送邮件
     *
     * @param context
     * @param subject 主题
     * @param content 内容
     * @param emails  邮件地址
     */
    public static void sendEmail(Context context, String subject, String content, String... emails) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            //模拟器
            //intent.setType("text/plain");
            //真机
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, emails);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, content);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
