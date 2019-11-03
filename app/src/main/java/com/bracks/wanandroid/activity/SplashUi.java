package com.bracks.wanandroid.activity;

import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.blankj.utilcode.util.ActivityUtils;
import com.bracks.mylib.base.basemvp.CreatePresenter;
import com.bracks.wanandroid.R;
import com.bracks.wanandroid.contract.SplashContract;
import com.bracks.wanandroid.presenter.SplashP;
import com.bracks.wanandroid.utils.SoundPlayUtils;

import butterknife.BindView;


/**
 * good programmer.
 *
 * @date : 2019-06-12 下午 04:25
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
@CreatePresenter(SplashP.class)
public class SplashUi extends BaseUi<SplashContract.View, SplashP> implements SplashContract.View {

    @BindView(R.id.one_animation)
    LottieAnimationView mOneAnimation;
    @BindView(R.id.two_animation)
    LottieAnimationView mTwoAnimation;
    @BindView(R.id.three_animation)
    LottieAnimationView mThreeAnimation;
    @BindView(R.id.four_animation)
    LottieAnimationView mFourAnimation;
    @BindView(R.id.five_animation)
    LottieAnimationView mFiveAnimation;
    @BindView(R.id.six_animation)
    LottieAnimationView mSixAnimation;
    @BindView(R.id.seven_animation)
    LottieAnimationView mSevenAnimation;
    @BindView(R.id.eight_animation)
    LottieAnimationView mEightAnimation;
    @BindView(R.id.nine_animation)
    LottieAnimationView mNineAnimation;
    @BindView(R.id.ten_animation)
    LottieAnimationView mTenAnimation;

    private SoundPlayUtils soundPlayUtils;

    @Override
    protected void onDestroy() {
        cancelAnimation();
        soundPlayUtils.release();
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected ViewModel initViewModel() {
        return null;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        soundPlayUtils = new SoundPlayUtils(this, 2)
                .load("pika", R.raw.pika)
                .load("ohuo", R.raw.ohuo);
        startAnimation(mOneAnimation, "W.json");
        startAnimation(mTwoAnimation, "A.json");
        startAnimation(mThreeAnimation, "N.json");
        startAnimation(mFourAnimation, "A.json");
        startAnimation(mFiveAnimation, "N.json");
        startAnimation(mSixAnimation, "D.json");
        startAnimation(mSevenAnimation, "R.json");
        startAnimation(mEightAnimation, "I.json");
        startAnimation(mNineAnimation, "O.json");
        startAnimation(mTenAnimation, "D.json");
        getPresenter().jumpToMain();
    }

    @Override
    public void jumpToMain() {
        soundPlayUtils.playRadom();
        ActivityUtils.startActivity(new Intent(this, HomeUi.class), android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void cancelAnimation() {
        cancelAnimation(mOneAnimation);
        cancelAnimation(mTwoAnimation);
        cancelAnimation(mThreeAnimation);
        cancelAnimation(mFourAnimation);
        cancelAnimation(mFiveAnimation);
        cancelAnimation(mSixAnimation);
        cancelAnimation(mSevenAnimation);
        cancelAnimation(mEightAnimation);
        cancelAnimation(mNineAnimation);
        cancelAnimation(mTenAnimation);
    }

    private void startAnimation(LottieAnimationView mLottieAnimationView, String animationName) {
        mLottieAnimationView.setAnimation(animationName);
        mLottieAnimationView.playAnimation();
    }

    private void cancelAnimation(LottieAnimationView mLottieAnimationView) {
        if (mLottieAnimationView != null) {
            mLottieAnimationView.cancelAnimation();
        }
    }
}
