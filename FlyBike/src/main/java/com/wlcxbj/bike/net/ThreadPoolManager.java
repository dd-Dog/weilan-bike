package com.wlcxbj.bike.net;

/**
 * Created by Administrator on 2016/11/10.
 */


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 使用静态内部类实现线程池对象的单例模式
 * 用来管理线程的执行和任务提交
 */
public class ThreadPoolManager {

    private final ThreadPoolExecutor executor;

    private ThreadPoolManager() {

        //处理器核心数*2 + 1,最大效率
        int corePoolSize = Runtime.getRuntime().availableProcessors() * 2 + 1;
        int maximumPoolSize = corePoolSize;

        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>();
        TimeUnit unit = TimeUnit.HOURS;
        long keepAliveTime = 2;
        executor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                threadFactory,
                handler
        );
    }

    /**
     * 添加任务
     * @param runnable
     */
    public void execute(Runnable runnable) {
        if (runnable != null) {
            executor.execute(runnable);
        }
    }

    /**
     * 移除任务
     * @param runnable
     */
    public void remove(Runnable runnable) {
        if (runnable != null) {
            executor.remove(runnable);
        }
    }


    public static ThreadPoolManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final ThreadPoolManager INSTANCE = new ThreadPoolManager();
    }

}
