package com.bracks.mylib.viewiterf;


import com.bracks.futia.mylib.base.basemvp.BaseView;
import com.bracks.mylib.model.bean.PublicList;

import java.util.List;

public interface PublicFragV extends BaseView {
    void showDatas(List<PublicList.DataBean> data);
}
