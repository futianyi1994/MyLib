package com.bracks.wanandroid;


import android.util.Log;

import com.bracks.mylib.base.BaseApp;
import com.tencent.smtt.sdk.QbSdk;

/**
 * good programmer.
 *
 * @date : 2019-01-28 下午 05:01
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class App extends BaseApp {

    @Override
    protected void onCreate(boolean isAppCreate) {
        //初始化X5内核相关
        initX5Environment();
    }

    /**
     * 初始化X5内核相关
     */
    private void initX5Environment() {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }
}
