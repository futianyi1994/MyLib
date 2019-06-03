package com.bracks.wanandroid.activity;

import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bracks.mylib.base.basemvp.CreatePresenter;
import com.bracks.wanandroid.R;
import com.bracks.wanandroid.presenter.LoginP;
import com.bracks.wanandroid.viewiterf.LoginV;

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
@CreatePresenter(LoginP.class)
public class LoginUi extends BaseUi<LoginV, LoginP> implements LoginV {

    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etPsw)
    EditText etPsw;
    @BindView(R.id.tvLogin)
    TextView tvLogin;
    @BindView(R.id.tvRegister)
    TextView tvRegister;


    @Override
    protected ViewModel initViewModel() {
        return getPresenter().getViewModel(this);
    }

    @Override
    public void showToast(String msg) {
        ToastUtils.showLong(msg);
    }

    @Override
    public void loginSrccess() {
        showToast("登陆成功");
        ActivityUtils.startActivity(HomeUi.class);
        finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @OnClick({R.id.tvLogin, R.id.tvRegister})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvLogin:
                getPresenter().login(etUserName.getText().toString(), etPsw.getText().toString());
                break;
            case R.id.tvRegister:
                showToast("暂不支持");
                break;
            default:
                break;
        }
    }
}
