package com.apero.aperoaiart.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

object DIContainer {

    fun init(application: Application) {
        startKoin {
            androidContext(application)
            modules(appModule)
        }
    }
}