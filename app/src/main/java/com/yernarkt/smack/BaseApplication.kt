package com.yernarkt.smack

import android.app.Application
import com.yernarkt.smack.util.SharedPrefsUtil

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        prefs = SharedPrefsUtil(applicationContext)
    }

    companion object {
        lateinit var prefs: SharedPrefsUtil
    }
}