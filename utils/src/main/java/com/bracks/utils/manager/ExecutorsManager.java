package com.bracks.utils.manager;

import android.support.annotation.NonNull;

import com.bracks.utils.util.DeviceUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * good programmer.
 *
 * @date : 2018-12-25 下午 04:52
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class ExecutorsManager {
    private static final int CPU_COUNT = DeviceUtils.getCountOfCPU();
    //private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    public static ExecutorService eventExecutor;
    private static ExecutorsManager sInstance;

    static {
        eventExecutor = new ThreadPoolExecutor(
                CPU_COUNT + 1,
                CPU_COUNT * 2 + 1,
                1,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(128),
                new ThreadFactory() {
                    private final AtomicInteger mCount = new AtomicInteger(1);

                    @Override
                    public Thread newThread(@NonNull Runnable r) {
                        return new Thread(r, "eventAsyncAndBackground #" + mCount.getAndIncrement());
                    }
                },
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    /*******************************************************************************************************************/

    ExecutorService mExecutorService = new ThreadPoolExecutor(
            1,
            1,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(),
            new ThreadPoolExecutor.AbortPolicy());

    private ExecutorsManager() {
    }

    public static ExecutorsManager getInstance() {
        if (sInstance == null) {
            synchronized (ExecutorsManager.class) {
                if (sInstance == null) {
                    sInstance = new ExecutorsManager();
                }
            }
        }
        return sInstance;
    }

    public void execute(Runnable runnable) {
        mExecutorService.execute(runnable);
    }
}
