package com.wsg.common

import android.util.Log
import com.wsg.common.utils.TimeUtils
import com.wsg.common.utils.smartCreateNewFile

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.nio.charset.Charset



object Logger {
    enum class LogLevel {
        ERROR {
            override val value: Int
                get() = 0
        },
        WARN {
            override val value: Int
                get() = 1
        },
        INFO {
            override val value: Int
                get() = 2
        },
        DEBUG {
            override val value: Int
                get() = 3
        };

        abstract val value: Int
    }

    private var TAG = "Ubox_Log"
    private var logLevel = LogLevel.DEBUG
    private var mTimeStringBuilder = java.lang.StringBuilder()


    @JvmStatic
    fun otherTagLog(tag: String = TAG, msg: String, filePath: String) {
        tagLog(msg, tag, filePath)
    }

    private fun tagLog(msg: String, tag: String, filePath: String) {
        if (LogLevel.DEBUG.value <= logLevel.value) {
            if (msg.isNotBlank()) {
                val s = getMethodNames(checkMsgLength(msg))
                val string = String.format(s, msg)
                Log.d(tag, string)
                writeText(string, filePath)
            }
        }
    }

    private fun checkMsgLength(msg: String): Boolean {
        if (msg.length > 70) {
            return true
        }
        return false
    }


    @JvmStatic
    fun e(tag: String = TAG, msg: String) {
        if (LogLevel.ERROR.value <= logLevel.value) {
            if (msg.isNotBlank()) {
                val s = getMethodNames(checkMsgLength(msg))
                Log.e(tag, String.format(s, msg))
            }
        }
    }

    @Synchronized
    fun writeText(text: String, file: String) {
        val mFile = File(file)
        if (mFile.smartCreateNewFile()) {
            val timeStringBuilder =
                mTimeStringBuilder.append(TimeUtils.getNow()).append(" - ").append(text)
            mFile.appendText(timeStringBuilder.toString(), Charset.defaultCharset())
            mTimeStringBuilder.clear()
        }else{
            e(msg = "Create New File Fail!")
        }
    }

    @JvmStatic
    fun w(tag: String = TAG, msg: String) {
        if (LogLevel.WARN.value <= logLevel.value) {
            if (msg.isNotBlank()) {
                val s = getMethodNames(checkMsgLength(msg))
                Log.w(tag, String.format(s, msg))
            }
        }
    }

    @JvmStatic
    fun i(tag: String = TAG, msg: String) {
        if (LogLevel.INFO.value <= logLevel.value) {
            if (msg.isNotBlank()) {
                val s = getMethodNames(checkMsgLength(msg))
                Log.i(tag, String.format(s, msg))
            }
        }
    }

    @JvmStatic
    fun d(tag: String = TAG, msg: String) {
        if (LogLevel.DEBUG.value <= logLevel.value) {
            if (msg.isNotBlank()) {
                val s = getMethodNames(checkMsgLength(msg))
                Log.d(tag, String.format(s, msg))
            }
        }
    }

    @JvmStatic
    fun json(json: String, filePath: String) {
        var json = json
        if (json.isBlank()) {
            d(msg = "Empty/Null json content")
            return
        }
        try {
            json = json.trim { it <= ' ' }
            if (json.startsWith("{")) {
                val jsonObject = JSONObject(json)
                var message = jsonObject.toString(LoggerPrinter.JSON_INDENT)
                message = message.replace("\n".toRegex(), "\n")
                val s = getJsonMethodNames()
                val string = String.format(s, message)
                println(string)
                writeText(string, filePath)
                return
            }
            if (json.startsWith("[")) {
                val jsonArray = JSONArray(json)
                var message = jsonArray.toString(LoggerPrinter.JSON_INDENT)
                message = message.replace("\n".toRegex(), "\n║ ")
                val s = getJsonMethodNames()
                val string = String.format(s, message)
                println(string)
                writeText(string, filePath)
                return
            }
            e(msg = "Invalid Json")
        } catch (e: JSONException) {
            e(msg = "Invalid Json")
        }

    }


    private fun getJsonMethodNames(): String {
        val sElements = Thread.currentThread().stackTrace
        var stackOffset = LoggerPrinter.getStackOffset(sElements)
        stackOffset += 2
        val builder = StringBuilder()
        builder.append("[" + Thread.currentThread().name).append("]")
            // 添加类名、方法名、行数
            .append(" - [")
            .append(sElements[stackOffset].className)
            .append(".")
            .append(sElements[stackOffset].methodName)
            .append("(")
            .append(sElements[stackOffset].fileName)
            .append(":")
            .append(sElements[stackOffset].lineNumber)
            .append(")")
            .append("]")
            .append("\r\n")
            // 添加打印的日志信息
            .append("%s").append("\r\n")
        return builder.toString()
    }


    private fun getMethodNames(checkMsgLength: Boolean): String {
        val sElements = Thread.currentThread().stackTrace
        var stackOffset = LoggerPrinter.getStackOffset(sElements)
        stackOffset += 2
        val builder = StringBuilder()
        //时间
        builder
            .append("[" + Thread.currentThread().name).append("]")
            // 添加类名、方法名、行数
            .append(" - [")
            .append(sElements[stackOffset].className)
            .append(".")
            .append(sElements[stackOffset].methodName)
            .append("(")
            .append(sElements[stackOffset].fileName)
            .append(":")
            .append(sElements[stackOffset].lineNumber)
            .append(")")
            .append("]")
            .append(
                if (checkMsgLength) {
                    "\r\n"
                } else {
                    " - "
                }
            )
            .append("%s").append("\r\n")
        return builder.toString()
    }


}

