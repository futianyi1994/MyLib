package com.bracks.wanandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.bracks.mylib.base.basemvp.BasePresenter;
import com.bracks.mylib.base.basemvp.BaseView;
import com.bracks.mylib.base.basemvp.CreatePresenter;
import com.bracks.mylib.base.basevm.LViewModelProviders;
import com.bracks.wanandroid.R;
import com.bracks.wanandroid.adapter.ChapterAdapter;
import com.bracks.wanandroid.model.bean.Chapter;
import com.bracks.wanandroid.viewmodel.HistoryViewModel;
import com.bracks.wanandroid.widget.recycleview.SpaceItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.lang.reflect.Method;
import java.util.List;

import butterknife.BindView;

import static com.bracks.mylib.utils.CommonUtils.getContext;

/**
 * good programmer.
 *
 * @date : 2019-02-18 下午 05:28
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
@CreatePresenter(BasePresenter.class)
public class HistoryUi extends BaseUi<BaseView, BasePresenter<BaseView>> {
    public static final String EXTRA_ID = "id";
    public static final String EXTRA_PAGE = "page";
    public static final String EXTRA_TITLE = "title";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private HistoryViewModel viewModel;
    private ChapterAdapter adapter;
    private int id;
    private int page;
    private String title;
    private String search = "";

    private SearchView searchView;
    private SearchView.SearchAutoComplete mSearchAutoComplete;


    @Override
    protected ViewModel initViewModel() {
        showLoading("加载中", true);
        viewModel = LViewModelProviders.of(this, HistoryViewModel.class);
        viewModel
                .getHistoryLiveData()
                .observe(this, datasBeans -> {
                    if (page == 1) {
                        showDatas(datasBeans);
                    } else {
                        loadMore(datasBeans);
                    }
                });
        viewModel.queryHistory(id, page, search);
        return viewModel;
    }

    @Override
    protected boolean isTransparencyBar() {
        BarUtils.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimaryDark));
        return false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_history;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getIntExtra(EXTRA_ID, 0);
            page = intent.getIntExtra(EXTRA_PAGE, 0);
            title = intent.getStringExtra(EXTRA_TITLE);
        }
        recyclerView.addItemDecoration(new SpaceItemDecoration(ConvertUtils.dp2px(10)));
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        //设置是否显示返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_history, menu);
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
                viewModel.queryHistory(id, page, search);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                search = s;
                viewModel.queryHistory(id, page, search);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 让菜单同时显示图标和文字
     *
     * @param featureId
     * @param menu
     * @return
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    public void showDatas(List<Chapter.DataBean.DatasBean> data) {
        if (adapter == null) {
            adapter = new ChapterAdapter(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
            refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    page++;
                    viewModel.queryHistory(id, page, search);
                }

                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    page = 1;
                    viewModel.queryHistory(id, page, search);
                }
            });
        }
        adapter.setData(data);
        refreshLayout.finishRefresh();
    }

    public void loadMore(List<Chapter.DataBean.DatasBean> data) {
        adapter.addAll(data);
        refreshLayout.finishLoadMore();
    }
}
