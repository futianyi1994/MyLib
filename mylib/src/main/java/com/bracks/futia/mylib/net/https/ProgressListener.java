package com.bracks.futia.mylib.net.https;

/**
 * good programmer.
 *
 * @date : 2018-09-02 上午 11:33
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :（上传和下载）接口
 */
public interface ProgressListener {

    /**
     * 子线程下载或上传的回调
     *
     * @param progress 已经下载或上传字节数
     * @param total    总字节数
     * @param done     是否完成
     */
    void onProgress(long progress, long total, boolean done);

    interface ProgressCallback {
        /**
         * 回调到主线程
         *
         * @param progressBean
         */
        void onUpload(ProgressBean progressBean);
    }
}
