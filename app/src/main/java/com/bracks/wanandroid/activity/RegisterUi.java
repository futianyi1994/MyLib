package com.bracks.wanandroid.activity;

import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.bracks.mylib.base.basemvp.CreatePresenter;
import com.bracks.wanandroid.R;
import com.bracks.wanandroid.contract.LoginContract;
import com.bracks.wanandroid.presenter.LoginP;

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
public class RegisterUi extends BaseUi<LoginContract.View, LoginP> implements LoginContract.View {

    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etPsw)
    EditText etPsw;
    @BindView(R.id.etRePsw)
    EditText etRePsw;
    @BindView(R.id.tvRegister)
    TextView tvRegister;


    @Override
    protected ViewModel initViewModel() {
        return getPresenter().getViewModel(this);
    }

    @Override
    public void loginSrccess(String username) {
    }

    @Override
    public void registerSrccess() {
        showToast("注册成功");
        ActivityUtils.startActivity(HomeUi.class);
        finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @OnClick({R.id.tvRegister})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvRegister:
                getPresenter().register(etUserName.getText().toString(), etPsw.getText().toString(), etRePsw.getText().toString());
                break;
            default:
                break;
        }
    }
}
