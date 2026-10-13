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
        ensureAppContainer()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun ensureAppContainer(): AppContainer {
        if (!::appContainer.isInitialized) {
            appContainer = AppContainer(this)
        }
        return appContainer
    }

    override val workManagerConfiguration: Configuration
        @RequiresApi(Build.VERSION_CODES.O)
        get() = Configuration.Builder()
            .setWorkerFactory(
                ensureAppContainer().autoBackupWorkerFactory
            ).build()
}