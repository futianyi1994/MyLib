package com.bracks.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bracks.utils.R;


/**
 * good programmer.
 *
 * @date : 2021-08-28 15:03
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class MaxLimitRecyclerView extends RecyclerView {
    private int mMaxHeight = -1;
    private int mMaxWidth = -1;

    public MaxLimitRecyclerView(Context context) {
        this(context, null);
    }

    public MaxLimitRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaxLimitRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inti(attrs);
    }

    private void inti(AttributeSet attrs) {
        if (getContext() != null && attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MaxLimitRecyclerView);
            if (typedArray.hasValue(R.styleable.MaxLimitRecyclerView_limit_maxHeight)) {
                mMaxHeight = typedArray.getLayoutDimension(R.styleable.MaxLimitRecyclerView_limit_maxHeight, -1);
            }
            if (typedArray.hasValue(R.styleable.MaxLimitRecyclerView_limit_maxWidth)) {
                mMaxWidth = typedArray.getLayoutDimension(R.styleable.MaxLimitRecyclerView_limit_maxWidth, -1);
            }
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        if (mMaxHeight >= 0 || mMaxWidth >= 0) {
            if (mMaxHeight > 0) {
                heightSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST);
            }
            if (mMaxWidth > 0) {
                widthSpec = MeasureSpec.makeMeasureSpec(mMaxWidth, MeasureSpec.AT_MOST);
            }
        }
        super.onMeasure(widthSpec, heightSpec);
    }

}
