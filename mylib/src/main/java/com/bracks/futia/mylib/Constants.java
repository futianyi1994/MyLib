package com.bracks.futia.mylib;

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
     * 测试的图片地址
     */
    String TEST_IMG_URL = "https://media.w3.org/2010/05/sintel/poster.png";
    /**
     * 测试mp4地址
     */
    String TEST_MP4_URL = "https://media.w3.org/2010/05/sintel/trailer.mp4";

    /**
     * 测试的API_URL
     */
    String API_URL = "http://www.wanandroid.com/";

    /**
     * 请求失败总共重试maxRetries次
     */
    int MAX_RETRIES = 20;

    /**
     * 请求失败重试间隔毫秒
     */
    long RETRY_DELAY_MILLIS = 3 * 1000L;

    /**
     * httpCache
     */
    String HTTP_CACHE = "http_cache";

    /**
     * 本地文件目录名
     */
    String LOCAL_FILE_DIR = "Bracks";

    /**
     * 本地文件根路径
     */
    String LOCAL_FILE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separatorChar + LOCAL_FILE_DIR + File.separatorChar;

    /******************************************************公用SpKey************************************************************/

    String COOKIE = "cookie";
    String TOKEN = "token";
}
