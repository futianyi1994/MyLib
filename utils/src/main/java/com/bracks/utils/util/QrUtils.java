package com.bracks.utils.util;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.util.Hashtable;

/**
 * good programmer.
 *
 * @date : 2020-11-26 17:11
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class QrUtils {
    private static final String TAG = "QRUtils";

    public static Bitmap createQrImage(String url, int width, int height) {
        long stime = System.currentTimeMillis();
        Log.i(TAG, "createQrImage start");

        try {
            Hashtable<EncodeHintType, Object> hints = new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix matrix = (new MultiFormatWriter()).encode(url, BarcodeFormat.QR_CODE, width, height, hints);
            int mWidth = matrix.getWidth();
            int mHeight = matrix.getHeight();
            int[] pixels = new int[width * height];
            boolean isFirstBlackPoint = false;
            int startX = 0;
            int startY = 0;

            int x;
            for (int y = 0; y < mHeight; ++y) {
                for (x = 0; x < mWidth; ++x) {
                    if (matrix.get(x, y)) {
                        if (!isFirstBlackPoint) {
                            isFirstBlackPoint = true;
                            startX = x;
                            startY = y;
                        }

                        pixels[y * width + x] = -16777216;
                    }
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, mWidth, mHeight);
            if (startX <= 1) {
                return bitmap;
            } else {
                x = startX - 1;
                int y1 = startY - 1;
                if (x >= 0 && y1 >= 0) {
                    int w1 = width - x * 2;
                    int h1 = height - y1 * 2;
                    Bitmap bitmapQr = Bitmap.createBitmap(bitmap, x, y1, w1, h1);
                    Log.i(TAG, "createQrImage end duration: " + (System.currentTimeMillis() - stime));
                    return bitmapQr;
                } else {
                    return bitmap;
                }
            }
        } catch (Exception var19) {
            return null;
        }
    }

    public static Bitmap createQrImage(String content) {
        return createQrImage(content, 200, 200);
    }
}
