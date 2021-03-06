package com.bracks.mylib.base;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.KeyboardUtils;
import com.bracks.mylib.R;
import com.bracks.mylib.base.interf.BaseUiInterf;
import com.bracks.mylib.utils.BarUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * good programmer.
 *
 * @date : 2016-12-29 上午 10:14
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseUiInterf {

    protected Unbinder mUnbinder;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutId() != 0) {
            setBackgroundColor(R.color.common_item_gray_bg);
            setContentView(getLayoutId());
        }
        // 通过注解绑定控件
        mUnbinder = ButterKnife.bind(this);
        if (isTransparencyBar()) {
            com.blankj.utilcode.util.BarUtils.setStatusBarColor(this, Color.alpha(0));
        }
        if (isLightBarMode()) {
            BarUtils.setLightStatusBar(this, true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        KeyboardUtils.hideSoftInput(this);
    }

    /**
     * 设置背景颜色
     *
     * @param color
     */
    protected void setBackgroundColor(@ColorRes int color) {
        this.getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, color));
    }

    /**
     * 设置背景资源
     */
    protected void setBackgroundResource(@DrawableRes int resid) {
        this.getWindow().getDecorView().setBackgroundResource(resid);
    }

    /**
     * 是否执行状态栏透明的操作，返回一个布尔值，默认是执行，
     * 可以在子类中重写不执行
     *
     * @return
     */
    protected boolean isTransparencyBar() {
        return true;
    }

    /**
     * 是否状态栏亮色的操作，返回一个布尔值，默认是不执行，
     * 可以在子类中重写不执行
     *
     * @return
     */
    protected boolean isLightBarMode() {
        return false;
    }
}
