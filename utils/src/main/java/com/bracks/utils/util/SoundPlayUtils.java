package com.bracks.utils.util;


import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;

import com.blankj.utilcode.util.Utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * good programmer.
 *
 * @date : 2019/11/2 15:43
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class SoundPlayUtils {
    public final static int TYPE_MUSIC = AudioManager.STREAM_MUSIC;
    public final static int TYPE_ALARM = AudioManager.STREAM_ALARM;
    public final static int TYPE_RING = AudioManager.STREAM_RING;
    private Context context;
    private SoundPool soundPool;
    private int maxStream;
    private int[] resIds;
    private String[] ringtonePaths;
    private String[] ringtoneNames;
    private float leftVolume, rightVolume;
    private int priority;
    private int loop;
    private float rate;
    private Map<String, Integer> ringtoneIds;
    private boolean isLoaded;
    private MyHandler handler;
    private @TYPE
    int streamType;
    private SoundPlayUtils(Builder builder) {
        context = builder.context;
        maxStream = builder.maxStream;
        resIds = builder.resIds;
        ringtonePaths = builder.ringtonePaths;
        ringtoneNames = builder.ringtoneNames;
        leftVolume = builder.leftVolume;
        rightVolume = builder.rightVolume;
        priority = builder.priority;
        loop = builder.loop;
        rate = builder.rate;
        streamType = builder.streamType;
        init();
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder().setMaxStreams(maxStream).build();
        } else {
            soundPool = new SoundPool(maxStream, streamType, 1);
        }
        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> isLoaded = true);
        ringtoneIds = new HashMap<>();
        for (int i = 0; i < ringtoneNames.length; i++) {
            if (resIds[i] != 0) {
                ringtoneIds.put(ringtoneNames[i], soundPool.load(context, resIds[i], 1));
            } else if (ringtonePaths[i] != null) {
                ringtoneIds.put(ringtoneNames[i], soundPool.load(ringtonePaths[i], 1));
            }
        }
        HandlerThread mHandlerThread = new HandlerThread("handler-thread");
        mHandlerThread.start();
        handler = new MyHandler(this, mHandlerThread.getLooper());
    }

    public SoundPool getSoundPool() {
        return soundPool;
    }

    public void playRadom() {
        playRadom(false);
    }

    public void playRadom(boolean isLoop) {
        play(ringtoneNames[new Random().nextInt(ringtoneNames.length)], isLoop);
    }

    public void play(@NonNull String ringtoneName) {
        play(ringtoneName, false);
    }

    /**
     * int play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate) ：
     * 1)该方法的第一个参数指定播放哪个声音；
     * 2) leftVolume 、
     * 3) rightVolume 指定左、右的音量：
     * 4) priority 指定播放声音的优先级，数值越大，优先级越高；
     * 5) loop 指定是否循环， 0 为不循环， -1 为循环；
     * 6) rate 指定播放的比率，数值可从 0.5 到 2 ， 1 为正常比率。
     * <p>
     * 注意不要在load后马上play，load需要一点点时间
     *
     * @param ringtoneName 自定义铃声名称
     * @param isLoop       指定是否循环， 0 为不循环， -1 为循环；
     */
    public void play(@NonNull String ringtoneName, boolean isLoop) {
        if (isLoaded) {
            handler.removeCallbacksAndMessages(null);
            if (ringtoneIds.containsKey(ringtoneName)) {
                soundPool.play(ringtoneIds.get(ringtoneName), leftVolume, rightVolume, priority, isLoop ? -1 : loop, rate);
            }
        } else {
            Message msg = handler.obtainMessage();
            msg.what = 0;
            Bundle bundle = new Bundle();
            bundle.putBoolean("isLoop", isLoop);
            bundle.putString("ringtoneName", ringtoneName);
            msg.setData(bundle);
            handler.sendMessageDelayed(msg, 50);
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @IntDef({TYPE_MUSIC, TYPE_ALARM, TYPE_RING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TYPE {
    }

    private static class MyHandler extends Handler {
        private WeakReference<SoundPlayUtils> weakReference;
        private SoundPlayUtils playUtils;

        private MyHandler(SoundPlayUtils mActivity, Looper looper) {
            super(looper);
            weakReference = new WeakReference<>(mActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            playUtils = weakReference.get();
            if (weakReference == null || playUtils == null) {
                return;
            }
            if (msg.what == 0) {
                Bundle bundle = msg.getData();
                String ringtoneName = bundle.getString("ringtoneName");
                boolean isLoop = bundle.getBoolean("isLoop");
                playUtils.play(ringtoneName, isLoop);
            }
            super.handleMessage(msg);
        }
    }

    public static class Builder {
        private Context context;
        private int maxStream;
        private int temMaxStream;
        private int[] resIds;
        private String[] ringtonePaths;
        private String[] ringtoneNames;
        private float leftVolume = 1.0f, rightVolume = 1.0f;
        private int priority = 1;
        private int loop;
        private float rate = 1;
        private @TYPE
        int streamType;

        public Builder() {
            this(Utils.getApp(), 1);
        }

        public Builder(Context context, int maxStream) {
            this.context = context;
            this.maxStream = this.temMaxStream = maxStream;
            streamType = TYPE_ALARM;
            resIds = new int[maxStream];
            ringtonePaths = new String[maxStream];
            ringtoneNames = new String[maxStream];
        }

        public Builder setStreamType(@TYPE int streamType) {
            this.streamType = streamType;
            return this;
        }

        public Builder setPriority(int priority) {
            this.priority = priority;
            return this;
        }

        public Builder setLoop(int loop) {
            this.loop = loop;
            return this;
        }

        public Builder setRate(float rate) {
            this.rate = rate;
            return this;
        }

        public Builder setVolume(@FloatRange(from = 0.0f, to = 1.0f) float volume) {
            this.leftVolume = this.rightVolume = volume;
            return this;
        }

        public Builder setLeftVolume(@FloatRange(from = 0.0f, to = 1.0f) float leftVolume) {
            this.leftVolume = leftVolume;
            return this;
        }

        public Builder setRightVolume(@FloatRange(from = 0.0f, to = 1.0f) float rightVolume) {
            this.rightVolume = rightVolume;
            return this;
        }

        /**
         * 加载音频资源
         *
         * @param resId 资源ID
         * @return this
         */
        public Builder load(@NonNull String ringtoneName, @RawRes int resId) {
            if (temMaxStream == 0) {
                return this;
            }
            resIds[maxStream - temMaxStream] = resId;
            ringtoneNames[maxStream - temMaxStream] = ringtoneName;
            temMaxStream--;
            return this;
        }

        /**
         * 加载铃声
         *
         * @param ringtoneName 自定义铃声名称
         * @param ringtonePath 铃声路径
         * @return this
         */
        public Builder load(@NonNull String ringtoneName, @NonNull String ringtonePath) {
            if (temMaxStream == 0) {
                return this;
            }
            ringtonePaths[maxStream - temMaxStream] = ringtonePath;
            ringtoneNames[maxStream - temMaxStream] = ringtoneName;
            temMaxStream--;
            return this;
        }

        public SoundPlayUtils build() {
            return new SoundPlayUtils(this);
        }
    }
}