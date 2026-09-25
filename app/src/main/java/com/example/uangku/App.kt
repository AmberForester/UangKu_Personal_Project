package com.example.uangku

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.uangku.core.dependencyInjection.AppContainer

class App: Application() {

    lateinit var appContainer: AppContainer
        private set

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(){
        super.onCreate()
        appContainer = AppContainer(this)
    }
}