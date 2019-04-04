package com.bracks.futia.mylib.net.download.breakpoint;

import android.os.Environment;

import com.blankj.utilcode.util.FileUtils;
import com.bracks.futia.mylib.utils.log.TLog;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import okhttp3.ResponseBody;

/**
 * good programmer.
 *
 * @date : 2019-04-03 下午 06:36
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class DownloadUtils {
    public static final String TAG = "DownloadUtils";
    private static final String APK_CONTENTTYPE = "application/vnd.android.package-archive";
    private static final String PNG_CONTENTTYPE = "image/png";
    private static final String JPG_CONTENTTYPE = "image/jpg";
    private static final String MP4_CONTENTTYPE = "video/mp4";
    private static String fileName = "";

    /**
     * 写入文件
     *
     * @param responseBody
     * @param file
     * @param info
     * @return
     * @throws IOException
     */
    public static DownloadInfo writeCache(ResponseBody responseBody, File file, DownloadInfo info) throws IOException {
        if (file == null) {
            String type = responseBody.contentType().toString();
            TLog.d(TAG, "contentType:>>>>" + type);
            if (type.equals(APK_CONTENTTYPE)) {
                fileName = FileUtils.getFileNameNoExtension(info.getUrl()) + ".apk";
            } else if (type.equals(PNG_CONTENTTYPE)) {
                fileName = FileUtils.getFileNameNoExtension(info.getUrl()) + ".png";
            } else if (type.equals(JPG_CONTENTTYPE)) {
                fileName = FileUtils.getFileNameNoExtension(info.getUrl()) + ".jpg";
            } else if (type.equals(MP4_CONTENTTYPE)) {
                fileName = FileUtils.getFileNameNoExtension(info.getUrl()) + ".mp4";
            } else {
                fileName = FileUtils.getFileName(info.getUrl());
            }
            //设置默认保存路径
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
            info.setSavePath(file.getAbsolutePath());
        }
        FileUtils.createOrExistsFile(file);

        long allLength;
        if (info.getContentLength() == 0) {
            allLength = responseBody.contentLength();
        } else {
            allLength = info.getContentLength();
        }
        FileChannel channelOut;
        RandomAccessFile randomAccessFile;
        randomAccessFile = new RandomAccessFile(file, "rwd");
        channelOut = randomAccessFile.getChannel();
        MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE, info.getReadLength(), allLength - info.getReadLength());
        byte[] buffer = new byte[1024 * 8];
        int len;
        int record = 0;
        while ((len = responseBody.byteStream().read(buffer)) != -1) {
            mappedBuffer.put(buffer, 0, len);
            record += len;
        }
        responseBody.byteStream().close();
        channelOut.close();
        randomAccessFile.close();
        return info;
    }
}
