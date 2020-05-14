package com.wsg.common

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import com.wsg.common.utils.TimeUtils
import java.util.*

/**
 *  @author WuSG
 *  @date : 2020-01-07 10:14
 *  用来配置日志
 */
abstract class AbstractLogConfig {
    /**
     * 日志默认文件夹
     */
    abstract fun getDefaultLogDirectory(): String?

    abstract fun onSuffix(): String?

    companion object {
        const val DEFAULT_SUFFIX = ".log"
        lateinit var APP: Application
        /**
         * 权限检测
         */
        fun checkPermission(): Boolean {
            val pm: PackageManager = APP.packageManager
            val permissionReadStorage = (PackageManager.PERMISSION_GRANTED ==
                    pm.checkPermission(
                        "android.permission.READ_EXTERNAL_STORAGE",
                        APP.packageName
                    ))
            val permissionWriteStorage = (PackageManager.PERMISSION_GRANTED ==
                    pm.checkPermission(
                        "android.permission.WRITE_EXTERNAL_STORAGE",
                        APP.packageName
                    ))
            return permissionReadStorage && permissionWriteStorage
        }
    }

    fun getLogFilePath(logTag: String): String? {
        return getDefaultLogDirectory()!!
            .plus("/$logTag")
            .plus("/")
            .plus(logTag)
            .plus("-")
            .plus(TimeUtils.getSimpleDateString())
            .plus(onSuffix() ?: DEFAULT_SUFFIX)
            .replace("//", "/")
    }



}