package com.bracks.mylib.base.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewAdapter<T>.BaseViewHolder> {
    public static final int HEAD_TYPE = 0;
    public static final int BODY_TYPE = 1;
    public static final int FOOT_TYPE = 2;
    private final Context context;
    private List<T> realDatas;
    private OnItemClickListener<T> mClickListener;
    private OnItemLongClickListener<T> mCLickLongListener;

    public BaseRecyclerViewAdapter(Context context) {
        this.context = context;
        realDatas = new ArrayList<>();
    }

    protected Context getContext() {
        return context;
    }

    /**
     * 得到数据
     *
     * @return 数据
     */
    public List<T> getData() {
        return realDatas;
    }

    /**
     * 设置数据
     *
     * @param realDatas
     * @return BaseRecyclerViewAdapter
     */
    public BaseRecyclerViewAdapter setData(List<T> realDatas) {
        this.realDatas = realDatas;
        notifyDataSetChanged();
        return this;
    }

    /**
     * 添加更多数据
     *
     * @param realDatas
     * @return BaseRecyclerViewAdapter
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
     *
     * @param parent   parent
     * @param viewType Item布局类型
     * @return
     */
    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BaseViewHolder(LayoutInflater.from(context).inflate(inflaterItemLayout(viewType), parent, false), viewType);
    }

    /**
     * 往控件中填充数据
     *
     * @param holder   BaseViewHolder
     * @param position position
     */
    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerViewAdapter<T>.BaseViewHolder holder, int position) {
        bindData(holder, position, realDatas.size() == 0 ? null : realDatas.get(position));
        //在这里设置Item的点击事件
        if (mClickListener == null) {
            //让子类去实现
            mClickListener = this::onItemClickListener;
        }
        holder.itemView.setOnClickListener(new TimmyItemClickListener(position));
        if (mCLickLongListener == null) {
            mCLickLongListener = this::onItemLongClickListener;
        }
        holder.itemView.setOnLongClickListener(new TimmyItemLongClickListener(position));
    }

    /**
     * 获取Item数量
     *
     * @return itemCount
     */
    @Override
    public int getItemCount() {
        return realDatas == null ? 0 : realDatas.size();
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.mClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> listener) {
        this.mCLickLongListener = listener;
    }

    /**
     * 交给子类自己去填充Item布局
     *
     * @param viewType Item布局类型
     * @return
     */
    protected abstract int inflaterItemLayout(int viewType);

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     * @param t
     */
    protected abstract void bindData(BaseViewHolder holder, int position, T t);

    /**
     * Item的长按监听
     *
     * @param itemView
     * @param position
     * @param t
     */
    protected abstract void onItemClickListener(View itemView, int position, T t);

    protected void onItemLongClickListener(View itemView, int position, T t) {
    }

    public interface OnItemClickListener<T> {
        /**
         * Item的点击事件接口
         *
         * @param itemView
         * @param position
         * @param t
         */
        void onItemClick(View itemView, int position, T t);
    }

    public interface OnItemLongClickListener<T> {
        /**
         * Item的长按事件接口
         *
         * @param itemView
         * @param position
         * @param t
         */
        void onItemLongClick(View itemView, int position, T t);
    }

    private class TimmyItemClickListener implements View.OnClickListener {
        private final int mPosition;

        public TimmyItemClickListener(int position) {
            this.mPosition = position;
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null && view != null && mPosition < realDatas.size()) {
                mClickListener.onItemClick(view, mPosition, realDatas.get(mPosition));
            }
        }
    }

    private class TimmyItemLongClickListener implements View.OnLongClickListener {
        private final int mPosition;

        public TimmyItemLongClickListener(int position) {
            this.mPosition = position;
        }

        @Override
        public boolean onLongClick(View view) {
            if (mCLickLongListener != null && view != null && mPosition < realDatas.size()) {
                mCLickLongListener.onItemLongClick(view, mPosition, realDatas.get(mPosition));
                return true;
            }
            return false;
        }
    }

    /**
     * Created by futia on 2019-05-17 上午 11:26
     *
     * @Description: ViewHolder只做View的缓存, 不关心数据内容其中的方法可以自己随意扩展
     */
    public class BaseViewHolder extends RecyclerView.ViewHolder {
        /**
         * 创建View容器,根据key为控件id
         */
        private final SparseArray<View> viewArray;
        private int viewType;

        public BaseViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            viewArray = new SparseArray<>();
        }

        /**
         * 获取布局中的View
         *
         * @param viewId
         * @param <T>
         * @return
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
