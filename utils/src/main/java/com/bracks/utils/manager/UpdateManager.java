package com.bracks.utils.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * good programmer.
 *
 * @date : 2018/10/25 19:51
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class UpdateManager {

    public static final int INSTALL_PERMISS_CODE = 20000;
    private static volatile UpdateManager instance = null;
    public boolean stopFlag = false;
    private int length;
    private int progress;
    private TextView progressTv;
    private final MyHandler handler = new MyHandler(this);

    /**
     * 单利构造器私有化
     */
    private UpdateManager() {
    }

    /**
     * 对外唯一实例的接口
     */
    public static final UpdateManager getInstance() {
        if (instance == null) {
            synchronized (UpdateManager.class) {
                if (instance == null) {
                    instance = new UpdateManager();
                }
            }
        }
        return instance;
    }

    /**
     * 安装APK文件
     */
    public void installApk(Activity activity, File apkfile) {
        if (!apkfile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(activity, activity.getApplication().getPackageName() + ".fileProvider", apkfile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            List<ResolveInfo> resInfoList = activity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            //然后全部授权
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                activity.grantUriPermission(packageName, contentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            //兼容8.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                boolean hasInstallPermission = activity.getPackageManager().canRequestPackageInstalls();
                if (!hasInstallPermission) {
                    ToastUtils.showShort("请在未知应用列表中信任此应用");
                    startInstallPermissionSettingActivity(activity);
                    return;
                }
            }
        } else {
            intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        activity.startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity(Activity activity) {
        //注意这个是8.0新API
        //提供的需要设置包名，去打开权限设置界面才能在onActivityResult中接收到【resultCode 等于 RESULT_OK 】
        Uri packageURI = Uri.parse("package:" + activity.getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        activity.startActivityForResult(intent, INSTALL_PERMISS_CODE);
    }

    /**
     * 下载apk
     *
     * @param httpUrl
     * @param activity
     * @param seekBar
     * @param progressTv
     * @return
     */
    public File downLoadFile(String httpUrl, Activity activity, File file, ProgressBar seekBar, TextView progressTv) {
        this.progressTv = progressTv;
        if (FileUtils.isFileExists(file)) {
            FileUtils.delete(file);
        } else {
            FileUtils.createOrExistsFile(file);
        }
        try {
            URL url = new URL(httpUrl);
            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream is = conn.getInputStream();
                FileOutputStream fos = new FileOutputStream(file);
                // 获取文件大小
                length = conn.getContentLength();
                byte[] buf = new byte[256];
                conn.connect();
                double count = 0;
                if (conn.getResponseCode() >= 400) {
                    ToastUtils.showShort("连接超时");
                } else {
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        if (seekBar != null) {
                            seekBar.setProgress(progress);
                            handler.sendEmptyMessage(0);
                        }
                        // 更新进度
                        if (numread <= 0) {
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (progress != 100 && !stopFlag);
                }
                if (!stopFlag) {
                    installApk(activity, file);
                }
                conn.disconnect();
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * @param context
     * @param archiveFilePath 安装包路径
     * @return
     */
    public int readNativeAPkInfo(Context context, String archiveFilePath) {
        PackageManager pm = context.getPackageManager();
        int versionCode = 0;
        PackageInfo info = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_PROVIDERS);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            String appName = pm.getApplicationLabel(appInfo).toString();
            //得到安装包名称
            String packageName = appInfo.packageName;
            //得到版本信息
            String version = info.versionName;
            versionCode = info.versionCode;
        }
        return versionCode;
    }

    private static class MyHandler extends Handler {
        private final WeakReference<UpdateManager> weakReference;

        private UpdateManager mActivity;

        private MyHandler(UpdateManager mActivity) {
            weakReference = new WeakReference<>(mActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            mActivity = weakReference.get();
            if (weakReference == null || mActivity == null) {
                return;
            }
            switch (msg.what) {
                case 0:
                    mActivity.progressTv.setText(mActivity.progress + "%");
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
