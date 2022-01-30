package com.bracks.player.play;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;

import com.blankj.utilcode.util.Utils;

/**
 * good programmer.
 *
 * @date : 2020/5/13 17:13
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public abstract class AudioFocus implements IPlay, AudioManager.OnAudioFocusChangeListener {
    private final AudioManager mAudioManager;
    private String TAG;
    private int mFocusState = AudioManager.AUDIOFOCUS_REQUEST_FAILED;
    private int focusChange = AudioManager.AUDIOFOCUS_NONE;
    private AudioFocusRequest mRequest;

    public AudioFocus() {
        this("AudioFocus");
    }

    protected AudioFocus(String tag) {
        TAG = tag;
        mAudioManager = (AudioManager) Utils.getApp().getSystemService(Context.AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes mAttributes = new AudioAttributes
                    .Builder()
                    .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                    .build();
            mRequest = new AudioFocusRequest
                    .Builder(AudioManager.AUDIOFOCUS_GAIN)
                    //关闭8.0延迟获取焦点的功能
                    .setAcceptsDelayedFocusGain(false)
                    .setOnAudioFocusChangeListener(this)
                    .setAudioAttributes(mAttributes)
                    //关闭8.0情况下系统自动降低音量的功能，如果使用了AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK这个焦点，
                    .setWillPauseWhenDucked(true)
                    .build();
        }
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    protected boolean requestFocus() {
        if (mFocusState != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mFocusState = mAudioManager.requestAudioFocus(mRequest);
            } else {
                mFocusState = mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            }
            if (mFocusState == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                Log.i(TAG, "request focus success");
            } else {
                Log.e(TAG, "request focus failed");
            }
        } else {
            Log.i(TAG, "already request focus");
        }
        return mFocusState == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    protected void releaseFocus() {
        if (mFocusState != AudioManager.AUDIOFOCUS_REQUEST_FAILED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mAudioManager.abandonAudioFocusRequest(mRequest);
            } else {
                mAudioManager.abandonAudioFocus(this);
            }
        }
        mFocusState = AudioManager.AUDIOFOCUS_REQUEST_FAILED;
        focusChange = AudioManager.AUDIOFOCUS_NONE;
        Log.i(TAG, "releaseFocus");
    }

    protected boolean hasFocus() {
        Log.d(TAG, "hasFocus: mFocusState : " + mFocusState);
        return mFocusState == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    protected int getFocusChange() {
        Log.d(TAG, "getFocusChange : " + focusChange);
        return focusChange;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        Log.d(TAG, "focusChange : " + focusChange);
        this.focusChange = focusChange;
        switch (focusChange) {
            //当其他应用申请焦点之后又释放焦点会触发此回调
            case AudioManager.AUDIOFOCUS_GAIN:
                upVolume();
                onPlay(true);
                break;
            //长时间丢失焦点,当其他应用申请的焦点为AUDIOFOCUS_GAIN时，会触发此回调事件，例如播放QQ音乐，网易云音乐等.通常需要暂停音乐播放，若没有暂停播放就会出现和其他音乐同时输出声音
            case AudioManager.AUDIOFOCUS_LOSS:
                onStop(false);
                break;
            //短暂性丢失焦点，当其他应用申请AUDIOFOCUS_GAIN_TRANSIENT或AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE时，会触发此回调事件，例如播放短视频，拨打电话等。通常需要暂停音乐播放
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                onPause(false);
                break;
            //短暂失去音频焦点，允许低音量播放
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                downVolume();
                break;
            default:
                break;
        }
    }
}
