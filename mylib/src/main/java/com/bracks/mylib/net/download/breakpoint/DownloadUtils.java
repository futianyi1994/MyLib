package com.bracks.mylib.net.download.breakpoint;

import android.os.Environment;
import android.text.TextUtils;

import com.blankj.utilcode.util.FileUtils;
import com.bracks.mylib.utils.log.TLog;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.UUID;

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
    private static File mFile;
    private static File mFileDir;
    private static String mFileName = "";

    /**
     * 写入文件
     *
     * @param responseBody
     * @param info
     * @return
     * @throws IOException
     */
    public static DownloadInfo writeCache(ResponseBody responseBody, DownloadInfo info) throws IOException {
        return writeCache(responseBody, null, null, null, info);
    }

    /**
     * 写入文件
     *
     * @param responseBody
     * @param fileDir
     * @param info
     * @return
     * @throws IOException
     */
    public static DownloadInfo writeCache(ResponseBody responseBody, File fileDir, DownloadInfo info) throws IOException {
        return writeCache(responseBody, null, fileDir, null, info);
    }

    /**
     * 写入文件
     *
     * @param responseBody
     * @param fileName
     * @param info
     * @return
     * @throws IOException
     */
    public static DownloadInfo writeCache(ResponseBody responseBody, String fileName, DownloadInfo info) throws IOException {
        return writeCache(responseBody, null, null, fileName, info);
    }

    /**
     * 写入文件
     *
     * @param responseBody
     * @param file
     * @param fileDir
     * @param fileName
     * @param info
     * @return
     * @throws IOException
     */
    public static DownloadInfo writeCache(ResponseBody responseBody, File file, File fileDir, String fileName, DownloadInfo info) throws IOException {
        if (file == null) {
            mFileDir = fileDir == null ? Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) : fileDir;
            if (TextUtils.isEmpty(fileName)) {
                String type = responseBody.contentType().toString();
                TLog.d(TAG, "contentType:>>>>" + type);
                switch (type) {
                    case APK_CONTENTTYPE:
                        mFileName = FileUtils.getFileNameNoExtension(info.getUrl()) + ".apk";
                        break;
                    case PNG_CONTENTTYPE:
                        mFileName = FileUtils.getFileNameNoExtension(info.getUrl()) + ".png";
                        break;
                    case JPG_CONTENTTYPE:
                        mFileName = FileUtils.getFileNameNoExtension(info.getUrl()) + ".jpg";
                        break;
                    case MP4_CONTENTTYPE:
                        mFileName = FileUtils.getFileNameNoExtension(info.getUrl()) + ".mp4";
                        break;
                    default:
                        mFileName = FileUtils.getFileName(info.getUrl());
                        if (TextUtils.isEmpty(mFileName)) {
                            //默认取一个文件名
                            mFileName = UUID.randomUUID() + ".tmp";
                        }
                        break;
                }
            } else {
                mFileName = fileName;
            }
            mFile = new File(mFileDir, mFileName);
        } else {
            mFile = file;
        }
        info.setSavePath(mFile.getAbsolutePath());
        FileUtils.createOrExistsFile(mFile);

        long allLength;
        if (info.getContentLength() == 0) {
            allLength = responseBody.contentLength();
        } else {
            allLength = info.getContentLength();
        }
        FileChannel channelOut;
        RandomAccessFile randomAccessFile;
        randomAccessFile = new RandomAccessFile(mFile, "rwd");
        channelOut = randomAccessFile.getChannel();
        MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE, info.getReadLength(), allLength - info.getReadLength());
        byte[] buffer = new byte[1024 << 2];
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
