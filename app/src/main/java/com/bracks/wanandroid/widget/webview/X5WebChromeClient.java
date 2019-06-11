package com.bracks.wanandroid.widget.webview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bracks.mylib.utils.widget.DialogUtils;
import com.bracks.wanandroid.utils.CameraUtils;
import com.bracks.wanandroid.utils.ImageSavaUtils;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

import java.io.File;

import static android.app.Activity.RESULT_OK;

/**
 * good programmer.
 *
 * @date : 2018-09-04 21:15
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class X5WebChromeClient extends WebChromeClient {
    private Context mContext;
    private Activity mActivity;

    private Dialog dialog;
    private boolean isShowDialog;

    private ValueCallback<Uri> uploadMsg;
    public ValueCallback<Uri[]> filePathCallback;
    /**
     * 拍照保存h5照片的文件夹名
     */
    public static final String LOCAL_H5_TAKE_PHOTO = "h5Photo";
    public static final int REQUEST_SHOW_FILE = 100;
    private final static int REQUEST_OPEN_FILE = 2;

    public X5WebChromeClient(Context mContext, boolean isShowDialog) {
        this.mContext = mContext;
        if (mContext instanceof Activity) {
            mActivity = (Activity) mContext;
        }
        this.isShowDialog = isShowDialog;
    }

    public Dialog getDialog() {
        return dialog;
    }

    @Override
    public void onProgressChanged(WebView webView, int i) {
        super.onProgressChanged(webView, i);
        if (dialog == null && isShowDialog && mActivity != null && !mActivity.isFinishing()) {
            dialog = DialogUtils.createLoadingDialog(mContext, "加载中", true);
            dialog.show();
        } else if (i == 100) {
            dialogDismiss();
        }
    }

    /**
     * For Lollipop 5.0+ Devices
     *
     * @param mWebView
     * @param filePathCallback
     * @param fileChooserParams
     * @return
     */
    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        if (X5WebChromeClient.this.filePathCallback != null) {
            X5WebChromeClient.this.filePathCallback.onReceiveValue(null);
            X5WebChromeClient.this.filePathCallback = null;
        }
        X5WebChromeClient.this.filePathCallback = filePathCallback;

        try {
            mActivity.startActivityForResult(ImageSavaUtils.createCameraAndChooserIntent(LOCAL_H5_TAKE_PHOTO), REQUEST_SHOW_FILE);
        } catch (ActivityNotFoundException e) {
            X5WebChromeClient.this.filePathCallback = null;
            ToastUtils.showLong("Cannot Open File Chooser");
            return false;
        }
        return true;
    }

    /**
     * For Android 4.1 only
     *
     * @param uploadMsg
     * @param acceptType
     * @param capture
     */
    @Override
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        X5WebChromeClient.this.uploadMsg = uploadMsg;
        mActivity.startActivityForResult(ImageSavaUtils.createCameraAndChooserIntent(LOCAL_H5_TAKE_PHOTO), REQUEST_OPEN_FILE);
    }

    /**
     * 选择文件后回调
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SHOW_FILE:
                    if (filePathCallback == null) {
                        return;
                    }
                    Uri[] results;
                    if (intent == null) {
                        //针对相机中选择的图片处理
                        File file = new File(ImageSavaUtils.mCameraFilePath);

                        //获取Bitmap
                        Bitmap bitmap = ImageUtils.getBitmap(ImageSavaUtils.mCameraFilePath);
                        int degree = CameraUtils.getInstance().readPictureDegree(ImageSavaUtils.mCameraFilePath);
                        //获得图片动态旋转角度然后缩放后保存
                        Bitmap bMapRotate = ImageUtils.rotate(bitmap, degree, 0, 0);
                        //质量压缩
                        Bitmap compressBitmap = ImageUtils.compressByQuality(bMapRotate, 20);
                        //保存图片
                        ImageSavaUtils.saveImage(mContext, LOCAL_H5_TAKE_PHOTO, compressBitmap);

                        results = new Uri[]{Uri.fromFile(file)};
                    } else {
                        //针对从文件中选图片的处理
                        results = FileChooserParams.parseResult(resultCode, intent);
                    }

                    filePathCallback.onReceiveValue(results);
                    filePathCallback = null;
                    ImageSavaUtils.mCameraFilePath = null;
                    break;
                case REQUEST_OPEN_FILE:
                    if (uploadMsg == null) {
                        return;
                    }

                    Uri result = intent == null ? null : intent.getData();
                    uploadMsg.onReceiveValue(result);
                    uploadMsg = null;
                    break;
                default:
                    ToastUtils.showLong("Failed to Upload Image");
                    break;
            }
        } else {
            if (filePathCallback != null) {
                filePathCallback.onReceiveValue(null);
            }
            if (uploadMsg != null) {
                uploadMsg.onReceiveValue(null);
            }
        }
    }

    /**
     * 销毁dialog
     */
    public void dialogDismiss() {
        if (dialog != null && mActivity != null && !mActivity.isFinishing()) {
            dialog.dismiss();
        }
    }
}
