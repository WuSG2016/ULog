package com.wsg.common


import android.app.Application
import android.os.Environment
import com.wsg.common.utils.FileZipUtils
import com.wsg.common.utils.TimeUtils
import java.io.File
import java.util.*


/**
 *  @author WuSG
 *  @date : 2020-01-07 11:28
 *  默认日志配置
 */
@Suppress("DEPRECATION")
class DefaultLogConfig : AbstractLogConfig() {
    companion object {
        private var DEFAULT_LOG_DIRECTORY = "ULog/${TimeUtils.getSimpleMonth()}"
    }

    override fun getDefaultLogDirectory(): String? {
        return FileZipUtils.generateDefaultLogDirectory(DEFAULT_LOG_DIRECTORY)
    }


    override fun onSuffix(): String? {
        return DEFAULT_SUFFIX
    }





}