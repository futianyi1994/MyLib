package com.bracks.utils.util;

import android.media.MediaMetadataRetriever;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * good programmer.
 *
 * @date : 2021-09-29 19:42
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class MediaMetadataUtils {
    private static final String TAG = "MediaMetadataUtils";

    @Nullable
    public static DataBean extractMetadata(String path) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        DataBean dataBean = null;
        try {
            retriever.setDataSource(path);
            String name = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            String album = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            String artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            long duration = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            byte[] picture = retriever.getEmbeddedPicture();
            dataBean = new DataBean(path, name, album, artist, duration, picture);
        } catch (Exception e) {
            Log.e(TAG, "解析音轨信息失败：" + e.getMessage() + "||path=" + path);
        } finally {
            retriever.release();
        }
        return dataBean;
    }

    public static class DataBean {
        /**
         * 路径
         */
        private String path;
        /**
         * 歌曲名称
         */
        private String name;
        /**
         * 专辑名称
         */
        private String album;
        /**
         * 歌手名称
         */
        private String artist;
        /**
         * 时长ms
         */
        private long duration;
        /**
         * 关联的图
         */
        private byte[] picture;

        public DataBean(String path, String name, String album, String artist, long duration, byte[] picture) {
            this.path = path;
            this.name = name;
            this.album = album;
            this.artist = artist;
            this.duration = duration;
            this.picture = picture;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAlbum() {
            return album;
        }

        public void setAlbum(String album) {
            this.album = album;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public long getDuration() {
            return duration;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }

        public byte[] getPicture() {
            return picture;
        }

        public void setPicture(byte[] picture) {
            this.picture = picture;
        }
    }
}
