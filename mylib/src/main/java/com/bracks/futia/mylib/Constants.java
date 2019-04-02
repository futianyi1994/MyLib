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
    String TEST_IMG_URL = "https://koudairoo-dev.oss-cn-hangzhou.aliyuncs.com/upload/RepnAxW7n2.jpg";

    /**
     * true:测试地址；false:正式地址
     */
    boolean IS_TEST = true;

    String IP = "202.108.22.59";
    String HOST = IS_TEST ? "http://www.wanandroid.com/" : "http://www.wanandroid.com/";
    String API_URL = HOST;
    String DOWN_UP_LOAD_HOST = API_URL;

    String WEBSITE = IS_TEST ? "http://www.wanandroid.com/" : "http://www.wanandroid.com/";

    /**
     * 轮询请求接口的时间
     */
    long POST_DELAY = 10 * 60 * 1000L;

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
     * SpName
     */
    String SP_FILE_NAME = "config";

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
}
