package com.wsg.common

import android.annotation.SuppressLint
import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*


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

    var logLevel = LogLevel.DEBUG
    private var mTimeStringBuilder = java.lang.StringBuilder()
    @SuppressLint("SimpleDateFormat")
    private var mSimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    @SuppressLint("SimpleDateFormat")
    private val mSimpleDateFormat1 = SimpleDateFormat("yyyy-MM-dd")
    private val mCalendar = Calendar.getInstance()


    @JvmStatic
    fun init(logFilePath: String = LogConfig.DEFAULT_LOG_DIRECTORY) {
        LogConfig
            .addLogFile("devLog")
            .addLogFile("vmcLog")
            .addLogFile("deBugLog")
            .DEFAULT_LOG_DIRECTORY = logFilePath
    }

    @JvmStatic
    fun addLogFile(logTag: String) {
        if (logTag.isNotEmpty()) {
            LogConfig.addLogFile(logTag)
        }
    }

    @JvmStatic
    fun devLog(tag: String = TAG, msg: String) {
        tagLog(msg, tag, LogConfig.DEV_TAG)
    }

    @JvmStatic
    fun deBugLog(tag: String = TAG, msg: String) {
        tagLog(msg, tag, LogConfig.DEBUG_TAG)
    }

    @JvmStatic
    fun vmcLog(tag: String = TAG, msg: String) {
        tagLog(msg, tag, LogConfig.VMC_TAG)
    }

    @JvmStatic
    fun otherTagLog(tag: String = TAG, msg: String, logTag: String) {
        tagLog(msg, tag, logTag)
    }

    private fun tagLog(msg: String, tag: String, logTag: String) {
        if (LogLevel.DEBUG.value <= logLevel.value) {
            if (msg.isNotBlank()) {
                val s = getMethodNames(checkMsgLength(msg))
                val string = String.format(s, msg)
                Log.d(tag, string)
                val filePath = getLogFilePath(logTag)
                if (!filePath.isNullOrEmpty()) {
                    writeText(string, filePath)
                }
            }
        }
    }

    private fun checkMsgLength(msg: String): Boolean {
        if (msg.length > 70) {
            return true
        }
        return false
    }

    @SuppressLint("SimpleDateFormat")
    fun getSimpleDateString(): String {
        return if (android.os.Build.VERSION.SDK_INT >= 24) {
            mSimpleDateFormat1.format(Date())
        } else {
            mCalendar.get(Calendar.YEAR).toString() + "-" + mCalendar.get(Calendar.MONTH).toString() + "-" + mCalendar.get(
                Calendar.DAY_OF_MONTH
            ).toString()
        }
    }

    private fun getLogFilePath(logTag: String): String? {
        if (LogConfig.logHashMap.containsKey(logTag)) {
            return LogConfig.generateDefaultLogDirectory()!!
                .plus("/")
                .plus(logTag)
                .plus("-")
                .plus(getSimpleDateString())
                .plus(LogConfig.SUFFIX)
        } else {
            e(TAG, "$logTag can't find logHashMap")
        }
        return null
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
        if (!mFile.exists()) {
            mFile.createNewFile()
        }
        val timeStringBuilder = mTimeStringBuilder.append(getNow()).append(" - ").append(text)
        mFile.appendText(timeStringBuilder.toString(), Charset.defaultCharset())
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
    fun json(json: String, logTag: String = "") {
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
                writeJsonToFile(logTag, string)
                return
            }
            if (json.startsWith("[")) {
                val jsonArray = JSONArray(json)
                var message = jsonArray.toString(LoggerPrinter.JSON_INDENT)
                message = message.replace("\n".toRegex(), "\n║ ")
                val s = getJsonMethodNames()
                val string = String.format(s, message)
                println(string)
                writeJsonToFile(logTag, string)
                return
            }
            e(msg = "Invalid Json")
        } catch (e: JSONException) {
            e(msg = "Invalid Json")
        }

    }

    private fun writeJsonToFile(logTag: String, string: String) {
        if (logTag.isNotEmpty()) {
            val filePath = getLogFilePath(logTag)
            if (!filePath.isNullOrEmpty()) {
                writeText(string, filePath)
            }
        }
    }

    private fun getJsonMethodNames(): String {
        val sElements = Thread.currentThread().stackTrace
        var stackOffset = LoggerPrinter.getStackOffset(sElements)
        stackOffset++
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

    @SuppressLint("SimpleDateFormat")
    private fun getNow(): String {
        return if (android.os.Build.VERSION.SDK_INT >= 24) {
            mSimpleDateFormat.format(Date())
        } else {
            mCalendar.get(Calendar.YEAR).toString() + "-" + mCalendar.get(Calendar.MONTH).toString() + "-" + mCalendar.get(
                Calendar.DAY_OF_MONTH
            ).toString() + " " + mCalendar.get(Calendar.HOUR_OF_DAY).toString() + ":" + mCalendar.get(
                Calendar.MINUTE
            ).toString() + ":" + mCalendar.get(
                Calendar.SECOND
            ).toString() + "." + mCalendar.get(Calendar.MILLISECOND).toString()
        }
    }

    private fun getMethodNames(checkMsgLength: Boolean): String {
        val sElements = Thread.currentThread().stackTrace
        var stackOffset = LoggerPrinter.getStackOffset(sElements)
        stackOffset++
        val builder = StringBuilder()
        //时间
        builder
            .append("["  + Thread.currentThread().name).append("]")
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

