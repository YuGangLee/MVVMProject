package com.lee.mvvm.utils;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author li
 * @date 2018/9/12
 */
public class ThreadHelper {
    private static ThreadPoolExecutor threadPool;

    public static ThreadPoolExecutor getThreadPool() {
        if (threadPool == null) {
            synchronized (ThreadHelper.class) {
                if (threadPool == null) {
                    int cpuCount = Runtime.getRuntime().availableProcessors();
                    int poolSize = Math.max(2, Math.min(cpuCount - 1, 4));
                    threadPool = new ScheduledThreadPoolExecutor(poolSize);
                }
            }
        }
        return threadPool;
    }
}
