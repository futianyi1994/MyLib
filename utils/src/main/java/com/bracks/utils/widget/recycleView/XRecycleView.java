package com.bracks.utils.widget.recycleView;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * good programmer.
 *
 * @data : 2018-03-29 上午 09:09
 * @author: futia
 * @email : futianyi1994@126.com
 * @description : 添加快速滚动停止加载图片
 */
public class XRecycleView extends RecyclerView {
    public XRecycleView(Context context) {
        this(context, null);
    }

    public XRecycleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        addOnScrollListener(new ImageAutoLoadScrollistener());
    }


    /**
     * Glide
     */
    public class ImageAutoLoadScrollistener extends OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            switch (newState) {
                case SCROLL_STATE_IDLE:
                    if (getContext() != null) {
                        //Clide.with(getContext()).resumeRequests();
                    }
                    break;
                case SCROLL_STATE_DRAGGING:
                    if (getContext() != null) {
                        //Clide.with(getContext()).pauseRequests();
                    }
                    break;
                case SCROLL_STATE_SETTLING:
                    if (getContext() != null) {
                        //Clide.with(getContext()).pauseRequests();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
