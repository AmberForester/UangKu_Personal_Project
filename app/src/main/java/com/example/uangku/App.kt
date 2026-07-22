package com.example.uangku

import android.app.Application
import com.example.uangku.core.dependencyInjection.AppContainer

class App: Application() {

    lateinit var appContainer: AppContainer
        private set

    override fun onCreate(){
        super.onCreate()
        appContainer = AppContainer(this)
    }
}