package com.bracks.wanandroid.presenter;

import com.bracks.mylib.base.basemvp.BasePresenter;
import com.bracks.wanandroid.contract.MyFragContract;

import java.util.ArrayList;
import java.util.List;

/**
 * good programmer.
 *
 * @date : 2019-02-18 下午 05:33
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class MyP extends BasePresenter<MyFragContract.View> implements MyFragContract.Presenter {
    @Override
    public void fetch() {
        if (getView() != null) {
            List<String> items = new ArrayList<>();
            items.add("收藏");
            items.add("设置");
            items.add("退出");
            getView().showDatas(items);
        }
    }
}
