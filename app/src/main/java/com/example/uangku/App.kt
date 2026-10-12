package com.example.uangku

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.Configuration
import com.example.uangku.core.dependency_Injection.AppContainer

class App: Application(), Configuration.Provider {

    lateinit var appContainer: AppContainer
        private set

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(){
        super.onCreate()
        appContainer = AppContainer(this)
    }

    override val workManagerConfiguration: Configuration
        @RequiresApi(Build.VERSION_CODES.O)
        get() = Configuration.Builder()
            .setWorkerFactory(
                appContainer.autoBackupWorkerFactory
            ).build()
}