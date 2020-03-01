package com.bracks.utils.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * good programmer.
 *
 * @data : 2018\4\9 15:33
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class ImageUtils {
    public static final String TAG = "ImageUtils";

    /**
     * 根据给定的宽度和高度动态计算图片压缩比率
     *
     * @param options   Bitmap配置文件
     * @param reqWidth  需要压缩到的宽度
     * @param reqHeight 需要压缩到的高度
     * @return 压缩比
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /****************************************************获取图片*****************************************************/

    /**
     * 得到bitmap
     *
     * @param imgPath
     * @return
     */
    public static Bitmap getBitmap(String imgPath) {
        if (imgPath.contains("file://")) {
            imgPath = StringUtils.replaceStr(imgPath, "file://");
        }
        // Get bitmap through image path
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = false;
        newOpts.inPurgeable = true;
        newOpts.inInputShareable = true;
        // Do not compress
        newOpts.inSampleSize = 1;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(imgPath, newOpts);
    }

    /**
     * 得到压缩后的bitmap,采用采样率压缩
     *
     * @param imgPath
     * @param sampleSize
     * @return
     */
    public static Bitmap getCompressBitmap(String imgPath, int sampleSize) {
        if (imgPath.contains("file://")) {
            imgPath = StringUtils.replaceStr(imgPath, "file://");
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imgPath, options);
    }

    /**
     * 得到压缩后的bitmap,（采用质量压缩)
     *
     * @param image
     * @return
     */
    public static Bitmap getCompressBitmap(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        //循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > 200) {
            //重置baos即清空baos
            baos.reset();
            //这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options < 0 ? 10 : options, baos);
            //每次都减少10
            options -= 10;
        }
        //把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        //把ByteArrayInputStream数据生成图片
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }

    /**
     * 将图片根据压缩比压缩成固定宽高的Bitmap，实际解析的图片大小可能和#reqWidth、#reqHeight不一样。
     *
     * @param imgPath   图片地址
     * @param reqWidth  需要压缩到的宽度
     * @param reqHeight 需要压缩到的高度
     * @return Bitmap 试用于二维码识别大图时压缩基本等同 ratio(String imgPath, float pixelW, float pixelH)
     */
    public static Bitmap getCompressBitmapAsSampleSize(String imgPath, int reqWidth, int reqHeight) {
        if (imgPath.contains("file://")) {
            imgPath = StringUtils.replaceStr(imgPath, "file://");
        }
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgPath, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imgPath, options);
    }

    /**
     * Compress image by pixel, this will modify image width/height.
     * Used to get thumbnail
     *
     * @param imgPath image path
     * @param pixelW  target pixel of width
     * @param pixelH  target pixel of height
     * @return
     */
    public static Bitmap ratio(String imgPath, float pixelW, float pixelH) {
        if (imgPath.contains("file://")) {
            imgPath = StringUtils.replaceStr(imgPath, "file://");
        }
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true，即只读边不读内容
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        // Get bitmap info, but notice that bitmap is null now
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 想要缩放的目标尺寸
        // 设置高度为240f时，可以明显看到图片缩小了
        float hh = pixelH;
        // 设置宽度为120f，可以明显看到图片缩小了
        float ww = pixelW;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        //be=1表示不缩放
        int be = 1;
        if (w > h && w > ww) {
            //如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            //如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        //设置缩放比例
        newOpts.inSampleSize = be;
        // 开始压缩图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
        // 压缩好比例大小后再进行质量压缩
        // 这里再进行质量压缩的意义不大，反而耗资源，删除
        //return compress(bitmap, maxSize);
        return bitmap;
    }

    /**
     * Compress image by size, this will modify image width/height.
     * Used to get thumbnail
     *
     * @param image
     * @param pixelW target pixel of width
     * @param pixelH target pixel of height
     * @return
     */
    public static Bitmap ratio(Bitmap image, float pixelW, float pixelH) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, os);
        //判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
        if (os.toByteArray().length / 1024 > 1024) {
            //重置baos即清空baos
            os.reset();
            //这里压缩50%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, 50, os);
        }
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 设置高度为240f时，可以明显看到图片缩小了
        float hh = pixelH;
        // 设置宽度为120f，可以明显看到图片缩小了
        float ww = pixelW;
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        //be=1表示不缩放
        int be = 1;
        if (w > h && w > ww) {
            //如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            //如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        //设置缩放比例
        newOpts.inSampleSize = be;
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        is = new ByteArrayInputStream(os.toByteArray());
        bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        //压缩好比例大小后再进行质量压缩
        // 这里再进行质量压缩的意义不大，反而耗资源，删除
        //return getCompressBitmap(bitmap);
        return bitmap;
    }

    /****************************************************存储图片*****************************************************/

    /**
     * Compress by quality,  and generate image to the path specified
     *
     * @param image
     * @param outPath
     * @param maxSize target will be compressed to be smaller than this size.(kb)
     * @throws IOException
     */
    public static void compressAndGenImage(Bitmap image, String outPath, int maxSize) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // scale
        int options = 100;
        // Store the bitmap into output stream(no compress)
        image.compress(Bitmap.CompressFormat.JPEG, options, os);
        // Compress by loop
        while (os.toByteArray().length / 1024 > maxSize) {
            // Clean up os
            os.reset();
            // interval 10
            options -= 10;
            image.compress(Bitmap.CompressFormat.JPEG, options, os);
        }

        // Generate compressed image file
        FileOutputStream fos = new FileOutputStream(outPath);
        fos.write(os.toByteArray());
        fos.flush();
        fos.close();
    }

    /**
     * Compress by quality,  and generate image to the path specified
     *
     * @param imgPath
     * @param outPath
     * @param maxSize     target will be compressed to be smaller than this size.(kb)
     * @param needsDelete Whether delete original file after compress
     * @throws IOException
     */
    public static void compressAndGenImage(String imgPath, String outPath, int maxSize, boolean needsDelete) throws IOException {
        compressAndGenImage(getBitmap(imgPath), outPath, maxSize);

        // Delete original file
        if (needsDelete) {
            File file = new File(imgPath);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * Ratio and generate thumb to the path specified
     *
     * @param image
     * @param outPath
     * @param pixelW  target pixel of width
     * @param pixelH  target pixel of height
     * @throws FileNotFoundException
     */
    public static void ratioAndGenThumb(Bitmap image, String outPath, float pixelW, float pixelH) throws FileNotFoundException {
        Bitmap bitmap = ratio(image, pixelW, pixelH);
        storeImage(bitmap, outPath);
    }

    /**
     * Ratio and generate thumb to the path specified
     *
     * @param outPath
     * @param pixelW      target pixel of width
     * @param pixelH      target pixel of height
     * @param needsDelete Whether delete original file after compress
     * @throws FileNotFoundException
     */
    public static void ratioAndGenThumb(String imgPath, String outPath, float pixelW, float pixelH, boolean needsDelete) throws FileNotFoundException {
        Bitmap bitmap = ratio(imgPath, pixelW, pixelH);
        storeImage(bitmap, outPath);

        // Delete original file
        if (needsDelete) {
            File file = new File(imgPath);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 质量压缩
     * 设置bitmap options属性，降低图片的质量，像素不会减少
     * 第一个参数为需要压缩的bitmap图片对象，第二个参数为压缩后图片保存的位置
     * 设置options 属性0-100，来实现压缩
     * <p>
     * <p>
     * 原理：保持像素的前提下改变图片的位深及透明度，（即：通过算法抠掉(同化)了图片中的一些某个些点附近相近的像素），达到降低质量压缩文件大小的目的。
     * 注意：它其实只能实现对file的影响，对加载这个图片出来的bitmap内存是无法节省的，还是那么大。
     * 因为bitmap在内存中的大小是按照像素计算的，也就是width*height，对于质量压缩，并不会改变图片的真实的像素（像素大小不会变）。
     * 使用场景：将图片压缩后将图片上传到服务器，或者保存到本地。根据实际需求来。
     *
     * @param bmp
     * @param file
     */
    public static void qualityCompress(Bitmap bmp, File file) {
        // 0-100 100为不压缩
        int quality = 20;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 把压缩后的数据存放到baos中
        bmp.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 尺寸压缩（通过缩放图片像素来减少图片占用内存大小）
     * <p>
     * 原理：通过减少单位尺寸的像素值，正真意义上的降低像素。1020*8880
     * 使用场景：缓存缩略图的时候（头像处理）
     *
     * @param bmp
     * @param file
     */
    public static void sizeCompress(Bitmap bmp, File file) {
        // 尺寸压缩倍数,值越大，图片尺寸越小
        int ratio = 8;
        // 压缩Bitmap到对应尺寸
        Bitmap result = Bitmap.createBitmap(bmp.getWidth() / ratio, bmp.getHeight() / ratio, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, bmp.getWidth() / ratio, bmp.getHeight() / ratio);
        canvas.drawBitmap(bmp, null, rect, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 把压缩后的数据存放到baos中
        result.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 采样率压缩（设置图片的采样率，降低图片像素）
     * <p>
     * 原理：设置图片的采样率，降低图片像素
     * 好处：是不会先将大图片读入内存，大大减少了内存的使用，也不必考虑将大图片读入内存后的释放事宜。
     * 问题：因为采样率是整数，所以不能很好的保证图片的质量。如我们需要的是在2和3采样率之间，用2的话图片就大了一点，但是用3的话图片质量就会有很明显的下降，这样也无法完全满足我的需要。
     *
     * @param imgPath
     * @param file
     */
    public static void samplingRateCompress(String imgPath, File file) {
        if (imgPath.contains("file://")) {
            imgPath = StringUtils.replaceStr(imgPath, "file://");
        }
        // 数值越高，图片像素越低
        int inSampleSize = 8;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        //为true的时候不会真正加载图片，而是得到图片的宽高信息。
        //options.inJustDecodeBounds = true;
        //采样率
        options.inSampleSize = inSampleSize;
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 把压缩后的数据存放到baos中
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        try {
            if (file.exists()) {
                file.delete();
            } else {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Store bitmap into specified image path
     *
     * @param bitmap
     * @param outPath
     * @throws FileNotFoundException
     */
    public static void storeImage(Bitmap bitmap, String outPath) throws FileNotFoundException {
        FileOutputStream os = new FileOutputStream(outPath);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
    }

    /***********************************************图片合成、拼接**********************************************************/

    /**
     * 合成两张图片：两图狂高要一样不然第二张图会被拉升
     *
     * @param firstBitmap
     * @param secondBitmap
     * @return
     */
    public static Bitmap mergeBitmap(Bitmap firstBitmap, Bitmap secondBitmap) {
        //以其中一张图片的大小作为画布的大小，或者也可以自己自定义
        Bitmap bitmap = Bitmap.createBitmap(firstBitmap.getWidth(), firstBitmap.getHeight(), firstBitmap.getConfig());
        //生成画布
        Canvas canvas = new Canvas(bitmap);
        //因为我传入的secondBitmap的大小是不固定的，所以我要将传来的secondBitmap调整到和画布一样的大小
        float w = firstBitmap.getWidth();
        float h = firstBitmap.getHeight();
        Matrix m = new Matrix();
        //确定secondBitmap大小比例
        m.setScale(w / secondBitmap.getWidth(), h / secondBitmap.getHeight());
        Paint paint = new Paint();
        //给画笔设定透明值，想将哪个图片进行透明化，就将画笔用到那张图片上
        paint.setAlpha(150);
        canvas.drawBitmap(firstBitmap, 0, 0, null);
        canvas.drawBitmap(secondBitmap, m, paint);
        return bitmap;
    }

    /**
     * 拼接图片：以第一个图为准
     * 优化算法：
     * 1.图片不需要铺满，只需要以统一合适的宽度。然后让imageview自己去铺满，不然长图合成长图会崩溃，这里以第一张图为例
     * 2.只缩放不相等宽度的图片。已经缩放过的不需要再次缩放
     *
     * @param bit1
     * @param bit2
     * @return
     */
    public static Bitmap stitchBitmap(Bitmap bit1, Bitmap bit2) {
        Bitmap newBit = null;
        int width = bit1.getWidth();
        if (bit2.getWidth() != width) {
            int h2 = bit2.getHeight() * width / bit2.getWidth();
            newBit = Bitmap.createBitmap(width, bit1.getHeight() + h2, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newBit);
            Bitmap newSizeBitmap2 = scaleBitmap(bit2, width, h2);
            canvas.drawBitmap(bit1, 0, 0, null);
            canvas.drawBitmap(newSizeBitmap2, 0, bit1.getHeight(), null);
        } else {
            newBit = Bitmap.createBitmap(width, bit1.getHeight() + bit2.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newBit);
            canvas.drawBitmap(bit1, 0, 0, null);
            canvas.drawBitmap(bit2, 0, bit1.getHeight(), null);
        }
        return newBit;
    }

    public static Bitmap stitchBitmap(Bitmap bit1, Bitmap bit2, Bitmap bit3) {
        return stitchBitmap(stitchBitmap(bit1, bit2), bit3);
    }

    /*********************************************************************************************************/

    /**
     * @param uri：图片的本地url地址
     * @return Bitmap；
     */
    public static Bitmap decodeUriAsBitmap(Context context, Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    /**
     * 缩放图片
     *
     * @param bitmap
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        float scaleWidth = ((float) newWidth) / bitmap.getWidth();
        float scaleHeight = ((float) newHeight) / bitmap.getHeight();
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap bit1Scale = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bit1Scale;
    }

    /**
     * 图片旋转
     *
     * @param tmpBitmap
     * @param degrees
     * @return
     */
    public static Bitmap rotateToDegrees(Bitmap tmpBitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degrees);
        return tmpBitmap = Bitmap.createBitmap(tmpBitmap, 0, 0, tmpBitmap.getWidth(), tmpBitmap.getHeight(), matrix, true);
    }

    /**
     * 裁剪
     *
     * @param bitmap 原图
     * @return 裁剪后的图像:
     */
    public static Bitmap cropBitmap(Bitmap bitmap, int x, int y, int width, int height) {
        try {
            return Bitmap.createBitmap(bitmap, x, y, width, height);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "图片裁剪失败返回原图：" + e.getMessage() + "\nx:" + x + "y:" + y + "width:" + width + "height:" + height);
            return bitmap;
        } finally {
        }
    }
}
