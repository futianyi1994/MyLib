package com.bracks.mylib.base.adapter;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
    private final List<T> fragments;
    private final List<String> tabList;
    private final SparseArray<Fragment> registeredFragments = new SparseArray<>();

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

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabList.get(position);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
