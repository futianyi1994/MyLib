package com.bracks.utils.util;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * good programmer.
 *
 * @date : 2018/9/21 11:57
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class UUIDTool {

    public UUIDTool() {
    }

    /**
     * 自动生成32位的UUid
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取设备唯一ID
     *
     * @param context
     * @return
     */
    public static String getUniqueId(Context context) {
        String androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String id = androidID + Build.SERIAL;
        try {
            return EncodeUtils.toMD5(id);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return id;
        }
    }

    /**
     * 通过UUID生成16位唯一订单号
     *
     * @return
     */
    public static String getOrdCdByUUId() {
        //最大支持1-9个集群机器部署
        int machineId = 1;
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        //有可能是负数
        if (hashCodeV < 0) {
            hashCodeV = -hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return machineId + String.format("%015d", hashCodeV);
    }
}
