package com.bracks.utils.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;

import androidx.annotation.IntRange;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.Utils;

import java.lang.ref.WeakReference;

/**
 * good programmer.
 *
 * @date : 2021-01-28 13:22
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class VolumeUtils {
    public static final int MIN_VOLUME_C11 = 0;
    public static final int MAX_VOLUME_C11 = 66;
    public static final String VOLUME_GLOBAL_KEY_NAME = "C11_MUSIC";
    private static final String VOLUME_CHANGED_ACTION = "android.media.VOLUME_CHANGED_ACTION";
    private static final String EXTRA_VOLUME_STREAM_TYPE = "android.media.EXTRA_VOLUME_STREAM_TYPE";
    private static int streamType = -1;
    private SettingsContentObserver contentObserver;
    private C11SettingsContentObserver c11ContentObserver;
    private VolumeChangeListener mVolumeChangeListener;
    private VolumeBroadcastReceiver mVolumeBroadcastReceiver;

    private VolumeUtils() {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @IntRange(from = MIN_VOLUME_C11, to = MAX_VOLUME_C11)
    public static int getC11Volume() throws Settings.SettingNotFoundException {
        return Settings.Global.getInt(Utils.getApp().getContentResolver(), VOLUME_GLOBAL_KEY_NAME);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void setC11Volume(@IntRange(from = MIN_VOLUME_C11, to = MAX_VOLUME_C11) int c11Volume) {
        Settings.Global.putInt(Utils.getApp().getContentResolver(), VOLUME_GLOBAL_KEY_NAME, c11Volume);
    }

    public static VolumeUtils getInstance() {
        return SingleInstanceHolder.INSTANCE;
    }

    public static float convertVolume(float sinkMinVolume, float sinkMaxVolume, float sourceVolume) {
        return convertVolume(getStreamType(), sinkMinVolume, sinkMaxVolume, -1, -1, sourceVolume);
    }

    public static float convertVolume(float sinkMinVolume, float sinkMaxVolume, float sourceMinVolume, float sourceMaxVolume, float sourceVolume) {
        return convertVolume(getStreamType(), sinkMinVolume, sinkMaxVolume, sourceMinVolume, sourceMaxVolume, sourceVolume);
    }

    /**
     * 音量转换
     *
     * @param streamType      需要转换的音量类型
     * @param sinkMinVolume   转换后的最低音量
     * @param sinkMaxVolume   转换后的最高音量
     * @param sourceMinVolume 转换前的最低音量
     * @param sourceMaxVolume 转换前的最高音量
     * @param sourceVolume    需要转换的音量值
     * @return
     */
    public static float convertVolume(int streamType, float sinkMinVolume, float sinkMaxVolume, float sourceMinVolume, float sourceMaxVolume, float sourceVolume) {
        float maxVolume = sourceMaxVolume <= 0 ? com.blankj.utilcode.util.VolumeUtils.getMaxVolume(streamType) : sourceMaxVolume;
        float minVolume = sourceMinVolume <= 0 ? 0 : sourceMinVolume;
        float sinkDif = sinkMaxVolume - sinkMinVolume;
        float perVolume = sinkDif / (maxVolume - minVolume);
        return sinkMinVolume + perVolume * sourceVolume;
    }

    public static int getStreamType() {
        return streamType;
    }

    public void setStreamType(int streamType) {
        VolumeUtils.streamType = streamType;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void init(int streamType, @Nullable VolumeChangeListener volumeChangeListener) {
        setStreamType(streamType);
        setVolumeChangeListener(volumeChangeListener);
        registerC11VolumeObserver();
        //registerVolumeObserver();
        //registerVolumeReceiver();
    }

    public void release() {
        unRegisterVolumeReceiver();
        unRegisterVolumeObserver();
        unRegisterC11VolumeObserver();
    }

    @Nullable
    public VolumeChangeListener getVolumeChangeListener() {
        return mVolumeChangeListener;
    }

    public void setVolumeChangeListener(@Nullable VolumeChangeListener volumeChangeListener) {
        this.mVolumeChangeListener = volumeChangeListener;
    }

    private void registerVolumeReceiver() {
        mVolumeBroadcastReceiver = new VolumeBroadcastReceiver(this);
        Utils.getApp().registerReceiver(mVolumeBroadcastReceiver, new IntentFilter(VOLUME_CHANGED_ACTION));
    }

    private void unRegisterVolumeReceiver() {
        if (mVolumeBroadcastReceiver != null) {
            Utils.getApp().unregisterReceiver(mVolumeBroadcastReceiver);
            mVolumeBroadcastReceiver = null;
            mVolumeChangeListener = null;
        }
    }

    private void unRegisterC11VolumeObserver() {
        if (c11ContentObserver != null) {
            Utils.getApp().getContentResolver().unregisterContentObserver(c11ContentObserver);
            c11ContentObserver = null;
            mVolumeChangeListener = null;
        }
    }

    private void registerVolumeObserver() {
        contentObserver = new SettingsContentObserver(this);
        Utils.getApp().getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true, contentObserver);
    }

    private void unRegisterVolumeObserver() {
        if (contentObserver != null) {
            Utils.getApp().getContentResolver().unregisterContentObserver(contentObserver);
            contentObserver = null;
            mVolumeChangeListener = null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void registerC11VolumeObserver() {
        c11ContentObserver = new C11SettingsContentObserver(this);
        Utils.getApp().getContentResolver().registerContentObserver(Settings.Global.getUriFor(VOLUME_GLOBAL_KEY_NAME), true, c11ContentObserver);
    }

    public interface VolumeChangeListener {
        /**
         * 系统媒体音量变化
         *
         * @param volume
         */
        void onVolumeChanged(int volume);
    }

    private static class VolumeBroadcastReceiver extends BroadcastReceiver {
        private final WeakReference<VolumeUtils> mObserverWeakReference;

        public VolumeBroadcastReceiver(VolumeUtils volumeChangeObserver) {
            mObserverWeakReference = new WeakReference<>(volumeChangeObserver);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            //媒体音量改变才通知
            VolumeUtils volumeUtils = mObserverWeakReference.get();
            if (VOLUME_CHANGED_ACTION.equals(intent.getAction()) && (intent.getIntExtra(EXTRA_VOLUME_STREAM_TYPE, -1) == getStreamType())) {
                if (volumeUtils != null) {
                    VolumeChangeListener listener = volumeUtils.getVolumeChangeListener();
                    if (listener != null) {
                        int volume = com.blankj.utilcode.util.VolumeUtils.getVolume(getStreamType());
                        if (volume >= 0) {
                            listener.onVolumeChanged(volume);
                        }
                    }
                }
            }
        }
    }

    private static class SingleInstanceHolder {
        private static final VolumeUtils INSTANCE = new VolumeUtils();
    }

    static class SettingsContentObserver extends ContentObserver {
        private final WeakReference<VolumeUtils> mObserverWeakReference;

        public SettingsContentObserver(VolumeUtils volumeChangeObserver) {
            super(new Handler());
            mObserverWeakReference = new WeakReference<>(volumeChangeObserver);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            VolumeUtils volumeUtils = mObserverWeakReference.get();
            if (volumeUtils != null) {
                VolumeChangeListener listener = volumeUtils.getVolumeChangeListener();
                if (listener != null) {
                    int volume = com.blankj.utilcode.util.VolumeUtils.getVolume(getStreamType());
                    if (volume >= 0) {
                        listener.onVolumeChanged(volume);
                    }
                }
            }
        }
    }

    static class C11SettingsContentObserver extends ContentObserver {
        private final WeakReference<VolumeUtils> mObserverWeakReference;

        public C11SettingsContentObserver(VolumeUtils volumeChangeObserver) {
            super(new Handler());
            mObserverWeakReference = new WeakReference<>(volumeChangeObserver);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            VolumeUtils volumeUtils = mObserverWeakReference.get();
            if (volumeUtils != null) {
                VolumeChangeListener listener = volumeUtils.getVolumeChangeListener();
                if (listener != null) {
                    try {
                        int c11Volume = Settings.Global.getInt(Utils.getApp().getContentResolver(), VOLUME_GLOBAL_KEY_NAME);
                        if (c11Volume >= MIN_VOLUME_C11) {
                            listener.onVolumeChanged(c11Volume);
                        }
                    } catch (Settings.SettingNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
