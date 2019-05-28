package com.lee.mvvm.utils

import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.ThreadPoolExecutor

/**
 * @author li
 * @date 2018/9/12
 */
object ThreadHelper {
    private val cpuCount = Runtime.getRuntime().availableProcessors()
    private val poolSize = Math.max(2, Math.min(cpuCount - 1, 4))
    val threadPool: ThreadPoolExecutor = ScheduledThreadPoolExecutor(poolSize)
}
