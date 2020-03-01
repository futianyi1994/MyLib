package com.bracks.utils.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.bracks.utils.util.ArithUtils;

/**
 * good programmer.
 *
 * @date : 2020/2/29 22:03
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :自定义带跟随文字的进度条
 */
public class TextProgressBar extends ProgressBar {

    private String text;
    private Paint mPaint;
    private boolean isShowText;

    public TextProgressBar(Context context) {
        this(context, null);
    }

    public TextProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initText();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isShowText) {
            Rect rect = new Rect();
            this.mPaint.getTextBounds(this.text, 0, this.text.length(), rect);
            rect.right = 53;
            rect.bottom = 33;
            this.mPaint.setTextSize(24);

            //计算比例所占宽度
            double x = ArithUtils.div(getProgress(), 100) * getWidth() - Math.abs(rect.right) - 5;
            double y = ArithUtils.div(getHeight(), 2) + Math.abs(rect.exactCenterY() - 5);
            canvas.drawText(this.text, (float) x, (float) y, this.mPaint);
        }
    }


    @Override
    public void setProgress(int progress) {
        setText(progress);
        super.setProgress(progress);

    }

    /**
     * 设置是否显示跟随的文字
     *
     * @param b
     */
    public void setProgressText(boolean b) {
        isShowText = b;
    }

    /**
     * 初始化，画笔
     */
    private void initText() {
        this.mPaint = new Paint();
        this.mPaint.setColor(Color.WHITE);
    }

    private void setText() {
        setText(this.getProgress());
    }

    /**
     * 设置文字内容
     *
     * @param progress
     */
    public void setText(int progress) {
        int i = (progress * 100) / this.getMax();
        this.text = i + "%";
    }

    /**
     * 设置文字内容
     *
     * @param progress
     */
    public void setText(String progress) {
        this.text = progress;
        invalidate();
    }
}
