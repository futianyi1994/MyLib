package com.bracks.player;

import com.blankj.utilcode.util.Utils;

import java.io.File;

/**
 * good programmer.
 *
 * @date : 2018/10/10 11:43
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public interface Constant {

    int MAX_CACHE_LRC_COUNT = 100;

    String KUGOU_LYCIC_PATH = Utils.getApp().getCacheDir().getPath() + File.separator + "lrc" + File.separator;
}
