package com.bracks.wanandroid.utils;


import android.content.Context;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;

import com.blankj.utilcode.util.Utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
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

    @IntDef({TYPE_MUSIC, TYPE_ALARM, TYPE_RING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TYPE {
    }

    public final static int RING_TYPE_MUSIC = RingtoneManager.TYPE_ALARM;
    public final static int RING_TYPE_ALARM = RingtoneManager.TYPE_NOTIFICATION;
    public final static int RING_TYPE_RING = RingtoneManager.TYPE_RINGTONE;

    @IntDef({RING_TYPE_MUSIC, RING_TYPE_ALARM, RING_TYPE_RING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RING_TYPE {
    }

    private Context context;
    private SoundPool soundPool;
    private int maxStream;
    private int temMaxStream;
    private Map<String, Integer> ringtoneIds;
    private String[] ringtoneNames;


    public SoundPlayUtils() {
        this(1);
    }

    public SoundPlayUtils(int maxStream) {
        this(Utils.getApp(), maxStream);
    }

    public SoundPlayUtils(Context context, int maxStream) {
        this(context, maxStream, TYPE_ALARM);
    }

    public SoundPlayUtils(Context context, int maxStream, @TYPE int streamType) {
        this.context = context;
        this.maxStream = this.temMaxStream = maxStream;
        ringtoneIds = new HashMap<>();
        ringtoneNames = new String[maxStream];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder().setMaxStreams(maxStream).build();
        } else {
            soundPool = new SoundPool(maxStream, streamType, 1);
        }
    }

    /**
     * 加载音频资源
     *
     * @param resId 资源ID
     * @return this
     */
    public SoundPlayUtils load(@NonNull String ringtoneName, @RawRes int resId) {
        if (maxStream == 0) {
            return this;
        }
        ringtoneNames[temMaxStream - maxStream] = ringtoneName;
        maxStream--;
        ringtoneIds.put(ringtoneName, soundPool.load(context, resId, 1));
        return this;
    }

    /**
     * 加载铃声
     *
     * @param ringtoneName 自定义铃声名称
     * @param ringtonePath 铃声路径
     * @return this
     */
    public SoundPlayUtils load(@NonNull String ringtoneName, @NonNull String ringtonePath) {
        if (maxStream == 0) {
            return this;
        }
        ringtoneNames[temMaxStream - maxStream] = ringtoneName;
        maxStream--;
        ringtoneIds.put(ringtoneName, soundPool.load(ringtonePath, 1));
        return this;
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
        if (ringtoneIds.containsKey(ringtoneName)) {
            soundPool.play(ringtoneIds.get(ringtoneName), 1, 1, 1, isLoop ? -1 : 0, 1);
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        if (soundPool != null) {
            soundPool.release();
        }
    }
}
