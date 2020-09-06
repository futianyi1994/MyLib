package com.bracks.mylib.net.download.breakpoint;

import android.support.annotation.IntDef;

import com.bracks.mylib.net.BaseService;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * good programmer.
 *
 * @date : 2019-04-03 下午 06:26
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class DownloadInfo {
    public static final int PAUSE = 1;
    public static final int STOP = 2;
    public static final int DOENLOADING = 3;
    /**
     * 存储位置
     */
    private String savePath;
    /**
     * 文件总长度
     */
    private long contentLength;
    /**
     * 下载长度
     */
    private long readLength;
    /**
     * 下载该文件的url
     */
    private String url;

    /**
     * 下载状态
     */
    private @DownloadState
    int state = STOP;
    private BaseService service;

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public long getReadLength() {
        return readLength;
    }

    public void setReadLength(long readLength) {
        this.readLength = readLength;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getState() {
        return state;
    }

    public void setState(@DownloadState int state) {
        this.state = state;
    }

    public BaseService getService() {
        return service;
    }

    public void setService(BaseService service) {
        this.service = service;
    }

    @IntDef({PAUSE, STOP, DOENLOADING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DownloadState {
    }
}
