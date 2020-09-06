package com.bracks.utils.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.bracks.utils.R;


/**
 * good programmer.
 *
 * @date : 2018-06-28 16:31
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class FocusView extends View {

    private Paint mPaint;
    /**
     * 焦点中心点X
     */
    private float centerX;
    /**
     * 焦点中心点Y
     */
    private float centerY;
    /**
     * 焦点选框的最小半径
     */
    private float radius = 20;
    /**
     * 焦点选框的最大半径
     */
    private float maxRadius = 100;
    /**
     * 焦点选框的最大透明度
     */
    private int paintAlpha = 255;
    private int paintColor = Color.WHITE;
    /**
     * 焦点选框是否需要消失
     */
    private boolean isNeedDismiss = true;
    /**
     * 是否已经开始绘制的标志位。如果已经开始绘制，则拒绝下一次点击绘制请求
     */
    private boolean isStart = false;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                // update size
                case 0:
                    radius += 5;
                    // 若将要绘制的尺寸大于约束的最大尺寸,则将该尺寸还原
                    if (radius >= maxRadius) {
                        radius -= 5;
                        // 使选框停留1s后逐渐消失
                        mHandler.sendEmptyMessageDelayed(1, 1000);
                    } else {
                        mHandler.sendEmptyMessageDelayed(0, (long) (200f / radius));
                        invalidate(); // setNeedDisplay
                    }
                    break;
                // update alpha
                case 1:
                    // 当选框达到最大后,就不断改变其透明度，直至透明度为0，本次绘制过程结束
                    paintAlpha -= 20;
                    if (paintAlpha <= 0) {
                        isNeedDismiss = true;
                        invalidate();
                        isStart = false;
                    } else {
                        mPaint.setAlpha(paintAlpha);
                        invalidate();
                        mHandler.sendEmptyMessageDelayed(1, 15);
                    }
                    break;
                default:
                    break;
            }
        }
    };
    public FocusView(Context context) {
        super(context);
    }
    public FocusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // init paint
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FocusView);
        int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int id = typedArray.getIndex(i);
            if (id == R.styleable.FocusView_focusColor) {
                paintColor = typedArray.getColor(id, Color.WHITE);
            } else if (id == R.styleable.FocusView_focusDefaultRadius) {
                radius = typedArray.getFloat(id, 20);
            } else if (id == R.styleable.FocusView_focusMaxRadius) {
                maxRadius = typedArray.getFloat(id, 100);
            }
        }

        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        mPaint.setColor(paintColor);

    }

    /**
     * addGestureRecognizer UITapGestureRecognizer
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            // UIGestureRecognizerStateBegan
            case MotionEvent.ACTION_DOWN:
                if (!isStart) {
                    centerX = event.getX();
                    centerY = event.getY();
                    // 重量选框参数
                    isStart = true;
                    isNeedDismiss = false;
                    paintAlpha = 255;
                    mPaint.setAlpha(paintAlpha);
                    radius = 20;
                    invalidate();
                    mHandler.obtainMessage(0).sendToTarget();
                }
                break;
            // UIGestureRecognizerStateChange
            case MotionEvent.ACTION_MOVE:
                break;
            // UIGestureRecognizerStateEnd
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // CGSize
        //setMeasuredDimension(measuredWidth, measuredHeight);
    }

    /**
     * 设置焦点选框的初始半径
     *
     * @param radius
     */
    public void setFocusCircleRadius(float radius) {
        this.radius = radius;
    }

    /**
     * 设置焦点选框的最大半径
     *
     * @param radius
     */
    public void setFocusCircleMaxRadius(float radius) {
        this.maxRadius = radius;
    }

    /**
     * 设置焦点选框的颜色
     *
     * @param color
     */
    public void setFocusCircleColor(int color) {
        this.paintColor = color;
        mPaint.setColor(color);
    }

    /**
     * drawRect
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //若绘制过程结束,则清除焦点框
        if (!isNeedDismiss) {
            canvas.drawCircle(centerX, centerY, radius, mPaint);
        }
    }
}
