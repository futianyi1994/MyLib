package com.bracks.futia.mylib.base.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * good programmer.
 *
 * @data : 2018-01-10 下午 01:51
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewAdapter.BaseViewHolder> {
    public static final int HEAD_TYPE = 0;
    public static final int BODY_TYPE = 1;
    public static final int FOOT_TYPE = 2;
    private Context context;
    private List<T> realDatas;
    private OnItemClickListener mClickListener;
    private OnItemLongClickListener mCLickLongListener;


    protected Context getContext() {
        return context;
    }

    public BaseRecyclerViewAdapter(Context context) {
        this.context = context;
        realDatas = new ArrayList<>();
    }

    /**
     * 设置数据
     *
     * @param realDatas
     * @return
     */
    public BaseRecyclerViewAdapter setData(List<T> realDatas) {
        this.realDatas = realDatas;
        notifyDataSetChanged();
        return this;
    }

    /**
     * 得到数据
     *
     * @return
     */
    public List<T> getData() {
        return realDatas;
    }

    /**
     * 添加更多数据
     */
    public BaseRecyclerViewAdapter addAll(List<T> realDatas) {
        this.realDatas.addAll(realDatas);
        notifyDataSetChanged();
        return this;
    }

    /**
     * 清空数据源
     */
    public void clear() {
        this.realDatas.clear();
        notifyDataSetChanged();
    }

    /**
     * 绑定布局界面
     */
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //LayoutInflater inflater = LayoutInflater.from(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(inflaterItemLayout(viewType), parent, false);
        return new BaseViewHolder(itemView, viewType);
    }

    /**
     * 往控件中填充数据
     */
    @Override
    public void onBindViewHolder(BaseRecyclerViewAdapter.BaseViewHolder holder, int position) {
        bindData(holder, position, realDatas.get(position));
        //在这里设置Item的点击事件
        if (mClickListener == null) {
            mClickListener = new OnItemClickListener<T>() {
                @Override
                public void onItemClick(View itemView, int position, T t) {
                    //让子类去实现
                    onItemClickListener(itemView, position, t);
                }
            };
        }
        holder.itemView.setOnClickListener(new TimmyItemClickListener(position));
        if (mCLickLongListener == null) {
            mCLickLongListener = new OnItemLongClickListener<T>() {
                @Override
                public void onItemLongClick(View itemView, int position, T t) {
                    onItemLongClickListener(itemView, position, t);
                }
            };
        }
        holder.itemView.setOnLongClickListener(new TimmyItemLongClickListener(position));
    }


    /**
     * 获取Item数量
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return realDatas == null ? 0 : realDatas.size();
    }


    private class TimmyItemClickListener implements View.OnClickListener {
        private int mPosition;

        public TimmyItemClickListener(int position) {
            this.mPosition = position;
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null && view != null) {
                mClickListener.onItemClick(view, mPosition, realDatas.get(mPosition));
            }
        }
    }


    private class TimmyItemLongClickListener implements View.OnLongClickListener {
        private int mPosition;

        public TimmyItemLongClickListener(int position) {
            this.mPosition = position;
        }

        @Override
        public boolean onLongClick(View view) {
            if (mCLickLongListener != null && view != null) {
                mCLickLongListener.onItemLongClick(view, mPosition, realDatas.get(mPosition));
                return true;
            }
            return false;
        }
    }

    /**
     * Item的点击事件接口
     */
    public interface OnItemClickListener<T> {
        void onItemClick(View itemView, int position, T t);
    }

    public interface OnItemLongClickListener<T> {
        void onItemLongClick(View itemView, int position, T t);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mCLickLongListener = listener;
    }

    /**
     * 交给子类自己去填充Item布局
     */
    protected abstract int inflaterItemLayout(int viewType);

    protected abstract void bindData(BaseViewHolder holder, int position, T t);

    protected abstract void onItemClickListener(View itemView, int position, T t);

    private void onItemLongClickListener(View itemView, int position, T t) {

    }

    /**
     * ViewHolder只做View的缓存,不关心数据内容
     * 其中的方法可以自己随意扩展
     */
    public class BaseViewHolder extends RecyclerView.ViewHolder {

        /**
         * 创建View容器,根据key为控件id
         */
        private SparseArray<View> viewArray;
        private int viewType;

        public BaseViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            viewArray = new SparseArray<>();
        }

        /**
         * 获取布局中的View
         */
        public <T extends View> T findViewById(@IdRes int viewId) {
            View view = viewArray.get(viewId);
            if (view == null) {
                view = itemView.findViewById(viewId);
                viewArray.put(viewId, view);
            }
            return (T) view;
        }

        public Context getContext() {
            return itemView.getContext();
        }

        public View getView(int viewId) {
            return findViewById(viewId);
        }

        public TextView getTextView(int resId) {
            return (TextView) getView(resId);
        }

        public void setText(int resId, String str) {
            getTextView(resId).setText(str);
        }

        public void setImage(int resId, String url) {
            Glide.with(context).load(url).into((ImageView) getView(resId));
        }

        public void setViewTyep(int viewType) {
            this.viewType = viewType;
        }

        public int getViewType() {
            return viewType;
        }
    }
}
