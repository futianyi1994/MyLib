package com.bracks.utils.util;


import android.content.Context;
import android.os.Build;
import android.view.ViewConfiguration;

import java.io.File;
import java.io.FileFilter;

/**
 * good programmer.
 *
 * @date : 2018/8/13 19:11
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class DeviceUtils {

    /**
     * Linux中的设备都是以文件的形式存在，CPU也不例外，因此CPU的文件个数就等价与核数。
     * Android的CPU 设备文件位于/sys/devices/system/cpu/目录，文件名的的格式为cpu\d+。
     */
    public static int getCountOfCPU() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            return 1;
        }
        int count;
        try {
            count = new File("/sys/devices/system/cpu/")
                    .listFiles(new FileFilter() {
                        @Override
                        public boolean accept(File pathname) {
                            String path = pathname.getName();
                            if (path.startsWith("cpu")) {
                                for (int i = 3; i < path.length(); i++) {
                                    if (path.charAt(i) < '0' || path.charAt(i) > '9') {
                                        return false;
                                    }
                                }
                                return true;
                            }
                            return false;
                        }
                    })
                    .length;
        } catch (SecurityException | NullPointerException e) {
            count = 0;
        }
        return count;
    }

    /**
     * 判断是否有菜单键
     *
     * @param context
     * @return
     */
    public static boolean hasHardwareMenuKey(Context context) {
        boolean flag = false;
        if (Build.VERSION.SDK_INT < 11) {
            flag = true;
        } else if (Build.VERSION.SDK_INT >= 14) {
            flag = ViewConfiguration.get(context).hasPermanentMenuKey();
        } else {
            flag = false;
        }
        return flag;
    }
}
