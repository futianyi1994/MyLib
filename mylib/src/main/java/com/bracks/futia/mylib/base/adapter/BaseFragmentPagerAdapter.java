package com.bracks.futia.mylib.base.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * good programmer.
 *
 * @data : 2018-01-12 上午 10:51
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class BaseFragmentPagerAdapter<T extends SupportFragment> extends FragmentPagerAdapter {
    private List<T> fragments = new ArrayList<>();
    private List<String> tabList = new ArrayList<>();

    public BaseFragmentPagerAdapter(FragmentManager fm, List<T> fragments, List<String> tabList) {
        super(fm);
        this.fragments = fragments;
        this.tabList = tabList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabList.get(position);
    }
}
