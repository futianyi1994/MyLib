package com.bracks.wanandroid.datasource;


import com.bracks.mylib.base.basevm.BaseRemoteDataSource;
import com.bracks.mylib.base.basevm.BaseViewModel;
import com.bracks.mylib.net.https.HttpCallback;
import com.bracks.wanandroid.model.bean.Chapter;
import com.bracks.wanandroid.net.ApiService;

/**
 * good programmer.
 *
 * @date : 2019-02-15 下午 02:46
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class HistoryDataSource extends BaseRemoteDataSource implements IHistoryDataSource {
    public HistoryDataSource(BaseViewModel baseViewModel) {
        super(baseViewModel);
    }


    @Override
    public void queryHistory(int id, int page, String search, HttpCallback<Chapter.DataBean> callback) {
        execute(ApiService.getService().getHistoryList(id, page,search), callback);
    }
}
