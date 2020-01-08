package com.wsg.log;

import android.app.Application;

/**
 * @author WuSG
 * @date : 2020-01-08 14:44
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        _UboxLog.Companion.init(this);
    }
}
