package com.bracks.wanandroid.activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.bracks.mylib.base.basemvp.BasePresenter;
import com.bracks.mylib.base.basemvp.CreatePresenter;
import com.bracks.mylib.rx.RxAutoDispose;
import com.bracks.mylib.rx.RxBus;
import com.bracks.utils.util.widget.TabFragmentUtils;
import com.bracks.wanandroid.R;
import com.bracks.wanandroid.fragment.HomeFrag;
import com.bracks.wanandroid.fragment.MyFrag;
import com.bracks.wanandroid.fragment.PubFrag;
import com.bracks.wanandroid.model.evenbus.ScrollEvent;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
public class HomeUi extends BaseUi {
    private final List<Fragment> fragments = new ArrayList<>();
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.bottomNavigationView)
    BottomNavigationView bottomNavigationView;
    private boolean isBottomShow = true;

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        fragments.add(HomeFrag.newInstance());
        fragments.add(PubFrag.newInstance());
        fragments.add(MyFrag.newInstance());
        RxBus
                .getDefault()
                .toObservable(ScrollEvent.class)
                .as(RxAutoDispose.bindLifecycle(this))
                .subscribe(scrollEvent -> {
                    if (scrollEvent.dy > 0 && isBottomShow) {
                        isBottomShow = false;
                        bottomNavigationView.animate().translationY(bottomNavigationView.getHeight());
                    } else if (scrollEvent.dy < 0 && !isBottomShow) {
                        isBottomShow = true;
                        bottomNavigationView.animate().translationY(0);
                    }
                });
    }

    @Override
    protected ViewModel initViewModel() {
        return null;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        TabFragmentUtils fragmentUtils = new TabFragmentUtils(fragments, R.id.container, getSupportFragmentManager());
        fragmentUtils.showFragmentByIndex(0);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.tab_home:
                    fragmentUtils.showFragmentByIndex(0);
                    break;
                case R.id.tab_pub:
                    fragmentUtils.showFragmentByIndex(1);
                    break;
                case R.id.tab_my:
                    fragmentUtils.showFragmentByIndex(2);
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    public void selectTab(@IdRes int itemId) {
        bottomNavigationView.setSelectedItemId(itemId);
    }
}
