package com.bracks.utils;

import android.os.Environment;

import java.io.File;

/**
 * good programmer.
 *
 * @date : 2018/10/10 11:43
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public interface Constants {
    /**
     * 本地文件目录名
     */
    String LOCAL_DIR_NAME = "Bracks";
    /**
     * 本地文件根路径
     */
    String LOCAL_FILE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separatorChar + LOCAL_DIR_NAME + File.separatorChar;

    /******************************************************公用SpKey************************************************************/
    String COOKIE = "cookie";
    String LANGUAGE = "language";
}
