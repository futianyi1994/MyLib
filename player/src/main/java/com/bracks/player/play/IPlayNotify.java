package com.bracks.player.play;


import com.bracks.player.entity.ErrorEntity;

/**
 * good programmer.
 *
 * @date : 2020/5/13 18:07
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :与外部进行交互
 */
public interface IPlayNotify {
    /**
     * 缓冲进度
     *
     * @param bufferPosition
     */
    void onBufferedPosition(long bufferPosition);

    /**
     * 歌曲切换
     */
    default void onSwitched() {
    }

    /**
     * 开始播放
     *
     * @param isSwitchPlay 是否切歌播放
     */
    default void onPlayed(boolean isSwitchPlay) {
    }

    /**
     * 停止播放
     */
    default void onStoped() {
    }

    /**
     * 通知外部异常信息
     *
     * @param errorEntity
     */
    void onError(ErrorEntity errorEntity);

    /**
     * 通知外部播放状态
     *
     * @param state
     * @param position
     * @param duration
     */
    void onPlayState(int state, long position, long duration);

    /**
     * 通知外部播放完成
     */
    void onPlayComplete();

}
