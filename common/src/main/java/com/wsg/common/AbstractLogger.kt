package com.wsg.common

/**
 *  @author WuSG
 *  @date : 2020-01-07 11:20
 */
abstract class AbstractLogger {
    protected val day: Long = 1000 * 60 * 60 * 24
    abstract fun getAbstractLogConfig(): AbstractLogConfig

    /**
     * 日志保留时长
     */
    abstract fun onRetentionTime(): Long


    abstract fun isRetention(): Boolean

    /**
     * 检测的文件夹
     */
    abstract fun onDetectedFolderPath():String?

}