package com.bracks.wanandroid.viewiterf;


import com.bracks.mylib.base.basemvp.BaseView;
import com.bracks.wanandroid.model.bean.PublicList;

import java.util.List;

public interface PublicFragV extends BaseView {
    void showDatas(List<PublicList.DataBean> data);
}
