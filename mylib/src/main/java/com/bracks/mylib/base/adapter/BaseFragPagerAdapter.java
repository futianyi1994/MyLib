package com.bracks.mylib.base.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * good programmer.
 *
 * @data : 2018-01-12 上午 10:51
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class BaseFragPagerAdapter<T extends Fragment> extends FragmentPagerAdapter {
    private List<T> fragments;
    private List<String> tabList;

    public BaseFragPagerAdapter(FragmentManager fm, List<T> fragments, List<String> tabList) {
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
