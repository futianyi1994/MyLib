package com.bracks.futia.mylib.utils.device;

import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * good programmer.
 *
 * @date : 2019-02-20 上午 10:17
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class RomUtils {

    public static final int MIUI = 1;
    public static final int FLYME = 2;
    public static final int ANDROID_NATIVE = 3;
    public static final int NA = 4;


    /**
     * 获取亮色状态栏的手机系统类型
     *
     * @return
     */
    public static int getLightStatusBarAvailableRomType() {
        //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错
        if (isMiUIV7OrAbove()) {
            return ANDROID_NATIVE;
        }
        if (isMiUIV6OrAbove()) {
            return MIUI;
        }
        if (isFlymeV4OrAbove()) {
            return FLYME;
        }
        if (isAndroidMOrAbove()) {
            return ANDROID_NATIVE;
        }
        return NA;
    }

    /**
     * Flyme V4的displayId格式为 [Flyme OS 4.x.x.xA]
     * Flyme V5的displayId格式为 [Flyme 5.x.x.x beta]
     *
     * @return
     */
    public static boolean isFlymeV4OrAbove() {
        String displayId = Build.DISPLAY;
        if (!TextUtils.isEmpty(displayId) && displayId.contains("Flyme")) {
            String[] displayIdArray = displayId.split(" ");
            for (String temp : displayIdArray) {
                //版本号4以上，形如4.x.
                if (temp.matches("^[4-9]\\.(\\d+\\.)+\\S*")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Android Api 23以上
     *
     * @return
     */
    public static boolean isAndroidMOrAbove() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 是否MIUI6及以上
     *
     * @return
     */
    public static boolean isMiUIV6OrAbove() {
        try {
            final Properties properties = new Properties();
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            String uiCode = properties.getProperty("ro.miui.ui.version.code", null);
            if (uiCode != null) {
                int code = Integer.parseInt(uiCode);
                return code >= 4;
            } else {
                return false;
            }

        } catch (final Exception e) {
            return false;
        }
    }

    /**
     * 是否MIUI7及以上
     *
     * @return
     */
    public static boolean isMiUIV7OrAbove() {
        try {
            final Properties properties = new Properties();
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            String uiCode = properties.getProperty("ro.miui.ui.version.code", null);
            if (uiCode != null) {
                int code = Integer.parseInt(uiCode);
                return code >= 5;
            } else {
                return false;
            }

        } catch (final Exception e) {
            return false;
        }
    }


    /*************************************************************************************************************/


    /**
     * 是否华为手机
     *
     * @return
     */
    public static boolean isHuawei() {
        return Build.MANUFACTURER.contains("HUAWEI");
    }

    /**
     * 是否360手机
     *
     * @return
     */
    public static boolean is360() {
        //fix issue https://github.com/zhaozepeng/FloatWindowPermission/issues/9
        return Build.MANUFACTURER.contains("QiKU") || Build.MANUFACTURER.contains("360");
    }

    /**
     * 是否OPPO手机
     *
     * @return
     */
    public static boolean isOppo() {
        //https://github.com/zhaozepeng/FloatWindowPermission/pull/26
        return Build.MANUFACTURER.contains("OPPO") || Build.MANUFACTURER.contains("oppo");
    }
}
