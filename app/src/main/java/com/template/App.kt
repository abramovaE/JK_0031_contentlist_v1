package com.template

import android.app.Application

class App: Application() {

    private lateinit var cacheFiles: Array<String?>
    private lateinit var fileNamesList: Array<String>

    override fun onCreate() {
        super.onCreate()
        fileNamesList = assets.list("pages")!!
        cacheFiles = arrayOfNulls<String>(fileNamesList.size)

    }
}