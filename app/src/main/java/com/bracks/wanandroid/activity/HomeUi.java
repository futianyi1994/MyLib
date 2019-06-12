package com.bracks.wanandroid.activity;

import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.bracks.mylib.base.basemvp.BasePresenter;
import com.bracks.mylib.base.basemvp.BaseView;
import com.bracks.mylib.base.basemvp.CreatePresenter;
import com.bracks.wanandroid.R;
import com.bracks.wanandroid.fragment.MyFrag;
import com.bracks.wanandroid.fragment.PublicFrag;

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

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        switchFragment(PublicFrag.newInstance(), false);
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
                        switch (position) {
                            case 0:
                                switchFragment(PublicFrag.newInstance(), false);
                                break;
                            case 1:
                                switchFragment(new PublicFrag(), false);
                                break;
                            case 2:
                                switchFragment(new PublicFrag(), false);
                                break;
                            case 3:
                                switchFragment(new PublicFrag(), false);
                                break;
                            case 4:
                                switchFragment(MyFrag.newInstance(), false);
                                break;
                            default:
                                break;
                        }
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

    private void switchFragment(Fragment fragment, boolean backStack) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (backStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.replace(R.id.container, fragment).commit();
    }
}
