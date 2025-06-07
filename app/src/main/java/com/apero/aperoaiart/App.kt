package com.apero.aperoaiart

import android.app.Application
import com.apero.aperoaiart.di.DIContainer

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        DIContainer.init(this)
    }
}