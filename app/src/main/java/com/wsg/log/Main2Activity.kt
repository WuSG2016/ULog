package com.wsg.log

import android.app.Activity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.wsg.common.LogConfig
import com.wsg.common.Logger

import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        Logger.init()
        val json = "{" +
                " \"code\":\"50002\"," + " \"message\":\"版本可选更新\"," + " \"resultData\":{" + "  \"foreUpdate\":false," +
                "  \"updateUrl\":\"http://127.0.0.1/android/test\"" +
                " }," +
                " \"success\":true," +
                " \"token\":\"tokenfslfjsdfsf\"" +
                "}"
        Logger.json(json, LogConfig.DEV_TAG)
        Logger.devLog(msg = json)
        Logger.devLog(msg = "dada")
//        Logger.devLog(msg = "dada")
//        Logger.devLog(msg = "dada")
////        Logger.devLog(msg = "dada")
////        Logger.devLog(msg = "dada")
////        Logger.devLog(msg = "dada")
////        Logger.devLog(msg = "dada")
////        Logger.devLog(msg = "dada")
////        Logger.devLog(msg = "dada")
////        Logger.devLog(msg = "dada")
////        Logger.devLog(msg = "dada")
////        Logger.devLog(msg = "dada")
////        Logger.devLog(msg = json)
    }

}
