package com.bracks.utils.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.bracks.utils.R;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * good programmer.
 *
 * @date : 2018-09-03 16:59
 * @author: futia
 * @email : futianyi1994@126.com
 * @description : Glide工具类（glide 4.x）
 * 功能包括加载图片，圆形图片，圆角图片，指定圆角图片，模糊图片，灰度图片等等。
 * 目前我只加了这几个常用功能，其他请参考glide-transformations这个开源库。
 * https://github.com/wasabeef/glide-transformations
 */
@GlideModule
public class GlideUtils extends AppGlideModule {
    public static final String TAG = "GlideUtils";

    public static final int PLACEHOLDER = R.color.white;
    public static final int ERROR = R.color.white;


    /**
     * 加载图片(默认)
     *
     * @param context
     * @param object
     * @param imageView
     */
    public static void loadImage(Context context, Object object, ImageView imageView) {
        loadImage(context, object, 0, 0, imageView);
    }

    /**
     * 加载图片(需要占位图和错误图)
     *
     * @param context
     * @param object
     * @param empty
     * @param imageView
     */
    public static void loadImage(Context context, Object object, @DrawableRes int empty, ImageView imageView) {
        loadImage(context, object, empty, empty, imageView);
    }

    /**
     * 加载图片(需要占位图和错误图)
     *
     * @param context
     * @param object
     * @param placeholder
     * @param error
     * @param imageView
     */
    public static void loadImage(Context context, Object object, @DrawableRes int placeholder, @DrawableRes int error, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .fitCenter()
                //占位图
                .placeholder(placeholder == 0 ? PLACEHOLDER : placeholder)
                //错误图
                .error(error == 0 ? ERROR : error)
                //.priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        helper(context, object, imageView, options).into(imageView);
    }

