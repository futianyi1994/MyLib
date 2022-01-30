package com.bracks.player.play;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.HttpCookie;
import java.util.List;
import java.util.Map;

/**
 * good programmer.
 *
 * @date : 2020/5/13 17:21
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class AudioPlayer extends MediaPlayer implements EffectUtils.AnimatorUpdateListener {
    public static final int STATE_IDLE = 0;
    public static final int STATE_INITIALIZED = 1;
    public static final int STATE_PREPARING = 2;
    public static final int STATE_PREPARED = 3;
    public static final int STATE_STARTED = 4;
    public static final int STATE_PAUSED = 5;
    public static final int STATE_STOPED = 6;
    public static final int STATE_END = 7;
    private static final String TAG = "AudioPlayer";
    @State
    public int mediaPlayerState = STATE_IDLE;
    private OnPreparedListener mOnPreparedListener;
    private OnCompletionListener mOnCompletionListener;
    private OnSeekCompleteListener mOnSeekCompleteListener;
    private OnErrorListener mOnErrorListener;
    private OnInfoListener mOnInfoListener;
    private OnBufferingUpdateListener mOnBufferingUpdateListener;
    private boolean isFadeIn = true;

    public AudioPlayer() {
        super();
        initListener();
        Log.d(TAG, "init audioPlayer");
    }

    @State
    public int getMediaPlayerState() {
        return mediaPlayerState;
    }

    @Override
    public void setDataSource(@NonNull Context context, @NonNull Uri uri, @Nullable Map<String, String> headers, @Nullable List<HttpCookie> cookies) throws IOException {
        super.setDataSource(context, uri, headers, cookies);
        mediaPlayerState = STATE_INITIALIZED;
    }

    @Override
    public void setDataSource(String path) throws IOException, IllegalArgumentException, IllegalStateException, SecurityException {
        super.setDataSource(path);
        mediaPlayerState = STATE_INITIALIZED;
    }

    @Override
    public void setDataSource(@NonNull AssetFileDescriptor afd) throws IOException, IllegalArgumentException, IllegalStateException {
        super.setDataSource(afd);
        mediaPlayerState = STATE_INITIALIZED;
    }

    @Override
    public void setDataSource(FileDescriptor fd, long offset, long length) throws IOException, IllegalArgumentException, IllegalStateException {
        super.setDataSource(fd, offset, length);
        mediaPlayerState = STATE_INITIALIZED;
    }

    @Override
    public void setDataSource(MediaDataSource dataSource) throws IllegalArgumentException, IllegalStateException {
        super.setDataSource(dataSource);
        mediaPlayerState = STATE_INITIALIZED;
    }

    @Override
    public void prepare() throws IOException, IllegalStateException {
        try {
            super.prepare();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        mediaPlayerState = STATE_PREPARED;
    }

    @Override
    public void prepareAsync() throws IllegalStateException {
        mediaPlayerState = STATE_PREPARING;
        try {
            super.prepareAsync();
        } catch (IllegalStateException e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void start() throws IllegalStateException {
        try {
            if (isFadeIn) {
                EffectUtils.fadeIn(this);
            }
            isFadeIn = true;
            super.start();
        } catch (IllegalStateException e) {
            Log.e(TAG, e.toString());
        }
        mediaPlayerState = STATE_STARTED;
    }

    @Override
    public void stop() throws IllegalStateException {
        try {
            EffectUtils.remove(this);
            super.stop();
        } catch (IllegalStateException e) {
            Log.e(TAG, e.toString());
        }
        mediaPlayerState = STATE_STOPED;
    }

    @Override
    public void pause() throws IllegalStateException {
        try {
            EffectUtils.remove(this);
            super.pause();
        } catch (IllegalStateException e) {
            Log.e(TAG, e.toString());
        }
        mediaPlayerState = STATE_PAUSED;
    }

    @Override
    public void reset() {
        EffectUtils.remove(this);
        super.reset();
        mediaPlayerState = STATE_IDLE;
    }

    @Override
    public void release() {
        EffectUtils.remove(this);
        super.release();
        mediaPlayerState = STATE_END;
        setOnInfoListener(null);
        setOnSeekCompleteListener(null);
        setOnErrorListener(null);
        setOnCompletionListener(null);
        setOnBufferingUpdateListener(null);
        setOnPreparedListener(null);

        setInfoListener(null);
        setSeekCompleteListener(null);
        setErrorListener(null);
        setCompletionListener(null);
        setBufferingUpdateListener(null);
        setPreparedListener(null);

    }

    @Override
    public boolean isPlaying() {
        if (mediaPlayerState >= AudioPlayer.STATE_INITIALIZED && mediaPlayerState <= AudioPlayer.STATE_STOPED) {
            return super.isPlaying();
        } else {
            Log.e(TAG, "isPlaying error mediaPlayerState is : " + mediaPlayerState);
            return false;
        }
    }

    @Override
    public int getCurrentPosition() {
        if (canGetDuration()) {
            return super.getCurrentPosition();
        } else {
            Log.e(TAG, "getCurrentPosition error mediaPlayerState is : " + mediaPlayerState);
            return 0;
        }
    }

    @Override
    public int getDuration() {
        if (canGetDuration()) {
            return super.getDuration();
        } else {
            Log.e(TAG, "getDuration error mediaPlayerState is : " + mediaPlayerState);
            return 0;
        }
    }

    @Override
    public void seekTo(int msec) throws IllegalStateException {
        if (canSeekTo()) {
            try {
                super.seekTo(msec);
            } catch (IllegalStateException e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Log.e(TAG, "seekTo error mediaPlayerState is : " + mediaPlayerState);
        }
    }

    @Override
    public void onAnimationUpdate(float value) {
        setVolume(value, value);
    }

    private void initListener() {
        setOnCompletionListener(var1 -> {
            Log.v(TAG, "onCompletion");
            if (mOnCompletionListener != null) {
                mOnCompletionListener.onCompletion(AudioPlayer.this);
            }
        });
        setOnErrorListener((var1, var2, var3) -> {
            //返回false会触发OnCompletion；true不会触发
            Log.e(TAG, "onErr var2 =" + var2 + "|| var3=" + var3 + "|| mediaPlayerState = " + mediaPlayerState);
            return mOnErrorListener == null || mOnErrorListener.onError(this, var2, var3);
        });
        setOnSeekCompleteListener(var1 -> {
            Log.v(TAG, "onSeekComplete ,duration:" + var1.getDuration() + ", position:" + var1.getCurrentPosition());
            if (mOnSeekCompleteListener != null) {
                mOnSeekCompleteListener.onSeekComplete(this);
            }

        });
        setOnBufferingUpdateListener((var1, var2) -> {
            Log.v(TAG, "onBufferingUpdate duration = " + var2);
            if (mOnBufferingUpdateListener != null) {
                mOnBufferingUpdateListener.onBufferingUpdate(var1, var2);
            }

        });
        setOnPreparedListener(mp -> {
            Log.v(TAG, "onPrepared");
            mediaPlayerState = STATE_PREPARED;
            if (mOnPreparedListener != null) {
                mOnPreparedListener.onPrepared(AudioPlayer.this);
            }
        });
        setOnInfoListener((mp, what, extra) -> {
            Log.v(TAG, "onInfo");
            return mOnInfoListener != null && mOnInfoListener.onInfo(this, what, extra);
        });
    }

    public void setBufferingUpdateListener(OnBufferingUpdateListener var1) {
        this.mOnBufferingUpdateListener = var1;
    }

    public void setPreparedListener(OnPreparedListener var1) {
        this.mOnPreparedListener = var1;
    }

    public void setCompletionListener(OnCompletionListener var1) {
        this.mOnCompletionListener = var1;
    }

    public void setSeekCompleteListener(OnSeekCompleteListener var1) {
        this.mOnSeekCompleteListener = var1;
    }

    public void setErrorListener(OnErrorListener var1) {
        this.mOnErrorListener = var1;
    }

    public void setInfoListener(OnInfoListener var1) {
        this.mOnInfoListener = var1;
    }

    public boolean canGetDuration() {
        return mediaPlayerState >= AudioPlayer.STATE_PREPARED && mediaPlayerState <= AudioPlayer.STATE_PAUSED;
    }

    public boolean canSeekTo() {
        return mediaPlayerState >= AudioPlayer.STATE_PREPARED && mediaPlayerState <= AudioPlayer.STATE_PAUSED;
    }

    public void setFadeIn(boolean fadeIn) {
        isFadeIn = fadeIn;
    }

    @IntDef({STATE_IDLE, STATE_INITIALIZED, STATE_PREPARING, STATE_PREPARED, STATE_STARTED, STATE_STOPED, STATE_PAUSED, STATE_END})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }

    public interface OnBufferingUpdateListener {
        void onBufferingUpdate(MediaPlayer var1, int var2);
    }

    public interface OnSeekCompleteListener {
        void onSeekComplete(AudioPlayer var1);
    }

    public interface OnPreparedListener {
        void onPrepared(AudioPlayer var1);
    }

    public interface OnInfoListener {
        boolean onInfo(AudioPlayer var1, int var2, int var3);
    }

    public interface OnErrorListener {
        boolean onError(AudioPlayer var1, int var2, int var3);
    }

    public interface OnCompletionListener {
        void onCompletion(AudioPlayer var1);
    }
}
