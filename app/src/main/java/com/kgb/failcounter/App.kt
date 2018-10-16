package com.kgb.failcounter

import android.app.Application

class App : Application() {
    var editMode = false

    override fun onCreate() {
        super.onCreate()
        _instance = this
    }

    companion object {
        private lateinit var _instance : App
        val instance: App
            get() = _instance


    }
}