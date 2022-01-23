package com.bracks.utils.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;

import com.blankj.utilcode.constant.MemoryConstants;
import com.blankj.utilcode.util.ConvertUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * good programmer.
 *
 * @date : 2021-02-25 15:07
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class TrafficStatsService extends LifecycleService {
    private static final String TAG = "TrafficStatsService";
    private static final int COUNT = 1;
    private static final List<Callback> CALLBACKS = new ArrayList<>();
    private long totalData;
    private MyHandler mHandler;
    private int uid;
    /**
     * 定义线程周期性地获取网速
     */
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            //定时器
            mHandler.postDelayed(mRunnable, COUNT * 1000);
            Message msg = mHandler.obtainMessage();
            msg.what = 1;
            msg.arg1 = getNetSpeed();
            mHandler.sendMessage(msg);
        }
    };

    public static void addCallback(Callback callback) {
        CALLBACKS.add(callback);
    }

    public static void removeCallback(@Nullable Callback callback) {
        if (callback != null) {
            CALLBACKS.remove(callback);
        } else {
            CALLBACKS.clear();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        uid = Process.myUid();
        totalData = getUidRxBytes(uid);
        mHandler = new MyHandler(this);
        mHandler.postDelayed(mRunnable, 0);
        Log.i(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "channel_nav_bar";
            String channelName = "navBar";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.createNotificationChannel(notificationChannel);
            }
            //启动前台服务提高服务优先级
            Notification notification = new NotificationCompat
                    .Builder(this, channelId)
                    .build();
            startForeground(1, notification);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 在服务结束时删除消息队列
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        removeCallback(null);
        mHandler.removeCallbacks(mRunnable);
        stopForeground(true);
    }

    /**
     * 核心方法，得到当前网速
     *
     * @return
     */
    private int getNetSpeed() {
        long currentTrafficData = getUidRxBytes(uid);
        long trafficData = currentTrafficData - totalData;
        totalData = currentTrafficData;
        return (int) trafficData / COUNT;
    }

    private long getUidRxBytes(int uid) {
        return TrafficStats.getUidRxBytes(uid) == TrafficStats.UNSUPPORTED ? 0 : TrafficStats.getUidRxBytes(uid);
    }

    public interface Callback {
        void onSpeed(String speed);
    }

    private static class MyHandler extends Handler {
        private WeakReference<TrafficStatsService> weakReference;
        private TrafficStatsService service;

        private MyHandler(TrafficStatsService mActivity) {
            weakReference = new WeakReference<>(mActivity);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void handleMessage(Message msg) {
            service = weakReference.get();
            if (weakReference == null || service == null) {
                return;
            }
            if (msg.what == 1) {
                String speed;
                if (msg.arg1 / 1024 / 1024 > 0) {
                    speed = (int) (ConvertUtils.byte2MemorySize(msg.arg1, MemoryConstants.MB) * 100) / 100.00 + "MB/s";
                } else if (msg.arg1 / 1024 > 0) {
                    speed = (int) ConvertUtils.byte2MemorySize(msg.arg1, MemoryConstants.KB) + "KB/s";
                } else {
                    speed = (int) ConvertUtils.byte2MemorySize(msg.arg1, MemoryConstants.BYTE) + "Byte/s";
                }
                CALLBACKS.forEach(callback -> callback.onSpeed(speed));
            }
            super.handleMessage(msg);
        }
    }
}
