package com.bracks.utils.util;


import android.annotation.SuppressLint;

import com.blankj.utilcode.util.FileUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * good programmer.
 *
 * @date : 2019-02-13 上午 09:22
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :本应用数据清除管理器
 */
public class CleanUtils {

    /**************************************************自定义********************************************************/

    /**
     * * 清除本应用所有的数据 *
     *
     * @param filepaths
     */
    public static void cleanApplicationData(String... filepaths) {
        com.blankj.utilcode.util.CleanUtils.cleanInternalCache();
        com.blankj.utilcode.util.CleanUtils.cleanExternalCache();
        com.blankj.utilcode.util.CleanUtils.cleanInternalDbs();
        com.blankj.utilcode.util.CleanUtils.cleanInternalSp();
        com.blankj.utilcode.util.CleanUtils.cleanInternalFiles();
        if (filepaths == null) {
            return;
        }
        for (String filePath : filepaths) {
            FileUtils.deleteFilesInDir(filePath);
        }
    }

    /**
     * 获取文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();

            if (fileList == null) {
                return size;
            }
            for (File aFileList : fileList) {
                // 如果下面还有文件
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    /***
     * 获取应用缓存大小
     * @param file
     * @return
     * @throws Exception
     */
    public static String getCacheSize(File file) throws Exception {
        return getFormatSize(getFolderSize(file));
    }

    /**
     * @param cacheFiles  缓存文件的集合
     * @param directories 缓存文件
     */
    @SuppressLint("SetTextI18n")
    public static String calculateCache(List<File> cacheFiles, File... directories) {
        try {
            long cacheSize = 0;
            cacheFiles.clear();
            cacheFiles.addAll(Arrays.asList(directories));

            for (int i = 0; i < cacheFiles.size(); i++) {
                cacheSize += getFolderSize(cacheFiles.get(i));
            }
            //小于1K为无缓存
            if (cacheSize < 1024) {
                return "0Byte";
            } else {
                return getFormatSize(cacheSize);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "0Byte";
        }
    }
}
