package com.bracks.utils.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

import androidx.annotation.RequiresApi;

/**
 * Good programmer.
 * Created by futia on 2017-11-17 下午 05:21.
 * Email:futianyi1994@126.com
 * Description:当ListView外面有ScrollView时, ListView只显示一行Item的高度,为使ListView跟所有Item高度一致使用这个ListView替换就可以.
 */
public class NoScrollExListView extends ExpandableListView {
    public NoScrollExListView(Context context) {
        super(context);
    }

    public NoScrollExListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollExListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NoScrollExListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    //不出现滚动条
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
