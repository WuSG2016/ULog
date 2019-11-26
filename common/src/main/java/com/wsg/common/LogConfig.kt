package com.wsg.common

import android.os.Environment
import java.io.File
import java.util.HashMap

/**
 *  @author WuSG
 *  @date : 2019-11-26 14:36
 */
object LogConfig {

    val logHashMap = HashMap<String, File>()
    var logDirectory: String? = null
    /**
     * 默认日志文件夹
     */
    var DEFAULT_LOG_DIRECTORY = "Ubox/log/"
    const val DEV_TAG = "devLog"
    const val DEBUG_TAG = "deBugLog"
    const val VMC_TAG = "VmcLog"
    const val SUFFIX = ".log"
    fun addLogFile(logTag: String): LogConfig {
        val logFile = generateLogFile(logTag)
        if (logFile != null) {
            logHashMap[logTag] = logFile
        }
        return this
    }

    private fun generateLogFile(logTag: String): File? {
        this.logDirectory = generateDefaultLogDirectory()
        if (this.logDirectory != null) {
            return File(logDirectory.plus(logTag).plus(SUFFIX))
        }
        return null
    }

     fun generateDefaultLogDirectory(): String? {
        if (Environment.isExternalStorageEmulated()) {
            val sdcardDirectory = Environment.getExternalStorageDirectory()
            val file = File(sdcardDirectory, DEFAULT_LOG_DIRECTORY)
            if (!file.exists()) {
                file.mkdirs()
            }
            return file.absolutePath
        }
        return null
    }

}