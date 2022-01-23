package com.bracks.utils.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bracks.utils.R;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
    public static final int FALLBACK = R.color.white;
    public static final int DEFAULT_RADIUS = 20;


    /**
     * 加载图片(默认)
     *
     * @param context
     * @param object
     * @param imageView
     */
    public static void loadImage(Context context, Object object, ImageView imageView) {
        loadImage(context, object, 0, 0, 0, imageView);
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
        loadImage(context, object, empty, empty, empty, imageView);
    }

    /**
     * 加载图片(需要占位图和错误图)
     *
     * @param context
     * @param object
     * @param placeholder
     * @param error
     * @param fallback
     * @param imageView
     */
    public static void loadImage(Context context, Object object, @DrawableRes int placeholder, @DrawableRes int error, @DrawableRes int fallback, ImageView imageView) {
        RequestOptions options = getDefaultOptions()
                .placeholder(placeholder == 0 ? PLACEHOLDER : placeholder)
                .error(error == 0 ? ERROR : error)
                .fallback(fallback == 0 ? FALLBACK : fallback);
        help(context, object, options).into(imageView);
    }

    /**
     * 指定图片大小;使用override()方法指定了一个图片的尺寸。
     * Glide现在只会将图片加载成width*height像素的尺寸，而不会管你的ImageView的大小是多少了。
     * 如果你想加载一张图片的原始尺寸的话，可以使用Target.SIZE_ORIGINAL关键字----override(Target.SIZE_ORIGINAL)
     *
     * @param context
     * @param url
     * @param imageView
     * @param width
     * @param height
     */
    public static void loadImageSize(Context context, Object url, ImageView imageView, int width, int height) {
        RequestOptions options = getDefaultOptions().override(width, height);
        help(context, url, options).into(imageView);
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
    public static void loadImageSizekipMemoryCache(Context context, Object url, ImageView imageView) {
        RequestOptions options = getDefaultOptions().skipMemoryCache(true);
        help(context, url, options).into(imageView);
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
    public static void loadImageSizekipDiskCache(Context context, Object url, ImageView imageView) {
        RequestOptions options = getDefaultOptions().diskCacheStrategy(DiskCacheStrategy.NONE);
        help(context, url, options).into(imageView);
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
    public static void loadImageSizekipCache(Context context, Object url, ImageView imageView) {
        RequestOptions options = getDefaultOptions()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        help(context, url, options).into(imageView);
    }

    /**
     * 加载圆形图片
     *
     * @param context
     * @param object
     * @param imageView
     */
    public static void loadCircleImage(Context context, Object object, ImageView imageView) {
        loadCircleImage(context, object, PLACEHOLDER, ERROR, FALLBACK, imageView);
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
        loadCircleImage(context, object, empty, empty, empty, imageView);
    }

    /**
     * 加载圆形图片(需要占位图和错误图)
     *
     * @param context
     * @param object
     * @param placeholder
     * @param error
     * @param fallback
     * @param imageView
     */
    public static void loadCircleImage(Context context, Object object, @DrawableRes int placeholder, @DrawableRes int error, @DrawableRes int fallback, ImageView imageView) {
        RequestOptions options = getDefaultOptions().circleCrop();
        if (placeholder != 0) {
            options.placeholder(placeholder);
        }
        if (error != 0) {
            options.error(error);
        }
        if (fallback != 0) {
            options.fallback(fallback);
        }
        help(context, object, options).into(imageView);
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
        loadRoundCircleImage(context, object, 0, 0, 0, imageView);
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
        loadRoundCircleImage(context, object, empty, empty, empty, imageView);
    }

    /**
     * 加载圆角图片(需要占位图和错误图)
     *
     * @param context
     * @param object
     * @param placeholder
     * @param error
     * @param fallback
     * @param imageView
     */
    public static void loadRoundCircleImage(Context context, Object object, @DrawableRes int placeholder, @DrawableRes int error, @DrawableRes int fallback, ImageView imageView) {
        RequestOptions options = getDefaultOptions()
                .placeholder(placeholder == 0 ? PLACEHOLDER : placeholder)
                .error(error == 0 ? ERROR : error)
                .fallback(fallback == 0 ? FALLBACK : fallback)
                .transform(new RoundedCornersTransformation(getDefaultRadius(context), 0, RoundedCornersTransformation.CornerType.ALL));
        help(context, object, options).into(imageView);
    }

    /**
     * 加载圆角图片-指定任意圆角弧度
     *
     * @param context
     * @param url
     * @param imageView
     * @param radius
     */
    public static void loadRoundCircleImage(Context context, Object url, ImageView imageView, int radius) {
        RequestOptions options = getDefaultOptions().transform(new RoundedCornersTransformation(radius, 0, RoundedCornersTransformation.CornerType.ALL));
        help(context, url, options).into(imageView);
    }

    /**
     * 加载圆角图片-指定任意部分圆角（图片上、下、左、右四个角度任意定义）
     *
     * @param context
     * @param url
     * @param imageView
     * @param type
     */
    public static void loadRoundCircleImage(Context context, Object url, ImageView imageView, RoundedCornersTransformation.CornerType type) {
        RequestOptions options = getDefaultOptions().transform(new RoundedCornersTransformation(getDefaultRadius(context), 0, type));
        help(context, url, options).into(imageView);
    }

    /**
     * 加载圆角图片-指定任意部分圆角（图片上、下、左、右四个角度任意定义）
     *
     * @param context
     * @param url
     * @param imageView
     * @param radius
     * @param type
     */
    public static void loadRoundCircleImage(Context context, Object url, ImageView imageView, int radius, RoundedCornersTransformation.CornerType type) {
        RequestOptions options = getDefaultOptions().transform(new RoundedCornersTransformation(radius, 0, type));
        help(context, url, options).into(imageView);
    }

    /**
     * 加载圆角图片-指定任意部分圆角（图片上、下、左、右四个角度任意定义）
     *
     * @param context
     * @param url
     * @param imageView
     * @param transformation
     */
    public static void loadRoundCircleImage(Context context, Object url, ImageView imageView, @NonNull Transformation<Bitmap> transformation) {
        RequestOptions options = getDefaultOptions().transform(transformation);
        help(context, url, options).into(imageView);
    }

    /**
     * 加载模糊图片（自定义透明度）
     *
     * @param context
     * @param url
     * @param imageView
     * @param blur      模糊度，一般1-100够了，越大越模糊
     */
    public static void loadBlurImage(Context context, Object url, ImageView imageView, int blur) {
        RequestOptions options = getDefaultOptions().transform(new BlurTransformation(blur));
        help(context, url, options).into(imageView);
    }

    /**
     * 加载模糊图片（自定义透明度和空布局）
     *
     * @param context
     * @param url
     * @param empty
     * @param imageView
     * @param blur      模糊度，一般1-100够了，越大越模糊
     */
    public static void loadBlurImage(Context context, Object url, @DrawableRes int empty, ImageView imageView, int blur) {
        RequestOptions options = getDefaultOptions()
                .placeholder(empty)
                .error(empty)
                .transform(new BlurTransformation(blur));
        help(context, url, options).into(imageView);
    }

    /**
     * 加载模糊+圆角图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadBlurAndRoundCircleImage(Context context, Object url, ImageView imageView, int blur, int radius, RoundedCornersTransformation.CornerType... cornerType) {
        List<Transformation<Bitmap>> list = new ArrayList<>();
        list.add(new BlurTransformation(blur));
        for (RoundedCornersTransformation.CornerType type : cornerType) {
            list.add(new RoundedCornersTransformation(radius, 0, type));
        }
        MultiTransformation<Bitmap> multi = new MultiTransformation<>(list);
        RequestOptions options = getDefaultOptions().transform(new CenterCrop(), multi);
        help(context, url, options).into(imageView);
    }

    /**
     * 加载灰度(黑白)图片（自定义透明度）
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadBlackImage(Context context, Object url, ImageView imageView) {
        RequestOptions options = getDefaultOptions().transform(new GrayscaleTransformation());
        help(context, url, options).into(imageView);
    }

    /**
     * Glide.with(this).asGif()    //强制指定加载动态图片
     * 如果加载的图片不是gif，则asGif()会报错， 当然，asGif()不写也是可以正常加载的。
     * 加入了一个asBitmap()方法，这个方法的意思就是说这里只允许加载静态图片，不需要Glide去帮我们自动进行图片格式的判断了。
     * 如果你传入的还是一张GIF图的话，Glide会展示这张GIF图的第一帧，而不会去播放它。
     *
     * @param context
     * @param url       例如：https://image.niwoxuexi.com/blog/content/5c0d4b1972-loading.gif
     * @param imageView
     */
    private static void loadGif(Context context, String url, ImageView imageView) {
        RequestOptions options = getDefaultOptions();
        GlideApp
                .with(context)
                .load(url)
                .apply(options)
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
    public static void downloadImageAsyn(final Context context, final String url, final Callback<File> callback) {
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
     * 异步下载图片保存为Bitmap
     *
     * @param context
     * @param url
     * @param callback
     */
    public static void downloadBitmapAsyn(final Context context, final String url, final Callback<Bitmap> callback) {
        ThreadPoolExecutor singleThreadPool = new ThreadPoolExecutor(
                1,
                1,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadPoolExecutor.AbortPolicy());
        singleThreadPool.execute(() -> {
            try {
                FutureTarget<Bitmap> target = GlideApp
                        .with(context)
                        .asBitmap()
                        .load(url)
                        .submit();
                Bitmap bitmap = target.get();
                callback.success(bitmap);
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
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void clear(Context context, View view) {
        GlideApp
                .with(context)
                .clear(view);
    }

    public static RequestOptions getDefaultOptions() {
        return new RequestOptions()
                .fitCenter()
                //占位图(正在请求图片的时候展示的图片)
                .placeholder(PLACEHOLDER)
                //错误图(如果请求失败的时候展示的图片 （如果没有设置，还是展示placeholder的占位符）)
                .error(ERROR)
                //如果请求的url/model为 null 的时候展示的图片 （如果没有设置，还是展示placeholder的占位符）
                .fallback(FALLBACK)
                //.priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    public static int getDefaultRadius(Context context) {
        return context.getResources().getDimensionPixelSize(DEFAULT_RADIUS);
    }

    public static GlideRequest<Drawable> help(Context context, Object object, @Nullable RequestOptions options) {
        GlideRequest<Drawable> load;
        if (object instanceof String) {
            load = GlideApp.with(context).load(((String) object));
        } else if (object instanceof File) {
            load = GlideApp.with(context).load(((File) object));
        } else if (object instanceof Bitmap) {
            load = GlideApp.with(context).load(((Bitmap) object));
        } else if (object instanceof Drawable) {
            load = GlideApp.with(context).load(((Drawable) object));
        } else if (object instanceof Uri) {
            load = GlideApp.with(context).load(((Uri) object));
        } else if (object instanceof Integer) {
            load = GlideApp.with(context).load(((Integer) object));
        } else if (object instanceof byte[]) {
            load = GlideApp.with(context).load((byte[]) object);
        } else {
            load = GlideApp.with(context).load((object));
        }
        if (options != null) {
            return load.apply(options);
        } else {
            return load;
        }
    }

    public interface Callback<T> {
        /**
         * 异步下载成功回调
         */
        void success(T file);

        /**
         * 异步下载失败 回调
         */
        void fail(Exception e);
    }
}
