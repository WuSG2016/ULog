package com.wsg.log

import android.app.Activity
import android.os.Bundle
import com.wsg.common.Logger
import com.wsg.common.utils.*
import java.io.File

class Main2Activity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val json = "{" +
                " \"code\":\"50002\"," + " \"message\":\"版本可选更新\"," + " \"resultData\":{" + "  \"foreUpdate\":false," +
                "  \"updateUrl\":\"http://127.0.0.1/android/test\"" +
                " }," +
                " \"success\":true," +
                " \"token\":\"tokenfslfjsdfsf\"" +
                "}"
        _UboxLog.json(json, "devLog")
        _UboxLog.devLog("dda")
        TestULog.Log("dadada")
        zip()

    }

    private fun zip() {

        val file = File("/mnt/sdcard/ULog/output.zip")
        //解压
        if (file.smartCreateNewFile()) {
            file.unZipTo("/mnt/sdcard/Ulog/2020-02")
        }
        //压缩
        file.zipOutputStream()
            .zipFrom(
                "/mnt/sdcard/Ulog/2020-01/dev",
                "/mnt/sdcard/Ulog/2020-01/devLog/devLog-2020-01-9.log"
            )


    }

}
