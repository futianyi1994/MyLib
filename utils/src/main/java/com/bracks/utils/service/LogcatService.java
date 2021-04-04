package com.bracks.utils.service;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.bracks.utils.Constants;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * good programmer.
 *
 * @date : 2019-01-22 上午 09:56
 * @author: futia
 * @email : futianyi1994@126.com
 * @description : 需要添加权限<uses-permission android:name="android.permission.READ_LOGS" />
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 */
public class LogcatService extends Service {
    public static final String TAG = "LogcatService";
    private Handler handler;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread mHandlerThread = new HandlerThread("LogcatService-thread");
        mHandlerThread.start();
        handler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                log2();
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.sendEmptyMessage(0);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 方法2:个人觉得这个方法更实用
     * 日志等级：*:v , *:d , *:w , *:e , *:f , *:s
     * // cmds = "logcat *:e *:w | grep \"(" + mPID + ")\"";//当前mPID程序的 E和W等级的日志.
     * // cmds = "logcat  | grep \"(" + mPID + ")\"";//打印所有日志信息
     * // cmds = "logcat -s way";//打印标签过滤信息
     */
    private void log2() {
        Log.i(TAG, "log2 start");
        String[] cmds = {"logcat", "-c"};
        //adb logcat -v time *:W
        String shellCmd = "logcat -v time | grep";
        Process process = null;
        Runtime runtime = Runtime.getRuntime();
        BufferedReader reader = null;
        try {
            runtime.exec(cmds).waitFor();
            process = runtime.exec(shellCmd);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.contains(String.valueOf(android.os.Process.myPid()))) {
                    //line = new String(line.getBytes("iso-8859-1"), "utf-8");
                    writeTofile(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "log2 finished");
    }

    /**
     * 方法1
     * 日志等级：*:v , *:d , *:w , *:e , *:f , *:s
     * 日志等级：*:v , *:d , *:w , *:e , *:f , *:s
     * // cmds = "logcat *:e *:w | grep \"(" + mPID + ")\"";//当前mPID程序的 E和W等级的日志.
     * // cmds = "logcat  | grep \"(" + mPID + ")\"";//打印所有日志信息
     * // cmds = "logcat -s way";//打印标签过滤信息
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void log() {
        Log.i(TAG, "log start");
        String[] cmds = {"logcat", "-c"};
        //adb logcat -v time *:W
        String shellCmd = "logcat -v time -s *:W ";
        Process process = null;
        InputStream is = null;
        DataInputStream dis = null;
        String line = "";
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(cmds);
            process = runtime.exec(shellCmd);
            is = process.getInputStream();
            dis = new DataInputStream(is);
            // String filter = GetPid();
            String filter = android.os.Process.myPid() + "";
            //这里如果输入流没断，会一直循环下去。
            while ((line = dis.readLine()) != null) {
                line = new String(line.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                if (line.contains(filter)) {
                    int pos = line.indexOf(":");
                    Log.i(TAG, line + "");
                    writeTofile(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "log finished");
    }

    /**
     * 写入文件
     *
     * @param line
     */
    private void writeTofile(String line) {
        //如果SD卡不存在或无法使用，则无法将异常信息写入SD卡
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.w(TAG, "sdcard unmounted,skip dump exception");
            return;
        }
        String pathname = Constants.LOCAL_FILE_PATH + "logcat";
        File dir = new File(pathname);
        //如果目录下没有文件夹，就创建文件夹
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(pathname, "myLog.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        FileOutputStream fos;
        String content = line + "\r\n";
        try {
            fos = new FileOutputStream(file, true);
            fos.write(content.getBytes());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }
}
