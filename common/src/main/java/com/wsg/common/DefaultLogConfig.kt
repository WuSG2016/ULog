package com.wsg.common


import android.app.Application
import android.os.Environment
import com.wsg.common.utils.TimeUtils
import java.io.File


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
        return generateDefaultLogDirectory()
    }


    override fun onSuffix(): String? {
        return DEFAULT_SUFFIX
    }

    private fun generateDefaultLogDirectory(): String? {
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