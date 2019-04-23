package com.bracks.futia.mylib.net.download;

import com.blankj.utilcode.util.ToastUtils;
import com.bracks.futia.mylib.net.https.ProgressBean;
import com.bracks.futia.mylib.net.https.ProgressListener;
import com.bracks.futia.mylib.net.interceptor.DownloadInterceptor;
import com.bracks.futia.mylib.utils.log.TLog;

import java.util.Locale;

import io.reactivex.Maybe;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableMaybeObserver;
import okhttp3.OkHttpClient;

/**
 * good programmer.
 *
 * @date : 2018-09-02 上午 11:33
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
@Deprecated
public class DownloadHelper {

    private static final String TAG = "DownloadHelper";

    /**
     * 包装OkHttpClient，用于下载文件的回调
     *
     * @param builder
     * @param callback
     * @return 包装后的OkHttpClient
     */
    public static OkHttpClient addDownloadListener(OkHttpClient.Builder builder, ProgressListener.ProgressCallback callback) {
        if (builder == null) {
            builder = new OkHttpClient.Builder();
        }

        //该方法在子线程中运行
        ProgressListener progressListener = (progress, total, done) ->
                Maybe
                        .create((MaybeOnSubscribe<ProgressBean>) emitter -> {
                            try {
                                int pro = (int) ((100 * progress) / total);
                                TLog.d(TAG, "progress:" + String.format(Locale.getDefault(), "%d%% done\n", pro));
                                ProgressBean progressBean = new ProgressBean();
                                progressBean.setTitle("Download...");
                                progressBean.setBytesRead(progress);
                                progressBean.setContentLength(total);
                                progressBean.setProgress(pro);
                                progressBean.setDone(done);
                                emitter.onSuccess(progressBean);
                            } catch (Exception e) {
                                e.printStackTrace();
                                emitter.onError(e);
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableMaybeObserver<ProgressBean>() {
                            @Override
                            public void onSuccess(ProgressBean progressBean) {
                                if (callback != null) {
                                    callback.onUpload(progressBean);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtils.showLong("下载过程出错：%s", e.getMessage());
                            }

                            @Override
                            public void onComplete() {
                                //此次完成只代表精度条走到100//此次完成只代表精度条走到100，下载成功另有回调（网络请求的回调）
                            }
                        });

        //添加拦截器，自定义ResponseBody，添加下载进度
        builder
                //.addInterceptor(HttpLogInterceptor.get())
                .addInterceptor(new DownloadInterceptor(progressListener))
        ;
        return builder.build();
    }
}
