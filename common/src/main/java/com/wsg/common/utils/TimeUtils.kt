package com.wsg.common.utils

import android.annotation.SuppressLint
import com.wsg.common.Logger
import java.text.SimpleDateFormat
import java.util.*

/**
 *  @author WuSG
 *  @date : 2020-01-07 15:06
 */
class TimeUtils {
    companion object {
        @SuppressLint("SimpleDateFormat")
        val mSimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        @SuppressLint("SimpleDateFormat")
        val mSimpleDateFormat2 = SimpleDateFormat("yyyy-MM")
        @SuppressLint("SimpleDateFormat")
        val mSimpleDateFormat3 = SimpleDateFormat("yyyy-MM-dd HH:MM:ss")
        private val mCalendar = Calendar.getInstance()
        @SuppressLint("SimpleDateFormat")
        fun getSimpleDateString(): String {
            return if (android.os.Build.VERSION.SDK_INT >= 24) {
                mSimpleDateFormat.format(Date())
            } else {
                mCalendar.get(Calendar.YEAR).toString() + "-" + mCalendar.get(Calendar.MONTH).toString() + "-" + mCalendar.get(
                    Calendar.DAY_OF_MONTH
                ).toString()
            }
        }

        fun getSimpleMonth(): String {
            return if (android.os.Build.VERSION.SDK_INT >= 24) {
                mSimpleDateFormat2.format(Date())
            } else {
                mCalendar.get(Calendar.YEAR).toString() + "-" + mCalendar.get(Calendar.MONTH).toString()
            }
        }

        @SuppressLint("SimpleDateFormat")
        fun getNow(): String {
            return if (android.os.Build.VERSION.SDK_INT >= 24) {
                mSimpleDateFormat3.format(Date())
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
    }

}