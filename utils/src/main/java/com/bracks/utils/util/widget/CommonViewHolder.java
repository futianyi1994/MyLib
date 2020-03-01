package com.bracks.utils.util.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * good programmer.
 *
 * @date : 2020/2/29 17:17
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :共用ViewHolder的工具类
 */
public class CommonViewHolder {
    /**
     * // 通用的ViewHoder，用 集合<View> 来代替多个成员变量 ，然后用同一个方法传入不同的id，来拿到需要的小控件
     */
    public final View convertView;
    private Map<Integer, View> views = new HashMap<>();


    private CommonViewHolder(View convertView) {
        this.convertView = convertView;
    }

    public static CommonViewHolder createCVH(View convertView, ViewGroup parent, int layoutRes) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            convertView = layoutInflater.inflate(layoutRes, parent, false);
            CommonViewHolder cvh = new CommonViewHolder(convertView);
            convertView.setTag(cvh);
        }
        CommonViewHolder cvh = (CommonViewHolder) convertView.getTag();

        return cvh;
    }

    public View getView(int id) {
        if (views.get(id) == null) {
            views.put(id, convertView.findViewById(id));
        }
        return views.get(id);
    }

    public TextView getTv(int id) {
        return ((TextView) getView(id));
    }

    public ImageView getIv(int id) {
        return ((ImageView) getView(id));
    }

    public CommonViewHolder setText(int id, String text) {
        getTv(id).setText(text);
        return this;
    }
}

