package com.bracks.wanandroid.activity;

import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.bracks.mylib.base.basemvp.BasePresenter;
import com.bracks.mylib.base.basemvp.BaseView;
import com.bracks.mylib.base.basemvp.CreatePresenter;
import com.bracks.wanandroid.R;
import com.bracks.wanandroid.fragment.HomeFrag;
import com.bracks.wanandroid.fragment.MyFrag;
import com.bracks.wanandroid.fragment.PubFrag;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * good programmer.
 *
 * @date : 2019-04-27 下午 03:25
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
@CreatePresenter(BasePresenter.class)
public class HomeUi extends BaseUi<BaseView, BasePresenter<BaseView>> {
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.bottomNavigationBar)
    BottomNavigationBar bottomNavigationBar;

    private List<Fragment> fragments = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        fragments.add(HomeFrag.newInstance());
        fragments.add(PubFrag.newInstance());
        fragments.add(PubFrag.newInstance());
        fragments.add(PubFrag.newInstance());
        fragments.add(MyFrag.newInstance());
        showFragmentByIndex(0);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .setActiveColor(R.color.common_main_color)
                .setInActiveColor(R.color.common_hint_color)
                .setBarBackgroundColor(R.color.white)
                .addItem(new BottomNavigationItem(R.mipmap.home_check, "首页").setInactiveIconResource(R.mipmap.home))
                .addItem(new BottomNavigationItem(R.mipmap.pub_check, "公众号").setInactiveIconResource(R.mipmap.pub))
                .addItem(new BottomNavigationItem(R.mipmap.pub_check, "公众号").setInactiveIconResource(R.mipmap.pub))
                .addItem(new BottomNavigationItem(R.mipmap.pub_check, "公众号").setInactiveIconResource(R.mipmap.pub))
                .addItem(new BottomNavigationItem(R.mipmap.my_check, "我的").setInactiveIconResource(R.mipmap.my))
                .setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(int position) {
                        showFragmentByIndex(position);
                    }

                    @Override
                    public void onTabUnselected(int position) {
                    }

                    @Override
                    public void onTabReselected(int position) {

                    }
                })
                .initialise();
    }

    @Override
    protected ViewModel initViewModel() {
        return null;
    }

    public void showFragmentByIndex(int index) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        for (int i = 0; i < fragments.size(); i++) {
            if (i == index) {
                if (fragments.get(i).isAdded()) {
                    fragmentManager.beginTransaction().show(fragments.get(i)).commit();
                } else {
                    fragmentManager.beginTransaction().add(R.id.container, fragments.get(i)).commit();
                }
            } else {
                if (fragments.get(i).isAdded()) {
                    fragmentManager.beginTransaction().hide(fragments.get(i)).commit();
                }
            }
        }
    }
}
