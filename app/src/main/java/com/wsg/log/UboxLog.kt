package com.wsg.log

import com.wsg.annotation.ULog
import com.wsg.common.AbstractLogConfig
import com.wsg.common.AbstractLogger
import com.wsg.common.DefaultLogConfig
import com.wsg.common.utils.FileZipUtils
import java.io.File

/**
 *  @author WuSG
 *  @date : 2020-01-08 16:16
 */
@ULog(tagName = ["devLog", "vmcLog"])
class UboxLog : AbstractLogger() {
    override fun getAbstractLogConfig(): AbstractLogConfig {
        return DefaultLogConfig()
    }

    override fun onRetentionTime(): Long {
        return 1000 * 60
    }

    override fun isRetention(): Boolean {
        return true
    }

    override fun onDetectedFolderPath(): String? {
        return FileZipUtils.generateDefaultLogDirectory("ULog")
    }


}