package com.bracks.wanandroid.fragment;

import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bracks.mylib.base.basemvp.BasePresenter;
import com.bracks.mylib.base.basemvp.BaseView;
import com.bracks.mylib.base.basemvp.CreatePresenter;
import com.bracks.mylib.base.basevm.BaseVmProxyFrag;
import com.bracks.mylib.base.basevm.LViewModelProviders;
import com.bracks.mylib.rx.RxBus;
import com.bracks.wanandroid.R;
import com.bracks.wanandroid.activity.HomeUi;
import com.bracks.wanandroid.adapter.TabPagerAdapter;
import com.bracks.wanandroid.model.bean.PublicList;
import com.bracks.wanandroid.model.evenbus.QueryEvent;
import com.bracks.wanandroid.viewmodel.PubViewModel;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * good programmer.
 *
 * @date : 2019-07-27 上午 10:50
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
@CreatePresenter(BasePresenter.class)
public class PubFrag extends BaseVmProxyFrag<BaseView, BasePresenter<BaseView>> {

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;


    private PubViewModel viewModel;
    private TabPagerAdapter pagerAdapter;
    private List<String> tabList = new ArrayList<>();
    private List<PublicList.DataBean> dataBeans;

    private SearchView searchView;
    private SearchView.SearchAutoComplete mSearchAutoComplete;

    public static String search = "";


    public static PubFrag newInstance() {
        Bundle args = new Bundle();
        PubFrag fragment = new PubFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected ViewModel initViewModel() {
        viewModel = LViewModelProviders.of(this, PubViewModel.class);
        viewModel
                .getPubListLiveData()
                .observe(this, dataBeans -> {
                    this.dataBeans = dataBeans;
                    for (PublicList.DataBean dataBean : dataBeans) {
                        tabList.add(dataBean.getName());
                    }
                    pagerAdapter = new TabPagerAdapter(getChildFragmentManager(), dataBeans);
                    viewPager.setAdapter(pagerAdapter);
                    tabLayout.setupWithViewPager(viewPager);
                });
        viewModel.getPublicList();
        return viewModel;
    }

    @Override
    public void showToast(String msg) {
        ToastUtils.showLong(msg);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_pub;
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        coordinatorLayout.setPadding(0, BarUtils.getStatusBarHeight(), 0, 0);
        toolbar.setTitle("公众号");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        //设置是否显示返回按钮
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            if (mSearchAutoComplete.isShown()) {
                try {
                    mSearchAutoComplete.setText("");
                    Method method = searchView.getClass().getDeclaredMethod("onCloseClicked");
                    method.setAccessible(true);
                    method.invoke(searchView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (getActivity() instanceof HomeUi) {
                    ((HomeUi) getActivity()).selectTab(R.id.tab_home);
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_history, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);

        //通过MenuItem得到SearchView
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchAutoComplete = searchView.findViewById(R.id.search_src_text);
        searchView.setQueryHint("搜索历史文章");

        //设置输入框提示文字样式
        mSearchAutoComplete.setHintTextColor(getResources().getColor(android.R.color.darker_gray));
        mSearchAutoComplete.setTextColor(getResources().getColor(android.R.color.background_light));
        mSearchAutoComplete.setTextSize(16);
        //设置触发查询的最少字符数（默认2个字符才会触发查询）
        mSearchAutoComplete.setThreshold(1);

        //true value will collapse the SearchView to an icon, while a false will expand it.左侧无放大镜 右侧有叉叉 可以关闭搜索框
        searchView.setIconified(true);
        //The default value is true，设置为false直接展开显示 左侧有放大镜(在搜索框外) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
        //searchView.setIconifiedByDefault(false);
        //内部调用了setIconified(false); 直接展开显示 左侧无放大镜 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
        //searchView.onActionViewExpanded();

        //设置最大宽度
        searchView.setMaxWidth(ScreenUtils.getScreenWidth());
        //设置是否显示搜索框展开时的提交按钮
        searchView.setSubmitButtonEnabled(false);

        //修改搜索框控件间的间隔
        LinearLayout search_edit_frame = searchView.findViewById(R.id.search_edit_frame);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) search_edit_frame.getLayoutParams();
        params.leftMargin = 0;
        params.rightMargin = 0;
        search_edit_frame.setLayoutParams(params);

        //监听SearchView的内容
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                search = s;
                RxBus
                        .getDefault()
                        .post(new QueryEvent(dataBeans.get(tabLayout.getSelectedTabPosition()).getId(), search));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                search = s;
                return false;
            }
        });
        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                search = "";
                RxBus
                        .getDefault()
                        .post(new QueryEvent(dataBeans.get(tabLayout.getSelectedTabPosition()).getId(), search));
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
}
