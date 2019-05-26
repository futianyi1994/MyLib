package com.bracks.mylib.datasource;


import com.bracks.futia.mylib.base.basevm.BaseRemoteDataSource;
import com.bracks.futia.mylib.base.basevm.BaseViewModel;
import com.bracks.futia.mylib.net.https.HttpCallback;
import com.bracks.mylib.model.bean.History;
import com.bracks.mylib.net.ApiService;

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
    public void queryHistory(int id, int page, String search, HttpCallback<History.DataBean> callback) {
        execute(ApiService.getService().getHistoryList(id, page,search), callback);
    }
}
