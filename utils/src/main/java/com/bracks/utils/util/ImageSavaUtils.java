package com.bracks.utils.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.bracks.utils.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * good programmer.
 *
 * @date : 2018-09-03 15:43
 * @author: futia
 * @email : futianyi1994@126.com
 * @description : 图片保存工具类
 */
public class ImageSavaUtils {
    /**
     * 保存文件到指定路径
     *
     * @param context
     * @param dirN
     * @param fileN
     * @param bmp
     * @return
     */
    public static boolean saveImage(Context context, File storageDir, String dirN, String fileN, Bitmap bmp, int quality) {
        if (TextUtils.isEmpty(fileN)) {
            fileN = System.currentTimeMillis() + ".jpg";
        }
        if (storageDir == null) {
            storageDir = Environment.getExternalStorageDirectory();
        }
        File fileroot = new File(storageDir.getPath() + File.separator + dirN);
        if (!fileroot.exists()) {
            //创建文件夹
            fileroot.mkdirs();
        }
        //创建文件
        File file = new File(fileroot.getPath() + File.separator + fileN);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.flush();
            fos.close();
            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileN, null);

            FileUtils.scanFileAsync(context, file);
            return isSuccess;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean saveImage(Context context, String dirN, Bitmap bmp) {
        return saveImage(context, null, dirN, null, bmp, 100);
    }

    public static boolean saveImage(Context context, String dirN, String fileN, Bitmap bmp) {
        return saveImage(context, null, dirN, fileN, bmp, 100);
    }

    public static boolean saveImage(Context context, File storageDir, String dirN, Bitmap bmp) {
        return saveImage(context, storageDir, dirN, null, bmp, 100);
    }

    public static boolean saveImage(Context context, String dirN, Bitmap bmp, int quality) {
        return saveImage(context, null, dirN, null, bmp, quality);
    }

    public static boolean saveImage(Context context, String dirN, String fileN, Bitmap bmp, int quality) {
        return saveImage(context, null, dirN, fileN, bmp, quality);
    }

    public static boolean saveImage(Context context, File storageDir, String dirN, Bitmap bmp, int quality) {
        return saveImage(context, storageDir, dirN, null, bmp, quality);
    }

    /***************************************************************************************************************************/

    /**
     * 获取手机中全部的图片（修改时间降序，最近修改在前。传null为默认最近修改在最后面）
     *
     * @param context
     * @return
     */
    public static List<String> getAllPhoto(Context context) {
        List<String> filePaths = new ArrayList<>();
        Cursor cursor = context
                .getContentResolver()
                .query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        null,
                        null,
                        null,
                        MediaStore.Images.Media.DATE_MODIFIED + " desc"
                );
        while (cursor.moveToNext()) {
            byte[] data = cursor.getBlob(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            String filePath = "file://" + new String(data, 0, data.length - 1);
            filePaths.add(filePath);
        }
        return filePaths;
    }

    /****************************************************************************************************************************/

    /**
     * 拍照的图片地址
     */
    public static String mCameraFilePath;

    /**
     * 生成一个选择本地相机和文件的选择
     **/
    public static Intent createCameraAndChooserIntent(String dirN) {
        //允许用户选择特殊种类的数据，并返回（特殊种类的数据：照一张相片或录一段音）
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //增加一个可打开的分类
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");

        Intent chooser = createChooserIntent(createCameraIntent(dirN));
        //意图描述您希望显示的选项
        chooser.putExtra(Intent.EXTRA_INTENT, intent);
        return chooser;
    }

    public static Intent createChooserIntent(Intent... intents) {
        //显示一个Activity选择器
        Intent chooser = new Intent(Intent.ACTION_CHOOSER);
        //将intents(相机)设置为额外初始化intent
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);
        //标题
        chooser.putExtra(Intent.EXTRA_TITLE, "选择图片来源");
        return chooser;
    }

    public static Intent createCameraIntent(String dirN) {
        //启动系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //改变相机拍摄的图片的路径
        File file = new File(Constants.LOCAL_FILE_PATH + dirN);
        if (!file.exists()) {
            //创建文件夹
            file.mkdirs();
        }
        mCameraFilePath = file.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mCameraFilePath)));
        return intent;
    }
}
