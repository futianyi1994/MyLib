package com.bracks.wanandroid.fragment;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bracks.mylib.base.basemvp.BaseProxyFrag;
import com.bracks.mylib.base.basemvp.CreatePresenter;
import com.bracks.mylib.rx.RxAutoDispose;
import com.bracks.mylib.rx.RxBus;
import com.bracks.mylib.utils.bar.BarUtils;
import com.bracks.mylib.utils.save.SPUtils;
import com.bracks.mylib.utils.widget.DialogUtils;
import com.bracks.wanandroid.Contants;
import com.bracks.wanandroid.R;
import com.bracks.wanandroid.activity.LoginUi;
import com.bracks.wanandroid.adapter.MyAdapter;
import com.bracks.wanandroid.contract.MyFragContract;
import com.bracks.wanandroid.model.evenbus.LoginEvent;
import com.bracks.wanandroid.presenter.MyP;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

@CreatePresenter(MyP.class)
public class MyFrag extends BaseProxyFrag<MyFragContract.View, MyP> implements MyFragContract.View {


    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;


    private Dialog dialog;


    public static MyFrag newInstance() {
        Bundle args = new Bundle();
        MyFrag fragment = new MyFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    public void initView(View view, @Nullable Bundle savedInstanceState) {
        getPresenter().fetch();
        if (SPUtils.getInstance().getBoolean(Contants.SP_IS_LOGIN)) {
            tvUserName.setText(SPUtils.getInstance().getString(Contants.SP_USER_NAME));
        } else {
            tvUserName.setText("请登录");
        }
        refreshLayout.setEnableOverScrollDrag(true);
        refreshLayout.setOnRefreshListener(refreshLayout -> getPresenter().fetch());
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        Disposable disposable = RxBus
                .getDefault()
                .toObservable(LoginEvent.class)
                .as(RxAutoDispose.bindLifecycle(this))
                .subscribe(loginEvent -> {
                    getPresenter().fetch();
                    if (SPUtils.getInstance().getBoolean(Contants.SP_IS_LOGIN)) {
                        tvUserName.setText(SPUtils.getInstance().getString(Contants.SP_USER_NAME));
                    } else {
                        tvUserName.setText("请登录");
                    }
                }, throwable -> {
                });
    }

    @Override
    public void showDatas(List<String> data) {
        MyAdapter adapter = new MyAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setData(data);
        recyclerView.setAdapter(adapter);
        refreshLayout.finishRefresh();
    }

    @Override
    public void showLoading(String msg, boolean isCancelable) {
        dialog = DialogUtils.createLoadingDialog(getActivity(), msg, isCancelable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            DialogUtils.afterShow(() -> BarUtils.hideNavBar(dialog.getWindow().getDecorView()));
        } else {
            dialog.show();
        }
    }

    @Override
    public void hideLoading() {
        DialogUtils.dismissDialog(dialog);
    }

    @Override
    public void showToast(String msg) {
        ToastUtils.showLong(msg);
    }

    @OnClick({R.id.tvUserName, R.id.image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvUserName:
                if ("请登录".equals(tvUserName.getText().toString())) {
                    ActivityUtils.startActivity(LoginUi.class);
                }
                break;
            case R.id.image:
                break;
            default:
                break;
        }
    }
}
