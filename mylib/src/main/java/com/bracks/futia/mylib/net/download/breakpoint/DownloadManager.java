package com.bracks.futia.mylib.net.download.breakpoint;

import com.blankj.utilcode.util.ToastUtils;
import com.bracks.futia.mylib.Constants;
import com.bracks.futia.mylib.net.BaseService;
import com.bracks.futia.mylib.net.https.ProgressListener;
import com.bracks.futia.mylib.net.interceptor.DownloadInterceptor;
import com.bracks.futia.mylib.rx.RxRetryWithObservFunc;
import com.bracks.futia.mylib.utils.json.JsonUtil;
import com.bracks.futia.mylib.utils.log.TLog;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * good programmer.
 *
 * @date : 2019-04-03 下午 06:25
 * @author: futia
 * @email : futianyi1994@126.com
 * @description : 下载工具类
 */
public class DownloadManager {
    public static final String TAG = "DownloadManager";
    private DownloadInfo info;
    private ProgressListener progressListener;
    private File outFile;
    private Disposable disposable;
    private static BaseService service;

    private static Retrofit retrofit;

    /**
     * 单利构造器私有化
     */
    private DownloadManager() {
        info = new DownloadInfo();
    }

    /**
     * 静态内部类，实例化对象使用
     */
    private static class SingleInstanceHolder {
        private static final DownloadManager INSTANCE = new DownloadManager();
    }

    /**
     * 对外唯一实例的接口
     */
    public static DownloadManager getInstance() {
        return SingleInstanceHolder.INSTANCE;
    }

    /**
     * 设置精度条监听
     *
     * @param progressListener
     */
    public void setProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    /**
     * 获取下载保存的File
     *
     * @return
     */
    public File getOutFile() {
        return outFile;
    }

    /**
     * 设置下载保存的File
     *
     * @param outFile
     */
    public void setOutFile(File outFile) {
        this.outFile = outFile;
        info.setSavePath(outFile.getAbsolutePath());
    }

    /**
     * 获取下载状态
     *
     * @return
     */
    public @DownloadInfo.DownloadState
    int getState() {
        return info.getState();
    }

    /**
     * 是否停止下载
     *
     * @return
     */
    public boolean isStop() {
        return info.getState() == DownloadInfo.STOP;
    }

    /**
     * 是否暂停
     *
     * @return
     */
    public boolean isPause() {
        return info.getState() == DownloadInfo.PAUSE;
    }

    /**
     * 是否正在下载
     *
     * @return
     */
    public boolean isDownloading() {
        return info.getState() == DownloadInfo.DOENLOADING;
    }

    /**
     * 开始下载
     *
     * @param url
     */
    public void start(String url) {
        start(Constants.DOWN_UP_LOAD_HOST, url);
    }

    /**
     * 开始\继续下载
     *
     * @param host
     * @param url
     */
    public void start(String host, String url) {
        if (info.getState() == DownloadInfo.DOENLOADING) {
            TLog.d(TAG, "正在下载中");
            return;
        }
        info.setUrl(url);

        if (retrofit == null) {
            final DownloadInterceptor interceptor = new DownloadInterceptor((progress, total, done) -> {
                //该方法仍然是在子线程，如果想要调用进度回调，需要切换到主线程，否则的话，会在子线程更新UI，直接错误
                //如果断电续传，重新请求的文件大小是从断点处到最后的大小，不是整个文件的大小，info中的存储的总长度是
                //整个文件的大小，所以某一时刻总文件的大小可能会大于从某个断点处请求的文件的总大小。此时read的大小为
                //之前读取的加上现在读取的
                if (info.getContentLength() > total) {
                    progress = progress + (info.getContentLength() - total);
                } else {
                    info.setContentLength(total);
                }
                info.setReadLength(progress);
                Observable
                        .just(1)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DefaultObserver<Integer>() {
                            @Override
                            public void onNext(Integer integer) {
                                if (progressListener != null) {
                                    progressListener.onProgress(info.getReadLength(), info.getContentLength(), done);
                                    int pro = (int) ((100 * info.getReadLength()) / info.getContentLength());
                                    TLog.d("progress:", String.format("%d%% done\n", pro));
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtils.showLong("下载过程出错：%s", e.getMessage());
                            }

                            @Override
                            public void onComplete() {
                                //此次完成只代表精度条走到100
                            }
                        });
            });
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(60, TimeUnit.SECONDS);
            builder.addInterceptor(interceptor);

            synchronized (com.bracks.futia.mylib.net.download.DownloadManager.class) {
                retrofit = new Retrofit
                        .Builder()
                        .client(builder.build())
                        .addConverterFactory(GsonConverterFactory.create(JsonUtil.getGsonBuilder().create()))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .baseUrl(host)
                        .build();
            }

            service = retrofit.create(BaseService.class);
            info.setService(service);
        }
        downLoad();
    }

    /**
     * 下载
     */
    private void downLoad() {
        download(
                downloadInfo -> TLog.d(TAG, "onNext ：" + JsonUtil.toJson(downloadInfo))
                , throwable -> TLog.d(TAG, "onError ：" + throwable.toString())
                , () -> TLog.d(TAG, "onCompleted ")
        );
    }

    /**
     * 下载
     *
     * @param onNext
     */
    private void downLoad(Consumer<? super DownloadInfo> onNext) {
        download(
                onNext
                , throwable -> TLog.d(TAG, "onError ：" + throwable.toString())
                , () -> TLog.d(TAG, "onCompleted ")
        );
    }

    /**
     * 下载
     *
     * @param onNext
     * @param onError
     */
    private void downLoad(Consumer<? super DownloadInfo> onNext, Consumer<? super Throwable> onError) {
        download(
                onNext
                , onError
                , () -> TLog.d(TAG, "onCompleted ")
        );
    }

    /**
     * 下载
     *
     * @param onNext
     * @param onError
     * @param onComplete
     */
    private void download(Consumer<? super DownloadInfo> onNext, Consumer<? super Throwable> onError, Action onComplete) {
        info.setState(DownloadInfo.DOENLOADING);
        disposable = service
                .download("bytes=" + info.getReadLength() + "-", info.getUrl())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .retryWhen(new RxRetryWithObservFunc())
                .map(responseBody -> DownloadUtils.writeCache(responseBody, outFile, info))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        onNext == null ? Functions.emptyConsumer() : onNext
                        , onError == null ? Functions.ON_ERROR_MISSING : onError
                        , onComplete == null ? Functions.EMPTY_ACTION : onComplete
                );
    }

    /**
     * 暂停下载
     */
    public void pause() {
        if (info.getState() == DownloadInfo.DOENLOADING) {
            info.setState(DownloadInfo.PAUSE);
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }

    /**
     * 停止下载
     */
    public void stop() {
        if (info.getState() != DownloadInfo.STOP) {
            retrofit = null;
            service = null;
            info.setContentLength(0);
            info.setReadLength(0);
            info.setState(DownloadInfo.STOP);
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }

    /**
     * 重新下载
     */
    public void reStart() {
        stop();
        start(info.getUrl());
    }
}
