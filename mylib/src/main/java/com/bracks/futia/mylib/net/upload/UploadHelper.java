package com.bracks.futia.mylib.net.upload;

import com.blankj.utilcode.util.ToastUtils;
import com.bracks.futia.mylib.net.https.ProgressBean;
import com.bracks.futia.mylib.net.https.ProgressListener;
import com.bracks.futia.mylib.net.interceptor.HttpLogInterceptor;
import com.bracks.futia.mylib.utils.log.TLog;

import io.reactivex.Maybe;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableMaybeObserver;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * good programmer.
 *
 * @date : 2019-03-04 下午 04:29
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class UploadHelper {
    /**
     * 包装OkHttpClient，用于上传文件的回调
     *
     * @param builder
     * @param callback
     * @return 包装后的OkHttpClient
     */
    public static OkHttpClient addUploadListener(OkHttpClient.Builder builder, ProgressListener.ProgressCallback callback) {
        if (builder == null) {
            builder = new OkHttpClient.Builder();
        }

        //该方法在子线程中运行
        ProgressListener progressListener = (progress, total, done) ->
                Maybe
                        .create((MaybeOnSubscribe<ProgressBean>) emitter -> {
                            try {
                                int pro = (int) ((100 * progress) / total);
                                TLog.d("progress:", String.format("%d%% done\n", pro));
                                ProgressBean progressBean = new ProgressBean();
                                progressBean.setTitle("Upload...");
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
                                ToastUtils.showLong("上传过程出错：%s", e.getMessage());
                            }

                            @Override
                            public void onComplete() {
                                //此次完成只代表精度条走到100，上传成功另有回调（网络请求的回调）
                            }
                        });

        //增加拦截器，自定义ResponseBody，添加上传进度
        builder
                .addInterceptor(HttpLogInterceptor.get())
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request request = original
                            .newBuilder()
                            .method(original.method(), new ProgressRequestBody(original.body(), progressListener))
                            .build();
                    return chain.proceed(request);
                })
        ;
        return builder.build();
    }
}
