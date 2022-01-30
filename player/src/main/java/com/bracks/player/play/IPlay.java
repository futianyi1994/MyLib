package com.bracks.player.play;

public interface IPlay {
    /**
     * @param isRequestAudioFocus 播放是否请求焦点
     */
    void onPlay(boolean isRequestAudioFocus);

    /**
     * @param isClearUrl 是否清除播放地址
     */
    void onStop(boolean isClearUrl);

    /**
     * @param isReleaseAudioFocus 暂停是否释放焦点
     */
    void onPause(boolean isReleaseAudioFocus);

    void downVolume();

    void upVolume();
}
