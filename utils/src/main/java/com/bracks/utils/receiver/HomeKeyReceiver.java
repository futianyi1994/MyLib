package com.bracks.utils.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * good programmer.
 *
 * @date : 2019-01-30 上午 10:15
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 * IntentFilter filter = new IntentFilter();
 * filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
 * registerReceiver(new HomeKeyReceiver(), filter);
 */
public class HomeKeyReceiver extends BroadcastReceiver {
    public static final String SYSTEM_DIALOG_REASON_KEY = "reason";
    public static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
    public static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
    public static final String SYSTEM_DIALOG_REASON_ASSIST = "assist";
    public static final String SYSTEM_DIALOG_REASON_LOCK = "lock";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String extra = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
            switch (extra) {
                case SYSTEM_DIALOG_REASON_HOME_KEY:
                    if (callback != null) {
                        callback.homeKey();
                    }
                    break;
                case SYSTEM_DIALOG_REASON_RECENT_APPS:
                    if (callback != null) {
                        callback.recentApps();
                    }
                    break;
                case SYSTEM_DIALOG_REASON_ASSIST:
                    break;
                case SYSTEM_DIALOG_REASON_LOCK:
                    break;
                default:
                    break;
            }
        }
    }

    private ReceiverCallback callback;

    public void registerReceiver(Context context, ReceiverCallback callback) {
        this.callback = callback;
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.registerReceiver(this, filter);
    }

    public interface ReceiverCallback {
        void homeKey();

        void recentApps();
    }
}