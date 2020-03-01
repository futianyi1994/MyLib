package com.bracks.utils.util;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

/**
 * good programmer.
 *
 * @date : 2019-04-01 下午 02:54
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class KeyboardUtils {
    /**
     * 计算软键盘高度的回调监听
     */
    public interface IKeyBoardVisibleListener {
        void onSoftKeyBoardVisible(boolean visible, int windowBottom);
    }

    static boolean isVisiableForLast = false;

    public static void addOnSoftKeyBoardVisibleListener(Activity activity, final IKeyBoardVisibleListener listener) {
        final View decorView = activity.getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                //计算出可见屏幕的高度
                int displayHight = rect.bottom - rect.top;
                //获得屏幕整体的高度
                int hight = decorView.getHeight();
                //获得键盘高度
                int keyboardHeight = hight - displayHight;
                boolean visible = (double) displayHight / hight < 0.8;
                if (visible != isVisiableForLast) {
                    listener.onSoftKeyBoardVisible(visible, keyboardHeight);
                }
                isVisiableForLast = visible;
            }
        });
    }

    /**
     * 复制文字
     *
     * @param context
     * @param string
     */
    public static void copyTextToBoard(Context context, String string) {
        if (TextUtils.isEmpty(string)) {
            return;
        }
        ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clip.setText(string);
        Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
    }
}
