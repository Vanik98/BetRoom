package com.vanik.betroom

import android.app.Application
import com.vanik.betroom.data.modules.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class GrowApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@GrowApp)
            modules(appModules)
        }
    }
}