    /**
     * 指定图片大小;使用override()方法指定了一个图片的尺寸。
     * Glide现在只会将图片加载成width*height像素的尺寸，而不会管你的ImageView的大小是多少了。
     * 如果你想加载一张图片的原始尺寸的话，可以使用Target.SIZE_ORIGINAL关键字----override(Target.SIZE_ORIGINAL)
     *
     * @param context
     * @param object
     * @param imageView
     * @param width
     * @param height
     */
    public static void loadImageSize(Context context, Object object, ImageView imageView, int width, int height) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                //占位图
                .placeholder(PLACEHOLDER)
                //错误图
                .error(ERROR)
                .override(width, height)
                //.priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        helper(context, object, imageView, options).into(imageView);
    }

    /**
     * 禁用内存缓存功能
     * diskCacheStrategy()方法基本上就是Glide硬盘缓存功能的一切，它可以接收五种参数：
     * <p>
     * DiskCacheStrategy.NONE： 表示不缓存任何内容。
     * DiskCacheStrategy.DATA： 表示只缓存原始图片。
     * DiskCacheStrategy.RESOURCE： 表示只缓存转换过后的图片。
     * DiskCacheStrategy.ALL ： 表示既缓存原始图片，也缓存转换过后的图片。
     * DiskCacheStrategy.AUTOMATIC： 表示让Glide根据图片资源智能地选择使用哪一种缓存策略（默认选项）。
     */
    public static void loadImageSizekipMemoryCache(Context context, Object object, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                //占位图
                .placeholder(PLACEHOLDER)
                //错误图
                .error(ERROR)
                //禁用掉Glide的内存缓存功能
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        helper(context, object, imageView, options).into(imageView);
    }

    /**
     * 禁用磁盘缓存功能
     * diskCacheStrategy()方法基本上就是Glide硬盘缓存功能的一切，它可以接收五种参数：
     * <p>
     * DiskCacheStrategy.NONE： 表示不缓存任何内容。
     * DiskCacheStrategy.DATA： 表示只缓存原始图片。
     * DiskCacheStrategy.RESOURCE： 表示只缓存转换过后的图片。
     * DiskCacheStrategy.ALL ： 表示既缓存原始图片，也缓存转换过后的图片。
     * DiskCacheStrategy.AUTOMATIC： 表示让Glide根据图片资源智能地选择使用哪一种缓存策略（默认选项）。
     */
    public static void loadImageSizekipDiskCache(Context context, Object object, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                //占位图
                .placeholder(PLACEHOLDER)
                //错误图
                .error(ERROR)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        helper(context, object, imageView, options).into(imageView);
    }

    /**
     * 禁用磁盘缓存功能
     * diskCacheStrategy()方法基本上就是Glide硬盘缓存功能的一切，它可以接收五种参数：
     * <p>
     * DiskCacheStrategy.NONE： 表示不缓存任何内容。
     * DiskCacheStrategy.DATA： 表示只缓存原始图片。
     * DiskCacheStrategy.RESOURCE： 表示只缓存转换过后的图片。
     * DiskCacheStrategy.ALL ： 表示既缓存原始图片，也缓存转换过后的图片。
     * DiskCacheStrategy.AUTOMATIC： 表示让Glide根据图片资源智能地选择使用哪一种缓存策略（默认选项）。
     */
    public static void loadImageSizekipCache(Context context, Object object, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                //占位图
                .placeholder(PLACEHOLDER)
                //错误图
                .error(ERROR)
                //禁用掉Glide的内存缓存功能
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        helper(context, object, imageView, options).into(imageView);
    }

    /**
     * 加载圆形图片
     *
     * @param context
     * @param object
     * @param imageView
     */
    public static void loadCircleImage(Context context, Object object, ImageView imageView) {
        loadCircleImage(context, object, 0, 0, imageView);
    }

    /**
     * 加载圆形图片(需要占位图和错误图)
     *
     * @param context
     * @param object
     * @param empty
     * @param imageView
     */
    public static void loadCircleImage(Context context, Object object, @DrawableRes int empty, ImageView imageView) {
        loadCircleImage(context, object, empty, empty, imageView);
    }

    /**
     * 加载圆形图片(需要占位图和错误图)
     *
     * @param context
     * @param object
     * @param placeholder
     * @param error
     * @param imageView
     */
    public static void loadCircleImage(Context context, Object object, @DrawableRes int placeholder, @DrawableRes int error, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                //设置圆形
                .circleCrop()
                .placeholder(placeholder == 0 ? PLACEHOLDER : placeholder)
                .error(error == 0 ? ERROR : error)
                //.priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        helper(context, object, imageView, options).into(imageView);
    }

    /**
     * 预先加载图片
     * 在使用图片之前，预先把图片加载到缓存，调用了预加载之后，我们以后想再去加载这张图片就会非常快了，
     * 因为Glide会直接从缓存当中去读取图片并显示出来
     */
    public static void preloadImage(Context context, String url) {
        GlideApp
                .with(context)
                .load(url)
                .preload();
    }

    /**
     * 加载圆角图片
     *
     * @param context
     * @param object
     * @param imageView
     */
    public static void loadRoundCircleImage(Context context, Object object, ImageView imageView) {
        loadRoundCircleImage(context, object, 0, 0, imageView);
    }

    /**
     * 加载圆角图片(需要占位图和错误图)
     *
     * @param context
     * @param object
     * @param empty
     * @param imageView
     */
    public static void loadRoundCircleImage(Context context, Object object, @DrawableRes int empty, ImageView imageView) {
        loadRoundCircleImage(context, object, empty, empty, imageView);
    }

    /**
     * 加载圆角图片(需要占位图和错误图)
     *
     * @param context
     * @param object
     * @param placeholder
     * @param error
     * @param imageView
     */
    public static void loadRoundCircleImage(Context context, Object object, @DrawableRes int placeholder, @DrawableRes int error, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                //设置圆形
                .circleCrop()
                //占位图
                .placeholder(placeholder == 0 ? PLACEHOLDER : placeholder)
                //错误图
                .error(error == 0 ? ERROR : error)
                //.priority(Priority.HIGH)
                .transform(new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL))
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        helper(context, object, imageView, options).into(imageView);
    }

    /**
     * 加载圆角图片-指定任意部分圆角（图片上、下、左、右四个角度任意定义）
     *
     * @param context
     * @param object
     * @param imageView
     * @param type
     */
    public static void loadCustRoundCircleImage(Context context, Object object, ImageView imageView, RoundedCornersTransformation.CornerType type) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(PLACEHOLDER)
                .error(ERROR)
                //.priority(Priority.HIGH)
                .transform(new RoundedCornersTransformation(45, 0, type))
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        helper(context, object, imageView, options).into(imageView);
    }

    /**
     * 加载模糊图片（自定义透明度）
     *
     * @param context
     * @param object
     * @param imageView
     * @param blur      模糊度，一般1-100够了，越大越模糊
     */
    public static void loadBlurImage(Context context, Object object, ImageView imageView, int blur) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(PLACEHOLDER)
                .error(ERROR)
                //.priority(Priority.HIGH)
                .transform(new BlurTransformation(blur))
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        helper(context, object, imageView, options).into(imageView);
    }

    /**
     * 加载灰度(黑白)图片（自定义透明度）
     *
     * @param context
     * @param object
     * @param imageView
     */
    public static void loadBlackImage(Context context, Object object, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(PLACEHOLDER)
                .error(ERROR)
                //.priority(Priority.HIGH)
                .transform(new GrayscaleTransformation())
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        helper(context, object, imageView, options).into(imageView);
    }

    /**
     * Glide.with(this).asGif()    //强制指定加载动态图片
     * 如果加载的图片不是gif，则asGif()会报错， 当然，asGif()不写也是可以正常加载的。
     * 加入了一个asBitmap()方法，这个方法的意思就是说这里只允许加载静态图片，不需要Glide去帮我们自动进行图片格式的判断了。
     * 如果你传入的还是一张GIF图的话，Glide会展示这张GIF图的第一帧，而不会去播放它。
     *
     * @param context
     * @param object    例如：https://image.niwoxuexi.com/blog/content/5c0d4b1972-loading.gif
     * @param imageView
     */
    private static void loadGif(Context context, Object object, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .placeholder(PLACEHOLDER)
                .error(ERROR);
        helper(context, object, imageView, options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageView);
    }

    /**
     * 异步下载图片保存为File
     *
     * @param context
     * @param url
     * @param callback
     */
    public static void downloadImageAsyn(final Context context, final String url, final Callback callback) {
        ThreadPoolExecutor singleThreadPool = new ThreadPoolExecutor(
                1,
                1,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadPoolExecutor.AbortPolicy());
        singleThreadPool.execute(() -> {
            try {
                FutureTarget<File> target = GlideApp
                        .with(context)
                        .asFile()
                        .load(url)
                        .submit();
                File file = target.get();
                callback.success(file);
            } catch (Exception e) {
                callback.fail(e);
                e.printStackTrace();
            }
        });
        singleThreadPool.shutdown();
    }

    /**
     * 同步下载图片保存为Bitmap
     *
     * @param context
     * @param url
     */
    public static Bitmap downloadImageSync(Context context, final String url) {
        try {
            return GlideApp
                    .with(context)
                    .asBitmap()
                    .load(url)
                    .submit()
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void clear(Context context, View view) {
        GlideApp
                .with(context)
                .clear(view);
    }

    private static GlideRequest<Drawable> helper(Context context, Object object, ImageView imageView, RequestOptions options) {
        if (object instanceof String) {
            return GlideApp
                    .with(context)
                    .load(((String) object))
                    .apply(options)
                    ;
        } else if (object instanceof File) {
            return GlideApp
                    .with(context)
                    .load(((File) object))
                    .apply(options)
                    ;
        } else if (object instanceof Bitmap) {
            return GlideApp
                    .with(context)
                    .load(((Bitmap) object))
                    .apply(options)
                    ;
        } else if (object instanceof Drawable) {
            return GlideApp
                    .with(context)
                    .load(((Drawable) object))
                    .apply(options)
                    ;
        } else if (object instanceof Uri) {
            return GlideApp
                    .with(context)
                    .load(((Uri) object))
                    .apply(options)
                    ;
        } else if (object instanceof Integer) {
            return GlideApp
                    .with(context)
                    .load(((Integer) object))
                    .apply(options)
                    ;
        } else {
            return GlideApp
                    .with(context)
                    .load((object))
                    .apply(options)
                    ;
        }
    }

    public interface Callback {
        /**
         * 异步下载成功回调
         */
        void success(File file);

        /**
         * 异步下载失败 回调
         */
        void fail(Exception e);
    }
}
