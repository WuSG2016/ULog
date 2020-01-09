package com.wsg.log

import com.wsg.annotation.ULog
import com.wsg.common.AbstractLogConfig
import com.wsg.common.AbstractLogger
import com.wsg.common.DefaultLogConfig

/**
 *  @author WuSG
 *  @date : 2020-01-08 16:16
 */
@ULog(tagName = ["devLog","vmcLog"])
class UboxLog : AbstractLogger() {
    override fun getAbstractLogConfig(): AbstractLogConfig {
        return DefaultLogConfig()
    }
}