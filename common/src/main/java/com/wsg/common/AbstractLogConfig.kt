package com.wsg.common

import android.content.Context
import android.content.pm.PackageManager
import com.wsg.common.utils.TimeUtils

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

    /**
     * 权限检测
     */
    fun checkPermission(mContexts: Context): Boolean {
        val pm: PackageManager = mContexts.packageManager
        val permissionReadStorage = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission(
                    "android.permission.READ_EXTERNAL_STORAGE",
                    mContexts.packageName
                ))
        val permissionWriteStorage = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission(
                    "android.permission.WRITE_EXTERNAL_STORAGE",
                    mContexts.packageName
                ))
        return permissionReadStorage && permissionWriteStorage
    }

}