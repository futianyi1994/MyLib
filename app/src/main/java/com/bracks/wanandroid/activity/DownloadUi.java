package com.bracks.wanandroid.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.bracks.mylib.base.BaseActivity;
import com.bracks.mylib.net.download.breakpoint.DownloadManager;
import com.bracks.mylib.net.https.ProgressListener;
import com.bracks.wanandroid.R;

import butterknife.BindView;
import butterknife.OnClick;

public class DownloadUi extends BaseActivity {


    @BindView(R.id.btnStartDownload)
    Button btnStartDownload;
    @BindView(R.id.btnContinueDownload)
    Button btnContinueDownload;
    @BindView(R.id.progress)
    ProgressBar progressBar;


    private DownloadManager downloadManager;


    @Override
    public int getLayoutId() {
        return R.layout.activity_download;
    }

    @Override
    protected boolean isTransparencyBar() {
        return false;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        downloadManager = DownloadManager.getInstance();
        downloadManager.setProgressListener(new ProgressListener() {
            @Override
            public void onProgress(long progress, long total, boolean done) {
                final int pro = (int) (100 * progress / total);
                progressBar.setProgress(pro);
            }
        });

    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
    }

    @OnClick({R.id.btnStartDownload, R.id.btnContinueDownload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnStartDownload:
                if (downloadManager.isStop()) {
                    downloadManager.start("http://192.168.1.239:8000/amy-server/sys-update/a2/qadbb/20190404/sdqldspro.apk");
                    btnStartDownload.setText("暂停下载");
                } else if (downloadManager.isDownloading()) {
                    downloadManager.pause();
                    btnStartDownload.setText("继续下载");
                } else if (downloadManager.isPause()) {
                    downloadManager.start("http://192.168.1.239:8000/amy-server/sys-update/a2/qadbb/20190404/sdqldspro.apk");
                    btnStartDownload.setText("暂停下载");
                }
                break;
            case R.id.btnContinueDownload:
                downloadManager.reStart();
                break;
            default:
                break;
        }
    }
}
