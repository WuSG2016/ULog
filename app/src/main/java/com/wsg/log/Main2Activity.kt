package com.wsg.log

import android.app.Activity
import android.os.Bundle
import com.wsg.common.Logger

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
        _UboxLog.json(json,"devLog")
        _UboxLog.dev("dda")
        TestULog.Log("dadada")

    }

}
