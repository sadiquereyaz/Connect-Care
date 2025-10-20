package com.reyaz.connectcare

import android.app.Application
import com.reyaz.connectcare.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Reference Android context
            androidContext(this@BaseApplication)
            // Load modules
            modules(
                appModule
            )
        }
    }
}