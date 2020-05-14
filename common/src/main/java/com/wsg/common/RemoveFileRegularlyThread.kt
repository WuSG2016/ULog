package com.wsg.common

import android.os.SystemClock
import android.util.Log
import java.io.File

/**
 *  @author WuSG
 *  @date : 2020-05-14 14:08
 *  移除文件
 */
class RemoveFileRegularlyThread constructor(
    private val filePath: String,
    private val retentionTime: Long
) :
    Thread() {
    override fun run() {
        val file = File(filePath)
        if (file.exists()) {
            val fileList = file.listFiles()
            for (f in fileList!!) {
                if (f.exists()) {
                    val time = System.currentTimeMillis()
                    val fileCreateTime = f.lastModified()
                    Log.e(
                        "TAG",
                        "retentionTime--$retentionTime date:${time - fileCreateTime} "
                    )
                    if (time - fileCreateTime >= retentionTime) {
                        f.deleteRecursively()
                    }
                }
            }
        }

    }
}