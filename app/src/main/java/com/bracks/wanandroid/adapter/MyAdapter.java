package com.bracks.wanandroid.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.bracks.mylib.base.adapter.MBaseRecyclerViewAdapter;
import com.bracks.mylib.rx.RxAppActivity;
import com.bracks.mylib.rx.RxDefaultObserver;
import com.bracks.mylib.rx.RxObservHelper;
import com.bracks.mylib.rx.RxSchedulersCompat;
import com.bracks.mylib.utils.save.SPUtils;
import com.bracks.mylib.widget.CustomAlertDialog;
import com.bracks.wanandroid.Contants;
import com.bracks.wanandroid.R;
import com.bracks.wanandroid.activity.CollectUi;
import com.bracks.wanandroid.manager.LoginManager;
import com.bracks.wanandroid.model.bean.Login;
import com.bracks.wanandroid.net.ApiService;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * good programmer.
 *
 * @date : 2019-06-12 下午 11:01
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class MyAdapter extends MBaseRecyclerViewAdapter<String> {

    private CustomAlertDialog dialog;

    @Override
    public int getItemLayoutResId(int viewType) {
        return R.layout.item_my;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(@NonNull View itemView) {
        return new MViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Context context = viewHolder.itemView.getContext();
        TextView tvItem = ((MViewHolder) viewHolder).tvItem;
        tvItem.setText(getItem(i));
        switch (i) {
            //收藏
            case 0:
                tvItem.setOnClickListener(v -> ActivityUtils.startActivity(CollectUi.class));
                break;
            //设置
            case 1:
                break;
            //登录
            case 2:
                if (SPUtils.getBoolean(Contants.SP_IS_LOGIN)) {
                    tvItem.setText("退出登录");
                    tvItem.setOnClickListener(v -> {
                        dialog = new CustomAlertDialog
                                .Builder(context)
                                .setDefaultPromptView2()
                                .setTitle("")
                                .setWidth(700)
                                .setMessage("退出登录？")
                                .setNegativeButton("是", v12 -> ApiService
                                        .getService()
                                        .logout()
                                        .compose(RxSchedulersCompat.ioSchedulerObser())
                                        .compose(RxObservHelper.applyProgressBar((RxAppActivity) context, true))
                                        .compose(((RxAppActivity) context).bindToLifecycle())
                                        .subscribe(new RxDefaultObserver<Login>() {
                                            @Override
                                            public void onSuccess(Login response) {
                                                dialog.dismiss();
                                                LoginManager.logout();
                                            }
                                        }))
                                .setPositiveButton("否", v1 -> dialog.dismiss())
                                .build();
                    });
                }
                break;
            default:
                break;
        }
    }

    class MViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvItem)
        TextView tvItem;

        MViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
