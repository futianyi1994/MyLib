package com.bracks.wanandroid.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.bracks.wanandroid.fragment.PubTabFrag;
import com.bracks.wanandroid.model.bean.PublicList;

import java.util.List;

/**
 * good programmer.
 *
 * @date : 2019-07-27 下午 03:02
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class TabPagerAdapter extends FragmentPagerAdapter {
    private List<PublicList.DataBean> dataBeans;

    public TabPagerAdapter(FragmentManager fm, List<PublicList.DataBean> dataBeans) {
        super(fm);
        this.dataBeans = dataBeans;
    }

    @Override
    public Fragment getItem(int i) {
        return PubTabFrag.newInstance(dataBeans.get(i).getId());
    }

    @Override
    public int getCount() {
        return dataBeans.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return dataBeans.get(position).getName();
    }

}
