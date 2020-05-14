package com.wsg

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.wsg.common.AbstractLogConfig

/**
 *  @author WuSG
 *  @date : 2020-05-14 09:47
 */
class LoggerRuntimeProvider : ContentProvider() {

    var isInit = false
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }
    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return null
    }

    override fun onCreate(): Boolean {
        if (!isInit) {
            AbstractLogConfig.APP = context!!.applicationContext as Application
            isInit = true
        }
        return isInit
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return -1
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return -1
    }

    override fun getType(uri: Uri): String? {
        return null
    }

}