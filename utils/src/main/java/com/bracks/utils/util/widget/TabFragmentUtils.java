package com.bracks.utils.util.widget;

import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.List;


/**
 * good programmer.
 *
 * @date : 2019-10-04 下午 12:21
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class TabFragmentUtils {
    private final List<Fragment> fragments;
    private final int containerLayout;
    private final FragmentManager fragmentManager;

    public TabFragmentUtils(List<Fragment> fragments, @IdRes int containerLayout, FragmentManager fragmentManager) {
        this(null, fragments, containerLayout, fragmentManager);
    }

    public TabFragmentUtils(RadioGroup radioGroup, List<Fragment> fragments, @IdRes int containerLayout, FragmentManager fragmentManager) {
        this.fragments = fragments;
        this.containerLayout = containerLayout;
        this.fragmentManager = fragmentManager;
        if (radioGroup == null) {
            return;
        }
        //设置radiogroup的选中监听
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            for (int i = 0; i < group.getChildCount(); i++) {
                RadioButton view = (RadioButton) group.getChildAt(i);
                if (view.getId() == checkedId) {
                    //显示响应的页面，隐藏其他页面
                    showFragmentByIndex(i);
                }
            }
        });
        //默认选中第0个
        showFragmentByIndex(0);
        ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);
    }

    public void showFragmentByIndex(int index) {
        for (int i = 0; i < fragments.size(); i++) {
            if (i == index) {
                if (fragments.get(i).isAdded()) {
                    fragmentManager.beginTransaction().show(fragments.get(i)).commit();
                } else {
                    fragmentManager.beginTransaction().add(containerLayout, fragments.get(i)).commit();
                }
            } else {
                if (fragments.get(i).isAdded()) {
                    fragmentManager.beginTransaction().hide(fragments.get(i)).commit();
                }
            }
        }
    }
}
