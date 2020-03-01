package com.bracks.utils.util.widget;

import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.Gravity;
import android.widget.ProgressBar;

import com.bracks.utils.R;


/**
 * good programmer.
 *
 * @date : 2019-07-10 下午 04:04
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class ProgressUtils {

    /**
     * 修改ProgressBar背景色
     *
     * @param progressBar
     * @param progress
     * @param color
     */
    public static void setProgressBg(ProgressBar progressBar, int progress, int color) {
        ColorDrawable colorDrawable = new ColorDrawable();
        colorDrawable.setColor(color);
        ClipDrawable clipDrawable = new ClipDrawable(colorDrawable, Gravity.START, ClipDrawable.HORIZONTAL);

        Drawable drawable = progressBar.getResources().getDrawable(R.color.transparent);
        /*Drawable[] layers = new Drawable[]{drawable, clipDrawable, clipDrawable};
        LayerDrawable layerDrawable = new LayerDrawable(layers);*/
        LayerDrawable layerDrawable = (LayerDrawable) progressBar.getProgressDrawable();
        layerDrawable.setDrawableByLayerId(android.R.id.background, drawable);
        layerDrawable.setDrawableByLayerId(android.R.id.progress, clipDrawable);
        layerDrawable.setDrawableByLayerId(android.R.id.secondaryProgress, clipDrawable);
        progressBar.setProgressDrawable(layerDrawable);

        progressBar.setProgress(0);
        progressBar.setProgress(progress);
    }
}
