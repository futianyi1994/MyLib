package com.bracks.wanandroid.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.ActivityUtils;
import com.bracks.mylib.base.basemvp.BasePresenter;
import com.bracks.mylib.base.basemvp.CreatePresenter;
import com.bracks.mylib.base.basevm.LViewModelProviders;
import com.bracks.wanandroid.R;
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
public class RegisterUi extends BaseUi {

    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etPsw)
    EditText etPsw;
    @BindView(R.id.etRePsw)
    EditText etRePsw;
    @BindView(R.id.tvRegister)
    TextView tvRegister;

    private LoginViewModel viewModel;


    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected ViewModel initViewModel() {
        viewModel = LViewModelProviders.of(this, LoginViewModel.class);
        viewModel
                .getRegitsterLiveData()
                .observe(this, dataBean -> {
                    if (!TextUtils.isEmpty(dataBean.getUsername())) {
                        registerSrccess();
                    }
                });
        return viewModel;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

    }

    @OnClick({R.id.tvRegister})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvRegister:
                viewModel.register(etUserName.getText().toString(), etPsw.getText().toString(), etRePsw.getText().toString());
                break;
            default:
                break;
        }
    }

    public void registerSrccess() {
        showToast("注册成功");
        ActivityUtils.startActivity(HomeUi.class);
        finish();
    }
}
