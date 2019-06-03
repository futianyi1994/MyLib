package com.bracks.mylib.net.https;

/**
 * good programmer.
 *
 * @date : 2018-09-02 上午 11:31
 * @author: futia
 * @email : futianyi1994@126.com
 * @description : 进度条实体（上传和下载）
 */
public class ProgressBean {

    private String title;
    private long bytesRead;
    private long contentLength;
    private int progress;
    private boolean done;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getBytesRead() {
        return bytesRead;
    }

    public void setBytesRead(long bytesRead) {
        this.bytesRead = bytesRead;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
