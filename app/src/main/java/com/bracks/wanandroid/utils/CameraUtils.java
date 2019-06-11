package com.bracks.wanandroid.utils;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.os.Build;
import android.util.Log;

import com.blankj.utilcode.util.ScreenUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * good programmer.
 *
 * @data : 2018\4\17 0017 15:00
 * @author: Administrator
 * @email : futianyi1994@126.com
 * @description :
 */
public class CameraUtils {
    public static final String TAG = "CameraUtils";
    private static volatile CameraUtils singleton = null;

    /**
     * 单利构造器私有化
     */
    private CameraUtils() {
    }

    /**
     * 对外唯一实例的接口
     */
    public static CameraUtils getInstance() {
        if (singleton == null) {
            synchronized (CameraUtils.class) {
                if (singleton == null) {
                    singleton = new CameraUtils();
                }
            }
        }
        return singleton;
    }


    public static int getSdkVersion() {
        return Build.VERSION.SDK_INT;
    }

    private static boolean checkCameraFacing(final int facing) {
        if (getSdkVersion() < Build.VERSION_CODES.GINGERBREAD) {
            return false;
        }
        final int cameraCount = Camera.getNumberOfCameras();
        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, info);
            if (facing == info.facing) {
                return true;
            }
        }
        return false;
    }

    /**
     *  
     * 检查设备是否有摄像头 
     *
     * @return true, 有相机；false,无相机
     *      
     */
    public static boolean hasCamera() {
        return hasBackFacingCamera() || hasFrontFacingCamera();
    }

    /**
     * 检查设备是否有后置摄像头 
     *
     * @return true, 有后置摄像头；false,后置摄像头      
     */

    public static boolean hasBackFacingCamera() {
        return checkCameraFacing(0);
    }

    /**
     * 检查设备是否有前置摄像头 
     *
     * @return true, 有前置摄像头；false,前置摄像头
     *  
     */
    public static boolean hasFrontFacingCamera() {
        return checkCameraFacing(1);
    }

    /**
     * 判断相机是否可用
     *
     * @return true, 相机驱动可用，false,相机驱动不可用
     *      
     */
    public static boolean isCameraCanUse() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            canUse = false;
        }
        if (canUse) {
            mCamera.release();
            mCamera = null;
        }
        return canUse;
    }

    /**
     * 调用照相机
     *
     * @param context
     */
    public static void openCamera(Context context) {
        Intent intent = new Intent();
        intent.setAction("android.media.action.STILL_IMAGE_CAMERA");
        intent.setFlags(0x34c40000);
        context.startActivity(intent);
    }

    /**
     * 读取照片exif信息中的旋转角度
     *
     * @param path 照片路径
     * @return角度
     */
    public int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    degree = 0;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 通过对比得到与宽高比最接近的尺寸（如果有相同尺寸，优先选择）
     *
     * @param surfaceWidth  需要被进行对比的原宽
     * @param surfaceHeight 需要被进行对比的原高
     * @param preSizeList   需要对比的预览尺寸列表
     * @return 得到与原宽高比例最接近的尺寸
     */
    public Camera.Size getCloselyPreSize(int surfaceWidth, int surfaceHeight, List<Camera.Size> preSizeList) {

        Collections.sort(preSizeList, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size size, Camera.Size t1) {
                //升序
                return size.width - t1.width;
                //降序
                //return size.width - t1.width;
            }
        });

        int reqTmpWidth;
        int reqTmpHeight;
        // 当屏幕为垂直的时候需要把宽高值进行调换，保证宽大于高
        if (ScreenUtils.isPortrait()) {
            reqTmpWidth = surfaceHeight;
            reqTmpHeight = surfaceWidth;
        } else {
            reqTmpWidth = surfaceWidth;
            reqTmpHeight = surfaceHeight;
        }
        //先查找preview中是否存在与surfaceview相同宽高的尺寸
        for (Camera.Size size : preSizeList) {
            if ((size.width == reqTmpWidth) && (size.height == reqTmpHeight)) {
                return size;
            }
        }

        // 得到与传入的宽高比最接近的size
        float reqRatio = ((float) reqTmpWidth) / reqTmpHeight;
        float curRatio, deltaRatio;
        float deltaRatioMin = Float.MAX_VALUE;
        Camera.Size retSize = null;
        for (Camera.Size size : preSizeList) {
            curRatio = ((float) size.width) / size.height;
            deltaRatio = Math.abs(reqRatio - curRatio);
            if (deltaRatio < deltaRatioMin) {
                deltaRatioMin = deltaRatio;
                retSize = size;
            }
        }

        return retSize;
    }

    /**
     * 通过对比得到与宽高比最接近的尺寸（如果有相同尺寸，优先选择）
     *
     * @param surfaceWidth  需要被进行对比的原宽
     * @param surfaceHeight 需要被进行对比的原高
     * @param picSizeList   需要对比的拍照尺寸列表
     * @return 得到与原宽高比例最接近的尺寸
     */
    public Camera.Size getCloselyPicSize(int surfaceWidth, int surfaceHeight, List<Camera.Size> picSizeList) {

        Collections.sort(picSizeList, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size size, Camera.Size t1) {
                //升序
                return size.width - t1.width;
                //降序
                //return size.width - t1.width;
            }
        });

        int reqTmpWidth;
        int reqTmpHeight;
        // 当屏幕为垂直的时候需要把宽高值进行调换，保证宽大于高
        if (ScreenUtils.isPortrait()) {
            reqTmpWidth = surfaceHeight;
            reqTmpHeight = surfaceWidth;
        } else {
            reqTmpWidth = surfaceWidth;
            reqTmpHeight = surfaceHeight;
        }
        for (Camera.Size size : picSizeList) {
            //先查找preview中是否存在与surfaceview相同宽高的尺寸
            if ((size.width == reqTmpWidth) && (size.height == reqTmpHeight)) {
                return size;
            }
        }

        // 得到与传入的宽高比最接近的size
        float curRatio;
        float deltaRatioMin = Float.MAX_VALUE;
        Camera.Size retSize = null;
        for (Camera.Size size : picSizeList) {
            curRatio = Math.abs(surfaceWidth - size.height);
            if (curRatio <= deltaRatioMin) {
                deltaRatioMin = curRatio;
                retSize = size;
            }
        }
        Camera.Size size = retSize;

        return retSize;
    }


    /**
     * 取的合适的预览尺寸（如果没找到，就选最大的size）
     *
     * @param list
     * @param minWidth
     * @param minHeight
     * @return
     */
    public Camera.Size getPropPreviewSize(List<Camera.Size> list, int minWidth, int minHeight) {
        Collections.sort(list, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size size, Camera.Size t1) {
                //升序
                return size.width - t1.width;
                //降序
                //return size.width - t1.width;
            }
        });
        Log.i(TAG, "PreviewSize : minWidth = " + minWidth);
        int i = 0;
        for (Camera.Size s : list) {
            Log.i(TAG, "PreviewSize : width = " + s.width + "height = " + s.height);
            if ((s.height == minWidth) && s.width >= minHeight) {
                Log.i(TAG, "PreviewSize : w = " + s.width + "h = " + s.height);
                break;
            }
            i++;
        }
        if (i == list.size()) {
            //如果没找到，就选最大的size
            i = list.size() - 1;
        }
        return list.get(i);
    }

    /**
     * 取的合适的拍照尺寸（如果没找到，就选最大的size）
     *
     * @param list
     * @param minWidth
     * @param minHeight
     * @return
     */
    public Camera.Size getPropPictureSize(List<Camera.Size> list, int minWidth, int minHeight) {
        Collections.sort(list, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size size, Camera.Size t1) {
                //升序
                return size.width - t1.width;
                //降序
                //return size.width - t1.width;
            }
        });

        int i = 0;
        for (Camera.Size s : list) {
            Log.i(TAG, "PreviewSize : width = " + s.width + "height = " + s.height);
            if (s.height == minHeight && s.width == minWidth) {
                Log.i(TAG, "PreviewSize : w = " + s.width + "h = " + s.height);
                break;
            }
            i++;
        }
        if (i == list.size()) {
            //如果没找到，就选最大的size
            i = list.size() - 1;
        }
        return list.get(i);
    }
}

