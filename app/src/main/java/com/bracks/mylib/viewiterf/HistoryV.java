package com.bracks.mylib.viewiterf;

import com.bracks.futia.mylib.base.basemvp.BaseView;
import com.bracks.mylib.model.bean.History;

import java.util.List;

/**
 * good programmer.
 *
 * @date : 2019-02-18 下午 06:35
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public interface HistoryV extends BaseView {

    void showDatas(List<History.DataBean.DatasBean> data);

    void loadMore(List<History.DataBean.DatasBean> data);

}
