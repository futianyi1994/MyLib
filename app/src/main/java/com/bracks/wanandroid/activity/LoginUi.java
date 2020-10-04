package com.bracks.wanandroid.activity;

import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.bracks.mylib.base.basemvp.BasePresenter;
import com.bracks.mylib.base.basemvp.CreatePresenter;
import com.bracks.mylib.base.basevm.LViewModelProviders;
import com.bracks.wanandroid.R;
import com.bracks.wanandroid.manager.LoginManager;
import com.bracks.wanandroid.viewmodel.LoginViewModel;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * good programmer.
 *
 * @date : 2019-01-02 下午 01:59
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
@CreatePresenter(BasePresenter.class)
public class LoginUi extends BaseUi {

    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etPsw)
    EditText etPsw;
    @BindView(R.id.tvLogin)
    TextView tvLogin;
    @BindView(R.id.tvRegister)
    TextView tvRegister;

    private LoginViewModel viewModel;


    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected ViewModel initViewModel() {
        viewModel = LViewModelProviders.of(this, LoginViewModel.class);
        viewModel
                .getHistoryLiveData()
                .observe(this, dataBean -> {
                    if (!TextUtils.isEmpty(dataBean.getUsername())) {
                        loginSrccess(dataBean.getUsername());
                    }
                });
        return viewModel;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

    }

    @OnClick({R.id.tvLogin, R.id.tvRegister})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvLogin:
                viewModel.login(etUserName.getText().toString(), etPsw.getText().toString());
                break;
            case R.id.tvRegister:
                ActivityUtils.startActivity(RegisterUi.class);
                break;
            default:
                break;
        }
    }

    public void loginSrccess(String username) {
        showToast("登陆成功");
        ActivityUtils.startActivity(HomeUi.class);
        LoginManager.login(username);
        finish();
    }
}
