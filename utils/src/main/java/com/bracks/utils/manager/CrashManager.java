package com.bracks.utils.manager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.blankj.utilcode.util.TimeUtils;
import com.bracks.utils.Constants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * good programmer.
 *
 * @date : 2019-01-07 上午 09:34
 * @author: futia
 * @email : futianyi1994@126.com
 * @description : 应用异常处理类
 * 使用方式： 在Application 中初始化  CrashManager.getInstance().init(this);
 */
public class CrashManager implements UncaughtExceptionHandler {
    private static final String TAG = "CrashManager";
    public static final boolean DEBUG = true;

    private static CrashManager sInstance = new CrashManager();
    private UncaughtExceptionHandler mDefaultCrashHandler;
    private Context mContext;


    private CrashManager() {

    }

    public static CrashManager getInstance() {
        return sInstance;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        //得到系统的应用异常处理器
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        //将当前应用异常处理器改为默认的
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context.getApplicationContext();
    }

    /**
     * 这个是最关键的函数，当系统中有未被捕获的异常，系统将会自动调用 uncaughtException 方法
     *
     * @param thread 为出现未捕获异常的线程
     * @param ex     为未捕获的异常 ，可以通过e 拿到异常信息
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        //导入异常信息到SD卡中
        try {
            dumpExceptionToSDCard(ex);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //这里可以上传异常信息到服务器，便于开发人员分析日志从而解决Bug
        uploadExceptionToServer();
        ex.printStackTrace();
        //如果系统提供了默认的异常处理器，则交给系统去结束程序，否则就由自己结束自己
        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(thread, ex);
        } else {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    /**
     * 将异常信息写入SD卡
     *
     * @param e
     */
    private void dumpExceptionToSDCard(Throwable e) throws IOException {
        //如果SD卡不存在或无法使用，则无法将异常信息写入SD卡
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (DEBUG) {
                Log.w(TAG, "sdcard unmounted,skip dump exception");
                return;
            }
        }
        String pathname = Constants.LOCAL_FILE_PATH + "crashlogs";
        File dir = new File(pathname);
        //如果目录下没有文件夹，就创建文件夹
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //获取当前时间
        String currentTime = TimeUtils.getNowString();
        //在定义的crashlogs文件夹下创建文件
        File file = new File(pathname, String.format("%s_" + currentTime + ".trace", "crash"));

        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            //写入时间
            pw.println(currentTime);
            //写入手机信息
            dumpPhoneInfo(pw);
            //换行
            pw.println();
            e.printStackTrace(pw);
            //关闭输入流
            pw.close();
        } catch (Exception e1) {
            Log.e(TAG, "dump crash info failed : " + e1.getMessage());
        }
    }

    /**
     * 将错误信息上传至服务器
     */
    private void uploadExceptionToServer() {
    }

    /**
     * 获取手机各项信息
     *
     * @param pw
     */
    private void dumpPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {
        //得到包管理器
        PackageManager pm = mContext.getPackageManager();
        //得到包对象
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        //写入APP版本号
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print("_");
        pw.println(pi.versionCode);
        //写入 Android 版本号
        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);
        //手机制造商
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);
        //手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);
        //CPU架构
        pw.print("CPU ABI: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pw.println(Build.SUPPORTED_ABIS);
        } else {
            pw.println(Build.CPU_ABI);
        }
    }
}
